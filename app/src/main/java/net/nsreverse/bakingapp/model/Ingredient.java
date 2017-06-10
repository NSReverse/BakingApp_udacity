package net.nsreverse.bakingapp.model;

/**
 * This is the basic data model for an Ingredient.
 *
 * @author Robert
 * Created on 6/7/2017.
 */
public class Ingredient {
    private String mName;
    private int mQuantity;
    private String mMeasurement;

    public Ingredient() {
        mName = "";
        mQuantity = 0;
        mMeasurement = "";
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setQuantity(int quantity) {
        this.mQuantity = quantity;
    }

    public void setMeasurement(String measurement) {
        this.mMeasurement = measurement;
    }

    public String getName() {
        return mName;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public String getMeasurement() {
        return mMeasurement;
    }
}
