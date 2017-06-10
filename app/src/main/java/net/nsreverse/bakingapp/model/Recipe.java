package net.nsreverse.bakingapp.model;

import android.graphics.Bitmap;

/**
 * This is the basic data model for a Recipe.
 *
 * @author Robert
 * Created on 6/7/2017.
 */
public class Recipe {
    private Bitmap recipeThumbnail;
    private String mName;
    private Ingredient[] mIngredients;
    private InstructionStep[] steps;
    private int mIngredientCount;
    private int mStepCount;
    private int mServings;

    public Recipe() {
        mName = "";
        mIngredientCount = 0;
        mStepCount = 0;
        mServings = 0;
        mIngredients = new Ingredient[0];
        steps = new InstructionStep[0];
    }

    public void setRecipeThumbnail(Bitmap recipeThumbnail) {
        this.recipeThumbnail = recipeThumbnail;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.mIngredients = ingredients;
    }

    public void setSteps(InstructionStep[] steps) {
        this.steps = steps;
    }

    public void setIngredientCount(int ingredientCount) {
        this.mIngredientCount = ingredientCount;
    }

    public void setStepCount(int stepCount) {
        this.mStepCount = stepCount;
    }

    public void setServings(int servings) {
        this.mServings = servings;
    }

    public Bitmap getRecipeThumbnail() {
        return recipeThumbnail;
    }

    public String getName() {
        return mName;
    }

    public Ingredient[] getIngredients() {
        return mIngredients;
    }

    public InstructionStep[] getSteps() {
        return steps;
    }

    public int getIngredientCount() {
        return mIngredientCount;
    }

    public int getStepCount() {
        return mStepCount;
    }

    public int getServings() {
        return mServings;
    }
}
