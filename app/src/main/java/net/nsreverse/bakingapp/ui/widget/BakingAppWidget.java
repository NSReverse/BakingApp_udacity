package net.nsreverse.bakingapp.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import net.nsreverse.bakingapp.R;

/**
 * This is the main controller class for the Baking App companion widget.
 *
 * @author Robert
 * Created on 6/9/2017
 */
public class BakingAppWidget extends AppWidgetProvider {
    public static final String ACTION_INGREDIENTS = "net.nsreverse.bakingapp.INGREDIENTS";
    public static final String ACTION_RECIPES = "net.nsreverse.bakingapp.RECIPES";

    public static final String RECIPE_POSITION = "recipe_position";
    public static final String WIDGET_ID = "widget_id";

    /**
     * This method updates the widget with its widget id.
     *
     * @param context The application context.
     * @param appWidgetManager The app widget manager responsible for carrying out the update.
     * @param appWidgetId The id of the widget to update.
     * @param intent Extra data carried through an Intent.
     * @param showRecipes A boolean representing which view to show.
     */
    static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                                     int appWidgetId, Intent intent, boolean showRecipes) {

        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.baking_app_widget);

        if (showRecipes) {
            Intent listIntent = new Intent(context, ListWidgetService.class);
            listIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            views.setRemoteAdapter(R.id.list_view_recipes, listIntent);
            views.setEmptyView(R.id.list_view_recipes, R.id.appwidget_text);

            Intent ingredientsIntent = new Intent(context, BakingAppWidget.class);
            ingredientsIntent.setAction(ACTION_INGREDIENTS);
            ingredientsIntent.putExtra(WIDGET_ID, appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                    ingredientsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.list_view_recipes, pendingIntent);
        }
        else {
            appWidgetManager.updateAppWidget(appWidgetId, null);

            Intent listIntent = new Intent(context, ListDetailWidgetService.class);
            listIntent.putExtra(RECIPE_POSITION, intent.getIntExtra(RECIPE_POSITION, 0));
            views.setRemoteAdapter(R.id.list_view_recipes_detail, listIntent);
            views.setEmptyView(R.id.list_view_recipes_detail, R.id.appwidget_text);

            Intent recipeIntent = new Intent(context, BakingAppWidget.class);
            recipeIntent.setAction(ACTION_RECIPES);
            recipeIntent.putExtra(WIDGET_ID, appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, recipeIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.image_view_widget_back, pendingIntent);
        }

        views.setViewVisibility(R.id.list_view_recipes, showRecipes? View.VISIBLE : View.GONE);
        views.setViewVisibility(R.id.image_view_widget_back, showRecipes? View.GONE : View.VISIBLE);
        views.setViewVisibility(R.id.list_view_recipes_detail, showRecipes?
                View.GONE : View.VISIBLE);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * This is a superclass method that initiates a widget update.
     *
     * @param context The application context.
     * @param appWidgetManager An app widget manager to pass to the updating method.
     * @param appWidgetIds An int array of all of the available widget ids for this app.
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId, null, true);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    /**
     * This is a superclass method similar to onCreate(). This method fires when the first
     * widget is created.
     *
     * @param context The application context.
     */
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context);
    }

    /**
     * This is a superclass method similar to onDestroy(). This method fires when the last
     * widget is destroyed.
     *
     * @param context The application context.
     */
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context);
    }

    /**
     * This is a superclass method that handles widget interaction.
     *
     * @param context The application context.
     * @param intent An Intent containing event data.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(WIDGET_ID)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetId = intent.getIntExtra(WIDGET_ID, 0);

            boolean showRecipes;

            if (intent.getAction().equals(ACTION_INGREDIENTS)) {
                showRecipes = false;
            }
            else {
                showRecipes = true;
            }

            updateWidget(context, appWidgetManager, appWidgetId, intent, showRecipes);
        }

        super.onReceive(context, intent);
    }
}

