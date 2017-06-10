package net.nsreverse.bakingapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.nsreverse.bakingapp.data.utils.ApplicationInstance;
import net.nsreverse.bakingapp.R;
import net.nsreverse.bakingapp.model.InstructionStep;
import net.nsreverse.bakingapp.ui.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This adapter provides a RecyclerView to navigate through the list of steps in a recipe.
 * On tablets, this also provides the fragment that shows the detail description and video
 * for the step.
 *
 * @author Robert
 * Created on 6/7/2017.
 */
public class RecipeDetailAdapter
        extends RecyclerView.Adapter<RecipeDetailAdapter.InstructionViewHolder> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private InstructionStep[] mInstructionsData;
    private Delegate delegate;

    /**
     * Implement this interface to receive click events on the recipe detail RecyclerView.
     */
    public interface Delegate {
        void itemSelected(InstructionStep step, int rowPosition);
    }

    /**
     * This is the basic constructor for creating a new RecipeDetailAdapter.
     *
     * @param delegate A Context that must implement this class' Delegate interface.
     */
    public RecipeDetailAdapter(Context delegate) {
        this.delegate = (Delegate)delegate;
    }

    /**
     * This method creates a new view holder for this adapter.
     *
     * @param parent The container ViewGroup.
     * @param viewType Unused.
     * @return A new view holder for this adapter.
     */
    @Override
    public InstructionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_recipe_detail, parent, false);
        return new InstructionViewHolder(view);
    }

    /**
     * This method binds a data source to this adapter's view holders based on position.
     *
     * @param holder The view holder.
     * @param position The view holder's position.
     */
    @Override
    public void onBindViewHolder(InstructionViewHolder holder, int position) {
        InstructionStep currentInstruction = mInstructionsData[position];

        if (currentInstruction != null) {
            holder.titleTextView.setText(currentInstruction.getShortDescription());
        }
        else {
            if (ApplicationInstance.loggingEnabled) Log.d(TAG, "Failed to bind to view holder.");
        }
    }

    /**
     * This method gives the current size of the data source.
     *
     * @return An int representing the size of the data source.
     */
    @Override
    public int getItemCount() {
        if (mInstructionsData != null) {
            return mInstructionsData.length;
        }

        return 0;
    }

    /**
     * This method sets a new data source to this adapter.
     *
     * @param data The new data source.
     */
    public void setInstructionsData(InstructionStep[] data) {
        mInstructionsData = data;
        notifyDataSetChanged();
    }

    /**
     * This class defines the view holders for this adapter.
     */
    class InstructionViewHolder extends RecyclerView.ViewHolder
                                implements View.OnClickListener {
        @BindView(R.id.text_view_title) TextView titleTextView;

        /**
         * Basic constructor for creating a new InstructionViewHolder.
         *
         * @param v A view that is used by ButterKnife to bind views to Java source.
         */
        InstructionViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        /**
         * This method passes a click event to the Delegate implementing object.
         *
         * @param v Unused View.
         */
        @Override
        public void onClick(View v) {
            if (delegate != null) {
                delegate.itemSelected(mInstructionsData[getAdapterPosition()],
                        getAdapterPosition());
            }
        }
    }
}
