package com.udacity.pmovies.database_model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {FavMovie.class}, version = 1, exportSchema = false)
public abstract class FavMoviesRoomDatabase extends RoomDatabase {

    public abstract FavMoviesDao favMoviesDao();

    private static FavMoviesRoomDatabase INSTANCE;

    public static FavMoviesRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FavMoviesRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavMoviesRoomDatabase.class, "fav_movies_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
