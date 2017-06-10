package net.nsreverse.bakingapp.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import net.nsreverse.bakingapp.R;
import net.nsreverse.bakingapp.data.utils.JsonParser;
import net.nsreverse.bakingapp.data.utils.RuntimeCache;
import net.nsreverse.bakingapp.model.Recipe;

import static net.nsreverse.bakingapp.ui.widget.BakingAppWidget.RECIPE_POSITION;

/**
 * This service populates initial data in a widget.
 *
 * @author Robert
 * Created on 6/9/2017.
 */
public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

/**
 * Class used by ListWidgetService to update the ListView containing recipes.
 */
class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private Recipe[] recipes;

    /**
     * Constructor for creating a new ListRemoteViewsFactory.
     *
     * @param context The application context.
     */
    ListRemoteViewsFactory(Context context) {
        this.context = context;
    }

    /**
     * This method is unused.
     */
    @Override
    public void onCreate() { }

    /**
     * This method is called whenever the backend is changed and the data source should be
     * updated.
     */
    @Override
    public void onDataSetChanged() {
        SharedPreferences preferences = context.getSharedPreferences("BakingAppPreferences",
                Context.MODE_PRIVATE);
        String storedJson = preferences.getString("JSON", "Default value");

        RuntimeCache.recipes = JsonParser.recipesWithJsonNoImage(context, storedJson);
        recipes = JsonParser.recipesWithJsonNoImage(context, storedJson);
    }

    /**
     * This method is called to free up unused resources when this is destroyed.
     */
    @Override
    public void onDestroy() { recipes = null; }

    /**
     * This method gets the current size of the data source.
     *
     * @return An int representing the size of the data source.
     */
    @Override
    public int getCount() {
        if (recipes != null) {
            return recipes.length;
        }

        return 0;
    }

    /**
     * This method gets a RemoteViews object at a position.
     *
     * @param position An int representing the position of the view.
     * @return A RemoteViews object representing the specified view.
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (recipes == null) return null;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_widget_recipe);
        views.setTextViewText(R.id.text_view_widget_title, recipes[position].getName());

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(RECIPE_POSITION, position);
        views.setOnClickFillInIntent(R.id.text_view_widget_title, fillInIntent);

        return views;
    }

    /**
     * Unused method.
     *
     * @return null
     */
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    /**
     * This method is set so the widget knows how to treat views. All views are currently set
     * to be treated the same.
     *
     * @return An int representing the number of types of views in this adapter.
     */
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    /**
     * This method gets a view's id. This method currently returns a position.
     *
     * @param position An int representing the position of the view.
     * @return An int representing the position of the view.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
