package com.udacity.pmovies.database_model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavMoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavMovie favMovie);

    @Delete
    void delete(FavMovie favMovie);

    @Query("DELETE FROM FAV_MOVIES_TABLE")
    void deleteAll();

    @Query("SELECT * from FAV_MOVIES_TABLE ORDER BY id ASC")
    LiveData<List<FavMovie>> getAllFavMovies();
}
