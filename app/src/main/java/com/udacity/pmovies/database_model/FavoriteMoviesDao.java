package com.udacity.pmovies.database_model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.udacity.pmovies.tmdb_model.Film;

import java.util.List;


@Dao
public interface FavoriteMoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Film favMovie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Film> products);

    @Delete
    void delete(Film favMovie);

    @Query("DELETE FROM FAV_MOVIES_TABLE")
    void deleteAll();

    @Query("SELECT * from FAV_MOVIES_TABLE ORDER BY id ASC")
    LiveData<List<Film>> getFavoriteMovies();
}
