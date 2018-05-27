package com.udacity.pmovies.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.udacity.pmovies.application.PMoviesApp;
import com.udacity.pmovies.repositories.TMDBRepository;
import com.udacity.pmovies.rest.TMDBApiInterface;
import com.udacity.pmovies.tmdb_model.Film;
import com.udacity.pmovies.tmdb_model.GenresResponse;
import com.udacity.pmovies.tmdb_model.Images;
import com.udacity.pmovies.tmdb_model.Review;
import com.udacity.pmovies.tmdb_model.Video;

import java.util.List;

public class TMDBViewModel extends AndroidViewModel {

    private final TMDBRepository mTmdbRepository;

    private final String mApiKey;

    public TMDBViewModel(Application application, TMDBRepository repository,
                         @NonNull TMDBApiInterface apiClient, String api_key) {
        super(application);
        this.mTmdbRepository = repository;
        this.mTmdbRepository.setmTMDBApiClient(apiClient);
        this.mApiKey = api_key;
    }

    public LiveData<List<Film>> getMostPopularMovies() {
        return mTmdbRepository.getMostPopularMovies(mApiKey);
    }

    public LiveData<List<Film>> getTopRatedMovies() {
        return mTmdbRepository.getTopRatedMovies(mApiKey);
    }

    public LiveData<Images> getAPIConfiguration() {
        return mTmdbRepository.getAPIConfiguration(mApiKey);
    }

    public LiveData<List<Video>> getMovieTrailers(final int film_id) {
        return mTmdbRepository.getMovieTrailers(film_id, mApiKey);
    }

    public LiveData<List<Review>> getMovieReviews(final int film_id) {
        return mTmdbRepository.getMovieReviews(film_id, mApiKey);
    }

    public LiveData<GenresResponse> getGenres() {
        return mTmdbRepository.getGenres(mApiKey);
    }

    /**
     * A creator is used to inject the product ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the product ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private final TMDBRepository mRepository;
        private final TMDBApiInterface mApiClient;
        private final String mApiKey;

        public Factory(@NonNull Application application, @NonNull TMDBApiInterface apiClient,
                       @NonNull String api_key) {
            mApplication = application;
            mRepository = ((PMoviesApp) application).getRepository();
            mApiClient = apiClient;
            mApiKey = api_key;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new TMDBViewModel(mApplication, mRepository, mApiClient, mApiKey);
        }
    }
}
