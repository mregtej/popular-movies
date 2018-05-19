package com.udacity.pmovies.database_model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.udacity.pmovies.tmdb_model.Film;

import java.util.ArrayList;

@Entity(tableName = "fav_movies_table")
public class FavMovie {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private int id;
    @NonNull
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "original_title")
    private String original_title;
    @ColumnInfo(name = "poster_path")
    private String poster_path;
    @ColumnInfo(name = "adult")
    private boolean adult;
    @ColumnInfo(name = "overview")
    private String overview;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    // @ColumnInfo(name = "genre_ids")
    //private ArrayList<Integer> genreIds;
    @ColumnInfo(name = "original_language")
    private String originalLanguage;
    @ColumnInfo(name = "backdrop_path")
    private String backdropPath;
    @ColumnInfo(name = "popularity")
    private Double popularity;
    @ColumnInfo(name = "vote_count")
    private Integer voteCount;
    @ColumnInfo(name = "video")
    private Boolean video;
    @ColumnInfo(name = "vote_average")
    private Double voteAverage;

    public FavMovie(@NonNull int id, @NonNull String title, String original_title,
                    String poster_path, boolean adult, String overview, String releaseDate,
                    String originalLanguage, String backdropPath, Double popularity,
                    Integer voteCount, Boolean video, Double voteAverage) {
        this.id = id;
        this.title = title;
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        // this.genreIds = genreIds;
        this.originalLanguage = originalLanguage;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
    }

    @NonNull
    public int getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public boolean getAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    // public ArrayList<Integer> getGenreIds() {
    //    return genreIds;
    // }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    // public void setGenreIds(ArrayList<Integer> genreIds) {
    //    this.genreIds = genreIds;
    // }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

}
