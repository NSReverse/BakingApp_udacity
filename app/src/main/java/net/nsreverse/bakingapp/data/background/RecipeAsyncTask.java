package net.nsreverse.bakingapp.data.background;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import net.nsreverse.bakingapp.data.utils.ApplicationInstance;
import net.nsreverse.bakingapp.data.utils.JsonParser;
import net.nsreverse.bakingapp.data.utils.NetworkUtils;
import net.nsreverse.bakingapp.model.Recipe;

import java.io.IOException;

/**
 * This AsyncTask runs a background thread to download and parse JSON based on recipes from an
 * API.
 *
 * @author Robert
 * Created on 6/7/2017.
 */
public class RecipeAsyncTask extends AsyncTask<Void, Void, Recipe[]> {

    private static final String TAG = RecipeAsyncTask.class.getSimpleName();

    private Delegate delegate;
    private Context context;

    /**
     * The results of this class are sent to objects that implement this interface.
     */
    public interface Delegate {
        void taskFinishedWithRecipes(Recipe[] recipes);
        void taskFinishedWithErrorMessage(String message);
        void jsonRecieved(String json);
    }

    /**
     * Basic constructor for creating a new RecipeAsyncTask.
     *
     * @param context A Context that implements the Delegate interface for this class.
     */
    public RecipeAsyncTask(Context context) {
        delegate = (Delegate)context;
        this.context = context;
    }

    /**
     * Method that executes logic on a background thread.
     *
     * @param unused Unused varargs.
     * @return An array of Recipe objects parsed from the downloaded Recipe JSON.
     */
    @Override
    protected Recipe[] doInBackground(Void... unused) {
        try {
            String json = NetworkUtils.getRecipesJsonFromUrl();

            if (json != null) {
                delegate.jsonRecieved(json);
            }

            return JsonParser.recipesWithJson(context, json);
        }
        catch (IOException ex) {
            if (ApplicationInstance.loggingEnabled) Log.e(TAG, "Unable to download recipe JSON: " +
                    ex.getMessage());
            return null;
        }
    }

    /**
     * Method that executes logic on the main thread. It delivers the results to the
     * Delegate-implementing class.
     *
     * @param recipes An array of Recipe objects that represent the parsed JSON. Can be null.
     */
    @Override
    protected void onPostExecute(Recipe[] recipes) {
        if (delegate != null) {
            if (recipes != null) {
                delegate.taskFinishedWithRecipes(recipes);
            }
            else {
                if (ApplicationInstance.loggingEnabled) Log.e(TAG, "Unable to handle recipes.");
                delegate.taskFinishedWithRecipes(null);
                delegate.taskFinishedWithErrorMessage("Unable to download recipes.");
            }
        }
    }
}
