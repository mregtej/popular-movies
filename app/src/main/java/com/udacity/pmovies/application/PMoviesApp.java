package com.udacity.pmovies.application;

import android.app.Application;

import com.udacity.pmovies.comms.ConnectivityHandler;
import com.udacity.pmovies.database_model.FavoriteMoviesRoomDatabase;
import com.udacity.pmovies.repositories.TMDBRepository;

public class PMoviesApp extends Application {

    private static PMoviesApp mInstance;
    private PMoviesExecutors mPMoviesExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        mPMoviesExecutors = new PMoviesExecutors();
        mInstance = this;
    }

    public static synchronized PMoviesApp getInstance() {
        return mInstance;
    }

    public FavoriteMoviesRoomDatabase getDatabase() {
        return FavoriteMoviesRoomDatabase.getInstance(this, mPMoviesExecutors);
    }

    public TMDBRepository getRepository() {
        return TMDBRepository.getInstance(getDatabase(), mPMoviesExecutors);
    }

    public void setConnectivityListener(ConnectivityHandler.ConnectivityHandlerListener listener) {
        ConnectivityHandler.connectivityHandlerListener = listener;
    }
}
