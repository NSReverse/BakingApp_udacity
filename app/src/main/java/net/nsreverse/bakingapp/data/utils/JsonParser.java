package net.nsreverse.bakingapp.data.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;

import net.nsreverse.bakingapp.R;
import net.nsreverse.bakingapp.model.Ingredient;
import net.nsreverse.bakingapp.model.InstructionStep;
import net.nsreverse.bakingapp.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * This class parses downloaded JSON.
 *
 * @author Robert
 * Created on 6/7/2017.
 */
public class JsonParser {
    private static final String TAG = JsonParser.class.getSimpleName();

    /**
     * This method parses JSON into an array of Recipe objects.
     *
     * @param context A Context used to access application assets.
     * @param json A downloaded JSON string.
     * @return An array of Recipe objects.
     */
    @Nullable
    public static Recipe[] recipesWithJson(Context context, String json) {
        JSONArray baseArray;

        try {
            baseArray = new JSONArray(json);
        }
        catch (JSONException ex) {
            if (ApplicationInstance.loggingEnabled) Log.e(TAG, "JSON is null: " + ex.getMessage());
            return null;
        }
        catch (NullPointerException ex) {
            return null;
        }

        Recipe[] recipes = new Recipe[baseArray.length()];

        for (int i = 0; i < recipes.length; i++) {
            try {
                JSONObject currentObject = baseArray.getJSONObject(i);
                JSONArray currentObjectIngredients = currentObject.getJSONArray("ingredients");
                JSONArray currentObjectSteps = currentObject.getJSONArray("steps");

                String name = currentObject.getString("name");
                int ingredientCount = currentObjectIngredients.length();
                int stepCount = currentObjectSteps.length();
                int servings = currentObject.getInt("servings");

                Recipe currentRecipe = new Recipe();

                if (name != null) {
                    currentRecipe.setName(name);
                }
                else {
                    if (ApplicationInstance.loggingEnabled) Log.d(TAG, "Unknown Recipe");
                    currentRecipe.setName(context.getString(R.string.unknown_recipe_name));
                }

                currentRecipe.setIngredientCount(ingredientCount);
                currentRecipe.setStepCount(stepCount);
                currentRecipe.setServings(servings);
                currentRecipe.setIngredients(parseIngredientJsonArray(currentObjectIngredients));
                currentRecipe.setSteps(parseInstructionsJsonArray(currentObjectSteps));

                try {
                    String imageLocation = currentObject.getString("image");
                    Bitmap thumbnail = NetworkUtils.getImageFromAddress(imageLocation);
                    currentRecipe.setRecipeThumbnail(thumbnail);
                }
                catch (IOException ex) {
                    currentRecipe.setRecipeThumbnail(null);
                }

                recipes[i] = currentRecipe;
            }
            catch (JSONException ex) {
                if (ApplicationInstance.loggingEnabled) Log.e(TAG, "Result failed to parse: " +
                                                                ex.getMessage());
                recipes[i] = null;
            }
        }

        if (recipes.length > 0) {
            return recipes;
        }

        return null;
    }

    /**
     * This method is only used by the widget to parse a data source that does not require
     * background threading.
     *
     * @param context The context to get resources.
     * @param json The Recipe JSON data source.
     * @return An array of Recipe objects.
     */
    public static Recipe[] recipesWithJsonNoImage(Context context, String json) {
        JSONArray baseArray;

        try {
            baseArray = new JSONArray(json);
        }
        catch (JSONException ex) {
            if (ApplicationInstance.loggingEnabled) Log.e(TAG, "JSON is null: " + ex.getMessage());
            return null;
        }
        catch (NullPointerException ex) {
            return null;
        }

        Recipe[] recipes = new Recipe[baseArray.length()];

        for (int i = 0; i < recipes.length; i++) {
            try {
                JSONObject currentObject = baseArray.getJSONObject(i);
                JSONArray currentObjectIngredients = currentObject.getJSONArray("ingredients");
                JSONArray currentObjectSteps = currentObject.getJSONArray("steps");

                String name = currentObject.getString("name");
                int ingredientCount = currentObjectIngredients.length();
                int stepCount = currentObjectSteps.length();
                int servings = currentObject.getInt("servings");

                Recipe currentRecipe = new Recipe();

                if (name != null) {
                    currentRecipe.setName(name);
                }
                else {
                    if (ApplicationInstance.loggingEnabled) Log.d(TAG, "Unknown Recipe");
                    currentRecipe.setName(context.getString(R.string.unknown_recipe_name));
                }

                currentRecipe.setIngredientCount(ingredientCount);
                currentRecipe.setStepCount(stepCount);
                currentRecipe.setServings(servings);
                currentRecipe.setIngredients(parseIngredientJsonArray(currentObjectIngredients));
                currentRecipe.setSteps(parseInstructionsJsonArray(currentObjectSteps));
                currentRecipe.setRecipeThumbnail(null);

                recipes[i] = currentRecipe;
            }
            catch (JSONException ex) {
                if (ApplicationInstance.loggingEnabled) Log.e(TAG, "Result failed to parse: " +
                        ex.getMessage());
                recipes[i] = null;
            }
        }

        if (recipes.length > 0) {
            return recipes;
        }

        return null;
    }

    /**
     * This method converts a JSONArray object into an array of Ingredient objects.
     *
     * @param ingredients A JSONArray representing ingredients.
     * @return An Ingredient array representing ingredients.
     */
    private static Ingredient[] parseIngredientJsonArray(JSONArray ingredients) {
        if (ingredients != null && ingredients.length() > 0) {
            Ingredient[] ingredientArray = new Ingredient[ingredients.length()];

            for (int i = 0; i < ingredients.length(); i++) {
                try {
                    JSONObject currentIngredientObject = ingredients.getJSONObject(i);

                    String name = currentIngredientObject.getString("ingredient");
                    String measurement = currentIngredientObject.getString("measure");
                    int quantity = currentIngredientObject.getInt("quantity");

                    Ingredient currentIngredient = new Ingredient();
                    currentIngredient.setName(name);
                    currentIngredient.setMeasurement(measurement);
                    currentIngredient.setQuantity(quantity);

                    ingredientArray[i] = currentIngredient;
                }
                catch (JSONException ex) {
                    if (ApplicationInstance.loggingEnabled) Log.e(TAG,
                            "Ingredient failed to parse: " + ex.getMessage());
                    ingredientArray[i] = null;
                }
            }

            return ingredientArray;
        }

        return new Ingredient[0];
    }

    /**
     * This method converts a JSONArray into an array of InstructionStep objects.
     *
     * @param instructions A JSONArray representing instructions.
     * @return An Ingredient array representing instructions.
     */
    private static InstructionStep[] parseInstructionsJsonArray(JSONArray instructions) {
        if (instructions != null && instructions.length() > 0) {
            InstructionStep[] stepArray = new InstructionStep[instructions.length()];

            for (int i = 0; i < instructions.length(); i++) {
                try {
                    JSONObject currentStepObject = instructions.getJSONObject(i);

                    String shortDescription = currentStepObject.getString("shortDescription");
                    String description = currentStepObject.getString("description");
                    String videoAddress = currentStepObject.getString("videoURL");
                    String thumbAddress = currentStepObject.getString("thumbnailURL");

                    InstructionStep currentStep = new InstructionStep();
                    currentStep.setShortDescription(shortDescription);
                    currentStep.setDescription(description);
                    currentStep.setVideoURL(videoAddress);
                    currentStep.setThumbURL(thumbAddress);

                    stepArray[i] = currentStep;
                }
                catch (JSONException ex) {
                    if (ApplicationInstance.loggingEnabled) Log.e(TAG,
                            "Instruction failed to parse: " + ex.getMessage());
                    stepArray[i] = null;
                }
            }

            return stepArray;
        }

        return new InstructionStep[0];
    }
}
