package net.nsreverse.bakingapp.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.nsreverse.bakingapp.data.utils.ApplicationInstance;
import net.nsreverse.bakingapp.R;
import net.nsreverse.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class is an adapter for MainActivity for a provided Recipe data source.
 *
 * @author Robert
 * Created on 6/7/2017.
 */
public class MainActivityAdapter
        extends RecyclerView.Adapter<MainActivityAdapter.RecipeAdapterViewHolder> {

    private static final String TAG = MainActivityAdapter.class.getSimpleName();

    private Recipe[] mRecipeData;
    private Delegate delegate;

    /**
     * Implement this interface to handle click events on the main RecyclerView.
     */
    public interface Delegate {
        void itemSelectedWith(Recipe recipe, int position);
    }

    /**
     * Basic constructor for creating a new MainActivityAdapter.
     *
     * @param delegate The Delegate-implementing Activity.
     */
    public MainActivityAdapter(Delegate delegate) {
        this.delegate = delegate;
    }

    /**
     * Method that gets the data source size.
     *
     * @return An int representing the current size of the data source.
     */
    @Override
    public int getItemCount() {
        if (mRecipeData != null) {
            return mRecipeData.length;
        }

        return 0;
    }

    /**
     * This method sets a new data source to this adapter.
     *
     * @param recipeData An array of Recipe objects.
     */
    public void setRecipeData(Recipe[] recipeData) {
        mRecipeData = recipeData;
        notifyDataSetChanged();
    }

    /**
     * This method creates a new view holder for this adapter.
     *
     * @param parent The container view group.
     * @param viewType Unused.
     * @return A new RecipeAdapterViewHolder.
     */
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_recipe_main, parent, false);
        return new RecipeAdapterViewHolder(view);
    }

    /**
     * This method sets the data source to the view holders in this adapter based on position.
     *
     * @param holder The view holder.
     * @param position The position of the view holder.
     */
    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        Recipe currentRecipe = mRecipeData[position];

        if (currentRecipe != null) {
            if (currentRecipe.getRecipeThumbnail() != null) {
                holder.iconImageView.setImageBitmap(currentRecipe.getRecipeThumbnail());
            }

            holder.titleTextView.setText(currentRecipe.getName());

            String subtitle = "".concat(Integer.toString(currentRecipe.getIngredientCount()))
                    .concat(" Ingredients, ")
                    .concat(Integer.toString(currentRecipe.getStepCount()))
                    .concat(" Steps");

            holder.subtitleTextView.setText(subtitle);
        }
        else {
            if (ApplicationInstance.loggingEnabled) Log.d(TAG, "Failed to bind ViewHolder.");
        }
    }

    /**
     * This class defines a view holder for this adapter.
     */
    class RecipeAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_view_recipe_icon) ImageView iconImageView;
        @BindView(R.id.text_view_title) TextView titleTextView;
        @BindView(R.id.text_view_subtitle) TextView subtitleTextView;
        @BindView(R.id.button_view) Button viewButton;

        /**
         * Basic constructor for creating a new RecipeAdapterViewHolder.
         * @param v The view of the viewholder. Used by butterknife to bind views to Java source.
         */
        RecipeAdapterViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delegate.itemSelectedWith(mRecipeData[getAdapterPosition()],
                            getAdapterPosition());
                }
            });
        }
    }
}
