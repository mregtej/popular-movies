package com.udacity.pmovies.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMoviesContract {

    /* Add content provider constants to the Contract
     Clients need to know how to access the FavFilm data, and it's your job to provide
     these content URI's for the path to that data:
        1) Content authority,
        2) Base content URI,
        3) Path(s) to the tasks directory
        4) Content URI for data in the FavoriteMoviesEntry class
      */

    /** The authority of this content provider. */
    public static final String AUTHORITY = "com.udacity.pmovies.provider";

    /** The Base Content URI */
    public static final Uri BASE_CONTENT_URI = Uri.parse(
            "content://" + AUTHORITY);

    /** The Path for the FavFilm directory */
    public static final String FAV_FILM_PATH = FavoriteMoviesEntry.TABLE_NAME;

    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class FavoriteMoviesEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(FAV_FILM_PATH).build();

        /**
         * The name of the Cheese table.
         */
        public static final String TABLE_NAME = "fav_movies_table";

        /**
         * The name of the Film ID column.
         */
        public static final String COLUMN_ID = "id";
        /**
         * The name of the Film title column.
         */
        public static final String COLUMN_TITLE = "title";
        /**
         * The name of the Film poster path column.
         */
        public static final String COLUMN_POSTER_PATH = "poster_path";
        /**
         * The name of the Film adult column.
         */
        public static final String COLUMN_ADULT = "adult";
        /**
         * The name of the Film overview column.
         */
        public static final String COLUMN_OVERVIEW = "overview";
        /**
         * The name of the Film release date column.
         */
        public static final String COLUMN_RELEASE_DATE = "release_date";
        /**
         * The name of the Film original title column.
         */
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        /**
         * The name of the Film original language column.
         */
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        /**
         * The name of the Film backdrop path column.
         */
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        /**
         * The name of the Film popularity column.
         */
        public static final String COLUMN_POPULARITY = "popularity";
        /**
         * The name of the Film vote count column.
         */
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        /**
         * The name of the Film video column.
         */
        public static final String COLUMN_VIDEO = "video";
        /**
         * The name of the Film vote average column.
         */
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        /**
         * The name of the Film genre ids column.
         */
        public static final String COLUMN_GENRE_IDS = "genre_ids";

    }

}
