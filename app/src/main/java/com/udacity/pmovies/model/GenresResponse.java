package com.udacity.pmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GenresResponse {

    @SerializedName("genres")
    private ArrayList<Genres> genres = new ArrayList<>();

    private static final GenresResponse INSTANCE = new GenresResponse();

    public static GenresResponse getInstance() {
        return INSTANCE;
    }

    private GenresResponse() { }

    public ArrayList<Genres> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genres> genres) {
        this.genres = genres;
    }
}
