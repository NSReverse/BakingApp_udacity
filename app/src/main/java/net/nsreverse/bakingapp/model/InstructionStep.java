package net.nsreverse.bakingapp.model;

/**
 * This is the basic data model for a Recipe's Instruction Step.
 *
 * @author Robert
 * Created on 6/7/2017.
 */
public class InstructionStep {
    private String mShortDescription;
    private String mDescription;
    private String mVideoAddress;
    private String mThumbAddress;

    public InstructionStep() {
        mShortDescription = "";
        mDescription = "";
        mVideoAddress = "";
        mThumbAddress = "";
    }

    public void setShortDescription(String shortDescription) {
        this.mShortDescription = shortDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public void setVideoURL(String videoAddress) {
        this.mVideoAddress = videoAddress;
    }

    public void setThumbURL(String thumbAddress) {
        this.mThumbAddress = thumbAddress;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getVideoURL() {
        return mVideoAddress;
    }

    public String getThumbURL() {
        return mThumbAddress;
    }
}
