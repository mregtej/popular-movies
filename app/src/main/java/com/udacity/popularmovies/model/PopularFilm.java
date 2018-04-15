package com.udacity.popularmovies.model;

/**
 * Popular Film Model (Custom ArrayAdapter)
 */
public class PopularFilm {

    /** Film poster */
    int filmPoster;
    /** Film title */
    String filmTitle;

    /**
     * Public constructor
     *
     * @param title Film title
     * @param image Film poster
     */
    public PopularFilm(String title, int image)
    {
        this.filmPoster = image;
        this.filmTitle = title;
    }
}
