package net.nsreverse.bakingapp.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import net.nsreverse.bakingapp.R;
import net.nsreverse.bakingapp.data.utils.JsonParser;
import net.nsreverse.bakingapp.data.utils.RuntimeCache;
import net.nsreverse.bakingapp.model.Ingredient;
import net.nsreverse.bakingapp.model.Recipe;

import static net.nsreverse.bakingapp.ui.widget.BakingAppWidget.RECIPE_POSITION;

/**
 * This service populates detail data in a widget.
 *
 * @author Robert
 * Created on 6/9/2017.
 */
public class ListDetailWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListDetailRemoteViewsFactory(getApplicationContext(), intent);
    }
}

/**
 * Class used by ListDetailWidgetService to update the ListView containing ingredients.
 */
class ListDetailRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private Recipe[] recipes;
    private Ingredient[] ingredients;

    private int index;

    /**
     * Constructor for creating a new ListDetailRemoteViewsFactory.
     *
     * @param context The application context.
     * @param intent An intent containing extra configuration data.
     */
    ListDetailRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;

        index = intent.getIntExtra(RECIPE_POSITION, 0);
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

        if (recipes != null) {
            ingredients = recipes[index].getIngredients();
        }
    }

    /**
     * This method is called to free up unused resources when this is destroyed.
     */
    @Override
    public void onDestroy() {
        recipes = null;
        ingredients = null;
    }

    /**
     * This method gets the current size of the data source.
     *
     * @return An int representing the size of the data source.
     */
    @Override
    public int getCount() {
        if (ingredients != null) {
            return ingredients.length;
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
        if (ingredients == null) return null;

        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.item_widget_ingredient);

        views.setTextViewText(R.id.text_view_widget_detail_title,
                ingredients[position].getName());
        views.setTextViewText(R.id.text_view_widget_detail_subtitle,
                ingredients[position].getQuantity() + " " + ingredients[position].getMeasurement());

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
