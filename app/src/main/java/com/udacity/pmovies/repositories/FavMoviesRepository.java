package com.udacity.pmovies.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.udacity.pmovies.database_model.FavMovie;
import com.udacity.pmovies.database_model.FavMoviesDao;
import com.udacity.pmovies.database_model.FavMoviesRoomDatabase;

import java.util.List;

public class FavMoviesRepository {

    private FavMoviesDao mFavMoviesDao;
    private LiveData<List<FavMovie>> mAllFavMovies;

    public FavMoviesRepository(Application application) {
        FavMoviesRoomDatabase db = FavMoviesRoomDatabase.getDatabase(application);
        mFavMoviesDao = db.favMoviesDao();
        mAllFavMovies = mFavMoviesDao.getAllFavMovies();
    }

    public LiveData<List<FavMovie>> getAllFavMovies() {
        return mAllFavMovies;
    }

    public void insert(FavMovie favMovie) {
        new insertAsyncTask(mFavMoviesDao).execute(favMovie);
    }

    public void delete(FavMovie favMovie) {
        new deleteAsyncTask(mFavMoviesDao).execute(favMovie);
    }


    private static class insertAsyncTask extends AsyncTask<FavMovie, Void, Void> {

        private FavMoviesDao mAsyncTaskDao;

        insertAsyncTask(FavMoviesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavMovie... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<FavMovie, Void, Void> {

        private FavMoviesDao mAsyncTaskDao;

        deleteAsyncTask(FavMoviesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavMovie... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

}
