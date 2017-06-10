package net.nsreverse.bakingapp.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.nsreverse.bakingapp.R;
import net.nsreverse.bakingapp.data.utils.RuntimeCache;
import net.nsreverse.bakingapp.model.Ingredient;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class is a very simple class that displays a list of ingredients for a Recipe.
 *
 * @author Robert
 * Created on 6/10/2017
 */
public class ListIngredientsActivity extends AppCompatActivity {

    @BindView(R.id.list_view_ingredients) ListView ingredientsListView;

    /**
     * This method handles the
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ingredients);

        int recipePosition = getIntent().getIntExtra(RecipeDetailActivity.POSITION_KEY, 0);
        final Ingredient[] ingredients = RuntimeCache.recipes[recipePosition].getIngredients();

        if (getSupportActionBar() != null) {
            assert getSupportActionBar() != null;
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(RuntimeCache.recipes[recipePosition].getName());

        ButterKnife.bind(this);

        ArrayAdapter adapter = new ArrayAdapter<Ingredient>(this,
                R.layout.activity_list_ingredients, ingredients) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {

                if (convertView == null) {
                    convertView = View.inflate(ListIngredientsActivity.this,
                            R.layout.item_widget_ingredient, null);
                }

                TextView titleTextView =
                        (TextView)convertView.findViewById(R.id.text_view_widget_detail_title);
                TextView subtitleTextView =
                        (TextView)convertView.findViewById(R.id.text_view_widget_detail_subtitle);

                titleTextView.setText(ingredients[position].getName());
                subtitleTextView.setText(
                        ingredients[position].getQuantity() + " " +
                        ingredients[position].getMeasurement()
                );

                return convertView;
            }
        };

        ingredientsListView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
