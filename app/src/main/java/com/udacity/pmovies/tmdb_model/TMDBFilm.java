package com.udacity.pmovies.tmdb_model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * TMDBFilm Model
 */
public class TMDBFilm implements Parcelable {

    @NonNull
    @SerializedName("id")
    private Integer id;
    @NonNull
    @SerializedName("title")
    private String title;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("popularity")
    private Double popularity;
    @SerializedName("vote_count")
    private Integer voteCount;
    @SerializedName("video")
    private Boolean video;
    @SerializedName("vote_average")
    private Double voteAverage;
    @SerializedName("genre_ids")
    private List<Integer> genreIds;

    /**
     * Constructor initialized from JSON parser
     *
     * @param posterPath        Path of film poster
     * @param adult             TMDBFilm adult (pornography) category
     * @param overview          TMDBFilm overview
     * @param releaseDate       TMDBFilm release date
     * @param genreIds          TMDBFilm genres
     * @param id                Unique film ID (DB)
     * @param originalTitle     Original film title
     * @param originalLanguage  Original film language
     * @param title             TMDBFilm title (EN)
     * @param backdropPath      Path of film backdrop-image
     * @param popularity        TMDBFilm popularity
     * @param voteCount         Number of film comments / ratings
     * @param video             TMDBFilm video flag
     * @param voteAverage       Average film rating
     */
    public TMDBFilm(String posterPath, boolean adult, String overview, String releaseDate,
                    int id, String originalTitle, String originalLanguage, String title,
                    String backdropPath, double popularity, int voteCount, boolean video,
                    double voteAverage, List<Integer> genreIds)
    {
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
        this.genreIds = genreIds;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    /**
     * Retrieves TMDBFilm data from Parcel object
     * This method is invoked by the method createFromParcel of object CREATOR
     * @param   in      TMDBFilm parcelable object
     */
    private TMDBFilm(Parcel in){
        this.posterPath = in.readString();
        this.adult = in.readInt() == 1;
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.id = in.readInt();
        this.originalTitle = in.readString();
        this.originalLanguage = in.readString();
        this.title = in.readString();
        this.backdropPath = in.readString();
        this.popularity = in.readDouble();
        this.voteCount = in.readInt();
        this.video = in.readInt() == 1;
        this.voteAverage = in.readDouble();
        this.genreIds = in.readArrayList(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<TMDBFilm> CREATOR = new Parcelable.Creator<TMDBFilm>() {
        @Override
        public TMDBFilm createFromParcel(Parcel source) {
            return new TMDBFilm(source);
        }

        @Override
        public TMDBFilm[] newArray(int size) {
            return new TMDBFilm[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.posterPath);
        dest.writeInt(this.adult ? 1 : 0);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeInt(this.id);
        dest.writeString(this.originalTitle);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.title);
        dest.writeString(this.backdropPath);
        dest.writeDouble(this.popularity);
        dest.writeInt(this.voteCount);
        dest.writeInt(this.video ? 1 : 0);
        dest.writeDouble(this.voteAverage);
        dest.writeList(this.genreIds);
    }

    private static int[] convertIntegers(ArrayList<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = integers.get(i);
        }
        return ret;
    }

    private static ArrayList<Integer> convertArrayListOfIntegers(int[] integers)
    {
        ArrayList<Integer> ret = new ArrayList<>();
        for (int i : integers) {
            ret.add(i);
        }
        return ret;
    }

}
