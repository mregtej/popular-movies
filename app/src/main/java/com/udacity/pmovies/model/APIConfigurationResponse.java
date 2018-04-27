package com.udacity.pmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class APIConfigurationResponse {
    @SerializedName("images")
    private Images images;
    @SerializedName("change_keys")
    private ArrayList<String> changeKeys;

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public ArrayList<String> getChangeKeys() {
        return changeKeys;
    }

    public void setChangeKeys(ArrayList<String> changeKeys) {
        this.changeKeys = changeKeys;
    }
}
