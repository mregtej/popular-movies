package com.udacity.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Images {
    @SerializedName("base_url")
    private String baseUrl;
    @SerializedName("secure_base_url")
    private String secureBaseUrl;
    @SerializedName("backdrop_sizes")
    private ArrayList<String> backdropSizes;
    @SerializedName("logo_sizes")
    private ArrayList<String> logoSizes;
    @SerializedName("poster_sizes")
    private ArrayList<String> posterSizes;
    @SerializedName("profile_sizes")
    private ArrayList<String> profileSizes;
    @SerializedName("still_sizes")
    private ArrayList<String> stillSizes;

    public Images(String baseUrl, String secureBaseUrl, ArrayList<String> backdropSizes,
                  ArrayList<String> logoSizes, ArrayList<String> posterSizes,
                  ArrayList<String> profileSizes, ArrayList<String> stillSizes) {
        this.baseUrl = baseUrl;
        this.secureBaseUrl = secureBaseUrl;
        this.backdropSizes = backdropSizes;
        this.logoSizes = logoSizes;
        this.posterSizes = posterSizes;
        this.profileSizes = profileSizes;
        this.stillSizes = stillSizes;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getSecureBaseUrl() {
        return secureBaseUrl;
    }

    public void setSecureBaseUrl(String secureBaseUrl) {
        this.secureBaseUrl = secureBaseUrl;
    }

    public ArrayList<String> getBackdropSizes() {
        return backdropSizes;
    }

    public void setBackdropSizes(ArrayList<String> backdropSizes) {
        this.backdropSizes = backdropSizes;
    }

    public ArrayList<String> getLogoSizes() {
        return logoSizes;
    }

    public void setLogoSizes(ArrayList<String> logoSizes) {
        this.logoSizes = logoSizes;
    }

    public ArrayList<String> getPosterSizes() {
        return posterSizes;
    }

    public void setPosterSizes(ArrayList<String> posterSizes) {
        this.posterSizes = posterSizes;
    }

    public ArrayList<String> getProfileSizes() {
        return profileSizes;
    }

    public void setProfileSizes(ArrayList<String> profileSizes) {
        this.profileSizes = profileSizes;
    }

    public ArrayList<String> getStillSizes() {
        return stillSizes;
    }

    public void setStillSizes(ArrayList<String> stillSizes) {
        this.stillSizes = stillSizes;
    }
}
