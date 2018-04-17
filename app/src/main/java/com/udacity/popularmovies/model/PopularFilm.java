package com.udacity.popularmovies.model;

/**
 * Popular Film Model (Custom ArrayAdapter)
 */
public class PopularFilm {

    /** Film poster */
    private int filmPoster;
    /** Film title */
    private String filmTitle;

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

    public int getFilmPoster() {
        return filmPoster;
    }

    public void setFilmPoster(int filmPoster) {
        this.filmPoster = filmPoster;
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public void setFilmTitle(String filmTitle) {
        this.filmTitle = filmTitle;
    }
}
