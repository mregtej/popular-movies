package com.udacity.pmovies.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.udacity.pmovies.application.PMoviesApp;
import com.udacity.pmovies.database_model.FavMovie;
import com.udacity.pmovies.repositories.TMDBRepository;

import java.util.List;

public class FavoriteMoviesViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<FavMovie>> mObservableFavoriteMovies;

    private final TMDBRepository mTmdbRepository;

    public FavoriteMoviesViewModel(Application application, TMDBRepository repository) {
        super(application);

        mObservableFavoriteMovies = new MediatorLiveData<>();
        mObservableFavoriteMovies.setValue(null);

        this.mTmdbRepository = repository;

        LiveData<List<FavMovie>> favMovies = mTmdbRepository.getAllFavMovies();

        // observe the changes of the products from the database and forward them
        mObservableFavoriteMovies.addSource(favMovies, mObservableFavoriteMovies::setValue);
    }

    public LiveData<List<FavMovie>> getAllFavMovies() { return mObservableFavoriteMovies; }

    public void insert(FavMovie movie) { mTmdbRepository.insert(movie); }

    public void delete(FavMovie movie) { mTmdbRepository.delete(movie); }

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

        public Factory(@NonNull Application application) {
            mApplication = application;
            mRepository = ((PMoviesApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new FavoriteMoviesViewModel(mApplication, mRepository);
        }
    }


}
