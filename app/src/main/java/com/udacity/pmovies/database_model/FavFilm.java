package com.udacity.pmovies.database_model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.udacity.pmovies.provider.FavoriteMoviesContract;


@Entity(tableName = FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME)
public class FavFilm {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ID)
    private Integer id;
    @NonNull
    @ColumnInfo(name = FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE)
    private String title;
    @ColumnInfo(name = FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH)
    private String posterPath;
    @ColumnInfo(name = FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ADULT)
    private boolean adult;
    @ColumnInfo(name = FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW)
    private String overview;
    @ColumnInfo(name = FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE)
    private String releaseDate;
    @ColumnInfo(name = FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ORIGINAL_TITLE)
    private String originalTitle;
    @ColumnInfo(name = FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ORIGINAL_LANGUAGE)
    private String originalLanguage;
    @ColumnInfo(name = FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_BACKDROP_PATH)
    private String backdropPath;
    @ColumnInfo(name = FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POPULARITY)
    private Double popularity;
    @ColumnInfo(name = FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VOTE_COUNT)
    private Integer voteCount;
    @ColumnInfo(name = FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VIDEO)
    private Boolean video;
    @ColumnInfo(name = FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VOTE_AVERAGE)
    private Double voteAverage;
    @ColumnInfo(name = FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_GENRE_IDS)
    private String genreIds;

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

    public String getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(String genreIds) {
        this.genreIds = genreIds;
    }

    public FavFilm() { }

    public FavFilm(@NonNull Integer id, @NonNull String title, String posterPath, boolean adult,
                   String overview, String releaseDate, String originalTitle,
                   String originalLanguage, String backdropPath, Double popularity,
                   Integer voteCount, Boolean video, Double voteAverage, String genreIds) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
        this.genreIds = genreIds;
    }

    /**
     * Create a new {@link FavFilm} from the specified {@link ContentValues}.
     *
     * @param values A {@link ContentValues}
     * @return A newly created {@link FavFilm} instance.
     */
    public static FavFilm fromContentValues(ContentValues values) {
        final FavFilm favFilm = new FavFilm();
        if (values.containsKey(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ID)) {
            favFilm.id = values.getAsInteger(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ID);
        }
        if (values.containsKey(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE)) {
            favFilm.title = values.getAsString(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE);
        }
        if (values.containsKey(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH)) {
            favFilm.posterPath = values.getAsString(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH);
        }
        if (values.containsKey(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ADULT)) {
            favFilm.adult = values.getAsBoolean(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ADULT);
        }
        if (values.containsKey(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW)) {
            favFilm.overview = values.getAsString(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW);
        }
        if (values.containsKey(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE)) {
            favFilm.releaseDate = values.getAsString(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE);
        }
        if (values.containsKey(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ORIGINAL_TITLE)) {
            favFilm.originalTitle = values.getAsString(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ORIGINAL_TITLE);
        }
        if (values.containsKey(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ORIGINAL_LANGUAGE)) {
            favFilm.originalLanguage = values.getAsString(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ORIGINAL_LANGUAGE);
        }
        if (values.containsKey(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_BACKDROP_PATH)) {
            favFilm.backdropPath = values.getAsString(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_BACKDROP_PATH);
        }
        if (values.containsKey(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POPULARITY)) {
            favFilm.popularity = values.getAsDouble(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POPULARITY);
        }
        if (values.containsKey(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VOTE_COUNT)) {
            favFilm.voteCount = values.getAsInteger(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VOTE_COUNT);
        }
        if (values.containsKey(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VIDEO)) {
            favFilm.video = values.getAsBoolean(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VIDEO);
        }
        if (values.containsKey(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VOTE_AVERAGE)) {
            favFilm.voteAverage = values.getAsDouble(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VOTE_AVERAGE);
        }
        if (values.containsKey(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_GENRE_IDS)) {
            favFilm.genreIds = values.getAsString(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_GENRE_IDS);
        }
        return favFilm;
    }

}
