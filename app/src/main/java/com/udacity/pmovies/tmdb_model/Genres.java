package com.udacity.pmovies.tmdb_model;

import com.google.gson.annotations.SerializedName;

public class Genres {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public Genres(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
