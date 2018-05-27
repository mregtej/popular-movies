package com.udacity.pmovies.database_model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.udacity.pmovies.application.PMoviesExecutors;

import java.util.List;

@Database(entities = {FavMovie.class}, version = 2, exportSchema = false)
public abstract class FavoriteMoviesRoomDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "favorite-movies-db";

    public abstract FavoriteMoviesDao favoriteMoviesDao();

    private static FavoriteMoviesRoomDatabase INSTANCE;

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static FavoriteMoviesRoomDatabase getInstance(final Context context,
                                                         final PMoviesExecutors executors) {
        if (INSTANCE == null) {
            synchronized (FavoriteMoviesRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = buildDatabase(context.getApplicationContext(), executors);
                    INSTANCE.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static FavoriteMoviesRoomDatabase buildDatabase(final Context appContext,
                                             final PMoviesExecutors executors) {
        return Room.databaseBuilder(appContext, FavoriteMoviesRoomDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(() -> {
                            // Add a delay to simulate a long-running operation
                            addDelay();
                            // Generate the data for pre-population
                            FavoriteMoviesRoomDatabase database = FavoriteMoviesRoomDatabase.
                                    getInstance(appContext, executors);
                            // notify that the database was created and it's ready to be used
                            database.setDatabaseCreated();
                        });
                    }
                }).build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    private static void insertData(final FavoriteMoviesRoomDatabase database,
                                   final List<FavMovie> favoriteMovies) {
        database.runInTransaction(() -> {
            database.favoriteMoviesDao().insertAll(favoriteMovies);
        });
    }

    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}
