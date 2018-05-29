package com.udacity.pmovies.database_model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavoriteMoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavFilm favMovie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<FavFilm> products);

    @Delete
    void delete(FavFilm favMovie);

    @Query("DELETE FROM fav_movies_table")
    void deleteAll();

    @Query("SELECT * from fav_movies_table ORDER BY id ASC")
    LiveData<List<FavFilm>> getFavoriteMovies();
}
