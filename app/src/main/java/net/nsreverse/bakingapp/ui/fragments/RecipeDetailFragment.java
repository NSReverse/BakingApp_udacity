package net.nsreverse.bakingapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.nsreverse.bakingapp.R;
import net.nsreverse.bakingapp.data.utils.RuntimeCache;
import net.nsreverse.bakingapp.ui.adapters.RecipeDetailAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This fragment displays a detail menu based on the contents of a Recipe.
 *
 * @author Robert
 * Created on 6/7/2017.
 */
public class RecipeDetailFragment extends Fragment {

    private int selectedIndex;

    @BindView(R.id.recyclerview_recipe_detail) RecyclerView mDetailRecyclerView;
    private RecipeDetailAdapter mAdapter;
    private Context context;

    /**
     * This method sets the selected index of the recipe detail.
     *
     * @param index An int representing the new index to be selected.
     */
    public void setSelectedIndex(int index) {
        selectedIndex = index;
    }

    public RecipeDetailFragment() { }

    /**
     * This method attaches the context to the fragment.
     *
     * @param context The parent Activity's Context;
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    /**
     * This method inflates the View for this fragment.
     *
     * @param inflater The LayoutInflater to inflate the view with.
     * @param container The containing view group.
     * @param savedInstanceState A Bundle containing state information if it previously exists.
     * @return An inflated View for this fragment.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.layout_recipe_detail_fragment,
                container, false);
        ButterKnife.bind(this, root);

        mAdapter = new RecipeDetailAdapter(context);
        mDetailRecyclerView.setAdapter(mAdapter);
        mDetailRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mAdapter.setInstructionsData(RuntimeCache.recipes[selectedIndex].getSteps());

        return root;
    }
}
