package net.nsreverse.bakingapp.ui;

import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import net.nsreverse.bakingapp.R;
import net.nsreverse.bakingapp.data.utils.RuntimeCache;
import net.nsreverse.bakingapp.model.InstructionStep;
import net.nsreverse.bakingapp.ui.fragments.RecipeStepDetailFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class represents the step detail activity and is only available on phones.
 *
 * @author Robert
 * Created on 6/7/2017
 */
public class RecipeStepDetailActivity extends AppCompatActivity {

    public static final String RECIPE_INDEX_KEY = "RECIPE_INDEX";
    public static final String INSTRUCTION_INDEX_KEY = "INSTRUCTION_INDEX";

    @Nullable @BindView(R.id.image_view_chevron_left) ImageView leftImageView;
    @Nullable @BindView(R.id.image_view_chevron_right) ImageView rightImageView;

    private int recipeIndex;
    private int instructionIndex;

    /**
     * This is the main entry point for this Activity.
     *
     * @param savedInstanceState A Bundle containing previously stored save state. (if any).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getSupportActionBar() != null;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
        }
        else {
            getSupportActionBar().show();
        }

        setContentView(R.layout.activity_recipe_step_detail);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (savedInstanceState != null) {
                recipeIndex = savedInstanceState.getInt(RECIPE_INDEX_KEY, 0);
                instructionIndex = savedInstanceState.getInt(INSTRUCTION_INDEX_KEY, 0);
            }
            else {
                recipeIndex = getIntent().getIntExtra(RECIPE_INDEX_KEY, 0);
                instructionIndex = getIntent().getIntExtra(INSTRUCTION_INDEX_KEY, 0);
            }

            InstructionStep currentStep = RuntimeCache.recipes[recipeIndex].getSteps()
                    [instructionIndex];

            RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
            fragment.setDataSource(currentStep);
            fragment.setIndices(recipeIndex, instructionIndex);

            if (savedInstanceState != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_detail_fragment_container, fragment)
                        .commit();
            }
            else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_detail_fragment_container, fragment)
                        .commit();
            }

            getSupportActionBar().setTitle(getString(R.string.activity_view_step_detail));

            if (leftImageView != null) {
                leftImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goBack();
                    }
                });
            }

            if (rightImageView != null) {
                rightImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goForward();
                    }
                });
            }
        }
    }

    /**
     * This method sets the selected index back one if the current is not zero. It the reloads
     * the fragment based on the new index.
     */
    private void goBack() {
        if (instructionIndex > 0) {
            instructionIndex--;

            reloadFragment();
        }
    }

    /**
     * This method sets the selected index forward one if the current is not the length of the
     * array - 1. It then reloads the fragment based on the new index.
     */
    private void goForward() {
        if (instructionIndex < RuntimeCache.recipes[recipeIndex].getStepCount() - 1) {
            instructionIndex++;

            reloadFragment();
        }
    }

    /**
     * This method swaps out the current fragment for a fresh one.
     */
    private void reloadFragment() {
        InstructionStep currentStep = RuntimeCache.recipes[recipeIndex].getSteps()
                [instructionIndex];

        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setDataSource(currentStep);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_detail_fragment_container, fragment)
                .commit();
    }

    /**
     * This method creates a save state bundle for recreating save state pre-configuration.
     *
     * @param outState The Bundle to store saved state information.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(RECIPE_INDEX_KEY, recipeIndex);
        outState.putInt(INSTRUCTION_INDEX_KEY, instructionIndex);
    }

    /**
     * This method handles click events in the ActionBar.
     *
     * @param item A MenuItem object representing the selected item in the ActionBar.
     * @return A boolean if the click event was handled.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
