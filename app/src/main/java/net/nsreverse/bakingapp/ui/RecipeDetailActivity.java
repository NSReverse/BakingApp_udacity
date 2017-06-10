package net.nsreverse.bakingapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import net.nsreverse.bakingapp.data.utils.ApplicationInstance;
import net.nsreverse.bakingapp.R;

import net.nsreverse.bakingapp.data.utils.RuntimeCache;
import net.nsreverse.bakingapp.model.InstructionStep;
import net.nsreverse.bakingapp.model.Recipe;
import net.nsreverse.bakingapp.ui.adapters.RecipeDetailAdapter;
import net.nsreverse.bakingapp.ui.fragments.RecipeDetailFragment;
import net.nsreverse.bakingapp.ui.fragments.RecipeStepDetailFragment;

/**
 * This class represents the recipe detail activity. On phones it only displays the steps in the
 * RecyclerView. On tablets, it additionally displays the step detail fragment.
 *
 * @author Robert
 * Created on 6/7/2017
 */
public class RecipeDetailActivity extends AppCompatActivity
                                  implements RecipeDetailAdapter.Delegate {

    public static final String POSITION_KEY = "POSITION";

    private static boolean sHasInitialized = false;

    /**
     * This is the main entry point for this Activity.
     *
     * @param savedInstanceState A Bundle containing previously stored state information
     *                           (if any).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (getSupportActionBar() != null && getIntent().hasExtra(POSITION_KEY)) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            int position = getIntent().getIntExtra(POSITION_KEY, 0);

            Recipe selectedRecipe = RuntimeCache.recipes[position];

            getSupportActionBar().setTitle(selectedRecipe.getName());

            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setSelectedIndex(position);

            if (!sHasInitialized) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.detail_fragment_container, fragment)
                        .commit();

                sHasInitialized = true;
            }
            else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment_container, fragment)
                        .commit();
            }
        }
    }

    /**
     * This method is a callback from a click event from the adapter.
     *
     * @param step An InstructionStep object containing information about the selected recipe.
     * @param rowPosition An int representing the index of the selected row.
     */
    @Override
    public void itemSelected(InstructionStep step, int rowPosition) {
        if (!ApplicationInstance.isTablet) {
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.putExtra(RecipeStepDetailActivity.RECIPE_INDEX_KEY,
                    getIntent().getIntExtra(POSITION_KEY, 0));
            intent.putExtra(RecipeStepDetailActivity.INSTRUCTION_INDEX_KEY, rowPosition);
            startActivity(intent);
        }
        else {
            int position = getIntent().getIntExtra(POSITION_KEY, 0);

            Recipe selectedRecipe = RuntimeCache.recipes[position];
            InstructionStep currentStep = selectedRecipe.getSteps()[rowPosition];

            RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
            fragment.setDataSource(currentStep);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_fragment_container, fragment)
                    .commit();
        }
    }

    /**
     * This method handles click events in the ActionBar.
     *
     * @param item A MenuItem representing the selected item in the ActionBar.
     * @return A boolean representing if the click event was handled.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        else if (item.getItemId() == R.id.action_ingredients) {
            int position = getIntent().getIntExtra(POSITION_KEY, 0);

            Intent intent = new Intent(this, ListIngredientsActivity.class);
            intent.putExtra(POSITION_KEY, position);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method sets a custom menu to the ActionBar in this Activity.
     *
     * @param menu The menu to inflate to.
     * @return A boolean representing if the menu inflated.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_detail, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
