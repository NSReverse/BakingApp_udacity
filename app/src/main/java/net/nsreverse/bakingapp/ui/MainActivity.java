package net.nsreverse.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import net.nsreverse.bakingapp.data.utils.ApplicationInstance;
import net.nsreverse.bakingapp.R;
import net.nsreverse.bakingapp.data.background.RecipeAsyncTask;
import net.nsreverse.bakingapp.data.utils.RuntimeCache;
import net.nsreverse.bakingapp.model.Recipe;
import net.nsreverse.bakingapp.ui.adapters.MainActivityAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class represents the main activity and entry point in the app.
 *
 * @author Robert
 * Created on 6/7/2017
 */
public class MainActivity extends AppCompatActivity
                          implements RecipeAsyncTask.Delegate,
                                     MainActivityAdapter.Delegate {

    private static final String JSON_KEY = "JSON";

    @BindView(R.id.linear_layout_main_error) LinearLayout mErrorLayout;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.recyclerview_main) RecyclerView mRecyclerView;
    @SuppressWarnings("FieldCanBeLocal") MainActivityAdapter mRecipeAdapter;

    @Nullable @BindView(R.id.tablet_flag) View tabletFlag; // Trick to find out if in tablet layout

    private String json;

    /**
     * This method is the main entry point of this app.
     *
     * @param savedInstanceState A Bundle containing previous state information (if any.)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        showErrorLayout(false);

        refreshData();
    }

    /**
     * This method refreshes the data source of the main menu.
     */
    private void refreshData() {
        mProgressBar.setVisibility(View.VISIBLE);

        if (RuntimeCache.recipes != null) {
            reloadData();
        }
        else {
            new RecipeAsyncTask(this).execute();
        }
    }

    /**
     * This method reloads the current data source into the adapter.
     */
    private void reloadData() {
        mRecipeAdapter = new MainActivityAdapter(this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        if(!isTablet()) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        else {
            if (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_PORTRAIT) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            }
            else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            }
        }

        mRecipeAdapter.setRecipeData(RuntimeCache.recipes);

        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * This method hides the RecyclerView and shows the layout containing an error message.
     *
     * @param show Whether the message should be shown.
     */
    private void showErrorLayout(boolean show) {
        mRecyclerView.setVisibility((show)? View.INVISIBLE : View.VISIBLE);
        mErrorLayout.setVisibility((show)? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * This method determines if the app is being currently ran on a tablet.
     *
     * @return A boolean representing if the current device is a tablet.
     */
    private boolean isTablet() {
        if (tabletFlag != null) {
            ApplicationInstance.isTablet = true;
        }

        return tabletFlag != null;
    }

    /**
     * This method is a callback for when new JSON has been downloaded. Only for caching purposes.
     *
     * @param json A String representing downloaded JSON.
     */
    @Override
    public void jsonRecieved(String json) {
        this.json = json;

        SharedPreferences preferences =
                getSharedPreferences("BakingAppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(JSON_KEY, json);
        editor.apply();
    }

    /**
     * This method is a callback for when downloaded JSON has been parsed into an array of Recipe
     * objects.
     *
     * @param recipes The new Recipe data source.
     */
    @Override
    public void taskFinishedWithRecipes(Recipe[] recipes) {
        if (recipes != null) {
            RuntimeCache.recipes = recipes;
            reloadData();
        }
    }

    /**
     * This method is a callback for when the downloading async task has encountered an error.
     *
     * @param message A String representing the error.
     */
    @Override
    public void taskFinishedWithErrorMessage(String message) {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * This method is a callback for when an item was selected in the RecyclerView.
     *
     * @param recipe A Recipe that was selected.
     * @param position An int representing the position in the RecyclerView.
     */
    @Override
    public void itemSelectedWith(Recipe recipe, int position) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.POSITION_KEY, position);
        startActivity(intent);
    }

    /**
     * This method stores state information before a configuration change.
     *
     * @param outState A Bundle containing state information.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(JSON_KEY, json);
    }

    /**
     * This method retrieves state information after a configuration change.
     *
     * @param savedInstanceState A Bundle containing state information.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(JSON_KEY)) {
            json = savedInstanceState.getString(JSON_KEY);
        }
    }
}
