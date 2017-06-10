package net.nsreverse.bakingapp.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import net.nsreverse.bakingapp.R;
import net.nsreverse.bakingapp.data.utils.RuntimeCache;
import net.nsreverse.bakingapp.model.InstructionStep;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This fragment is a detail view for a step in the baking process. Some steps come with a video.
 *
 * @author Robert
 * Created on 6/8/2017.
 */
public class RecipeStepDetailFragment extends Fragment
                                      implements ExoPlayer.EventListener {

    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();

    private static int selectedRecipeIndex;
    private static int selectedInstructionIndex;

    boolean shouldHideExoPlayer = false;

    private SimpleExoPlayer mExoPlayer;
    @BindView(R.id.exoplayer_view) SimpleExoPlayerView mPlayerView;
    @Nullable @BindView(R.id.text_view_title) TextView titleTextView;

    private Context context;

    private InstructionStep step;

    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    public RecipeStepDetailFragment() { }

    /**
     * This method sets a new data source to this fragment.
     *
     * @param step The InstructionStep object to set to this fragment for displaying instruction
     *             information.
     */
    public void setDataSource(InstructionStep step) {
        this.step = step;

        if (!step.getVideoURL().contains(".mp4")) {
            if (!step.getThumbURL().contains(".mp4")) {
                shouldHideExoPlayer = true;
            }
        }
    }

    /**
     * This method sets the index of the recipe and the step.
     *
     * @param recipe An int representing which recipe to use.
     * @param step An int representing which step to use in the selected recipe.
     */
    public void setIndices(int recipe, int step) {
        selectedRecipeIndex = recipe;
        selectedInstructionIndex = step;
    }

    /**
     * This method attaches the parent Context to this fragment.
     *
     * @param context The Context of the parent Activity.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;

        initializeSession();
    }

    /**
     * This method initializes a new MediaSession for video playback.
     */
    private void initializeSession() {
        mMediaSession = new MediaSessionCompat(context, TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE
                );

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new SessionCallback());
        mMediaSession.setActive(true);
    }

    /**
     * This method initializes a new ExoPlayer for media playback.
     *
     * @param resourceUri The Uri representing the location of the remote media.
     */
    private void initializePlayer(Uri resourceUri) {
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector(),
                new DefaultLoadControl(), null, 0);

        DefaultDataSourceFactory mediaDataSourceFactory = new DefaultDataSourceFactory(
                context, Util.getUserAgent(
                context,
                Util.getUserAgent(context, "BakingApp")));

        mPlayerView.setPlayer(mExoPlayer);

        mExoPlayer.setPlayWhenReady(true);

        DefaultExtractorsFactory factory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(resourceUri, mediaDataSourceFactory,
                factory, null, null);

        mExoPlayer.prepare(mediaSource);
    }

    /**
     * This method inflates the view of this fragment.
     *
     * @param inflater The LayoutInflater to inflate this fragment's View.
     * @param container The ViewGroup container.
     * @param savedInstanceState A Bundle containing previous state information (if any.)
     * @return An inflated View for this fragment.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.layout_recipe_step_detail_fragment,
                container, false);
        ButterKnife.bind(this, root);

        if (step == null) {
            step = RuntimeCache.recipes[selectedRecipeIndex].getSteps()[selectedInstructionIndex];
        }

        mPlayerView = (SimpleExoPlayerView)root.findViewById(R.id.exoplayer_view);

        if (titleTextView != null) {
            titleTextView.setText(step.getDescription());
        }

        if (!shouldHideExoPlayer) {
            if (!step.getVideoURL().contains(".mp4")) {
                if (step.getThumbURL().contains(".mp4")) {
                    initializePlayer(Uri.parse(step.getThumbURL()));
                }
            }
            else {
                initializePlayer(Uri.parse(step.getVideoURL()));
            }

            mPlayerView.setVisibility(View.VISIBLE);
        }
        else {
            mPlayerView.setVisibility(View.GONE);
        }

        return root;
    }

    /**
     * Class of Media session callbacks.
     */
    private class SessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    /**
     * Releases ExoPlayer and it's resources when no longer needed.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    /**
     * This method prepares for destroying this fragment's views.
     */
    @Override
    public void onDestroyView() {
        releasePlayer();
        mMediaSession.setActive(false);

        super.onDestroyView();
    }

    //region: --Unused--
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }
    //endregion

    /**
     * This method detects change in player state.
     *
     * @param playWhenReady A boolean representing if the player should play.
     * @param playbackState An int representing the current state of the player.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        else if (playbackState == ExoPlayer.STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }

        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    //region: --Unused--
    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
    //endregion
}
