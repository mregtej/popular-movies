package com.udacity.pmovies.database_model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.udacity.pmovies.provider.FavoriteMoviesContract;

import java.util.List;

@Dao
public interface FavoriteMoviesDao {

    @Query("SELECT COUNT(*) FROM " + FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME)
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(FavFilm favMovie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<FavFilm> products);

    @Query("SELECT * FROM " + FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME + " WHERE " +
            FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ID + " = :id")
    Cursor selectById(long id);

    @Query("SELECT * FROM " + FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME + " ORDER BY id ASC")
    LiveData<List<FavFilm>> getFavoriteMovies();

    @Query("SELECT * FROM " + FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME + " ORDER BY id ASC")
    Cursor getFavoriteMoviesViaCP();

    @Delete
    void delete(FavFilm favMovie);

    @Query("DELETE FROM " + FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME + " WHERE " +
            FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ID + " = :id")
    int deleteByIdViaCP(long id);

    @Query("DELETE FROM " + FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME)
    void deleteAll();

    @Update
    int update(FavFilm favMovie);
}
