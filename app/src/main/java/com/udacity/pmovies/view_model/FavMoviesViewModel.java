package com.udacity.pmovies.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.udacity.pmovies.database_model.FavMovie;
import com.udacity.pmovies.repositories.FavMoviesRepository;

import java.util.List;

public class FavMoviesViewModel extends AndroidViewModel {

    private FavMoviesRepository mRepository;

    private LiveData<List<FavMovie>> mAllFavMovies;

    public FavMoviesViewModel (Application application) {
        super(application);
        mRepository = new FavMoviesRepository(application);
        mAllFavMovies = mRepository.getAllFavMovies();
    }

    public LiveData<List<FavMovie>> getAllFavMovies() { return mAllFavMovies; }

    public void insert(FavMovie movie) { mRepository.insert(movie); }

    public void delete(FavMovie movie) { mRepository.delete(movie); }
}
