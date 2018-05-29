package com.udacity.pmovies.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.udacity.pmovies.application.PMoviesExecutors;
import com.udacity.pmovies.database_model.FavFilm;
import com.udacity.pmovies.database_model.FavoriteMoviesRoomDatabase;
import com.udacity.pmovies.rest.TMDBApiInterface;
import com.udacity.pmovies.tmdb_model.APIConfigurationResponse;
import com.udacity.pmovies.tmdb_model.TMDBFilm;
import com.udacity.pmovies.tmdb_model.FilmResponse;
import com.udacity.pmovies.tmdb_model.GenresResponse;
import com.udacity.pmovies.tmdb_model.Images;
import com.udacity.pmovies.tmdb_model.Review;
import com.udacity.pmovies.tmdb_model.ReviewsResponse;
import com.udacity.pmovies.tmdb_model.Video;
import com.udacity.pmovies.tmdb_model.VideosResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TMDBRepository {

    //--------------------------------------------------------------------------------|
    //                                Constants                                       |
    //--------------------------------------------------------------------------------|

    private static final String TAG = TMDBRepository.class.getName();


    //--------------------------------------------------------------------------------|
    //                                  Params                                        |
    //--------------------------------------------------------------------------------|

    private static TMDBRepository INSTANCE;

    private TMDBApiInterface mTMDBApiClient;

    private final FavoriteMoviesRoomDatabase mFavoriteMoviesDB;
    private MediatorLiveData<List<FavFilm>> mObservableFavoriteMovies;
    private final PMoviesExecutors mExecutors;

    //--------------------------------------------------------------------------------|
    //                  Constructor (Singleton Pattern)                               |
    //--------------------------------------------------------------------------------|

    private TMDBRepository(final FavoriteMoviesRoomDatabase database,
                           final PMoviesExecutors executors) {
        mFavoriteMoviesDB = database;
        mObservableFavoriteMovies = new MediatorLiveData<>();
        mExecutors = executors;

        mObservableFavoriteMovies.addSource(mFavoriteMoviesDB.favoriteMoviesDao().getFavoriteMovies(),
                productEntities -> {
                    if (mFavoriteMoviesDB.getDatabaseCreated().getValue() != null) {
                        mObservableFavoriteMovies.postValue(productEntities);
                    }
                });
    }

    public static TMDBRepository getInstance(final FavoriteMoviesRoomDatabase database,
                                             final PMoviesExecutors executors) {
        if (INSTANCE == null) {
            synchronized (TMDBRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TMDBRepository(database,executors);
                }
            }
        }
        return INSTANCE;
    }


    //--------------------------------------------------------------------------------|
    //                               Setters                                          |
    //--------------------------------------------------------------------------------|

    public void setmTMDBApiClient(TMDBApiInterface mTMDBApiClient) {
        this.mTMDBApiClient = mTMDBApiClient;
    }


    //--------------------------------------------------------------------------------|
    //                               Local DB Ops                                     |
    //--------------------------------------------------------------------------------|

    public LiveData<List<FavFilm>> getAllFavMovies() {
        return mObservableFavoriteMovies;
    }

    public void insert(final FavFilm favMovie) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mFavoriteMoviesDB.favoriteMoviesDao().insert(favMovie);
            }
        });

    }

    public void delete(final FavFilm favMovie) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mFavoriteMoviesDB.favoriteMoviesDao().delete(favMovie);
            }
        });
    }

    //--------------------------------------------------------------------------------|
    //                               TMDB API Request Methods                         |
    //--------------------------------------------------------------------------------|

    /**
     * get /movie/popular
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     */
    public LiveData<List<TMDBFilm>> getMostPopularMovies(@NonNull final String api_key) {
        final MutableLiveData<List<TMDBFilm>> data = new MutableLiveData<>();
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mTMDBApiClient.getMostPopularMovies(api_key).enqueue(new Callback<FilmResponse>() {
                    @Override
                    public void onResponse(Call<FilmResponse> call, Response<FilmResponse> response) {
                        if(response != null) {
                            ArrayList<TMDBFilm> TMDBFilms = response.body().getResults();
                            Log.d(TAG, "TMDB - Requested most popular movies. " +
                                    "Number of movies received: " + TMDBFilms.size());
                            data.setValue(TMDBFilms);
                        } else {
                            data.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<FilmResponse> call, Throwable t) {
                        Log.e(TAG, t.toString());
                        data.setValue(null);
                    }
                });
            }
        });
        return data;
    }

    /**
     * get /movie/top_rated
     * https://developers.themoviedb.org/3/movies/get-top-rated-movies
     */
    public LiveData<List<TMDBFilm>> getTopRatedMovies(@NonNull final String api_key) {
        final MutableLiveData<List<TMDBFilm>> data = new MutableLiveData<>();
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mTMDBApiClient.getTopRatedMovies(api_key).enqueue(new Callback<FilmResponse>() {
                    @Override
                    public void onResponse(Call<FilmResponse> call, Response<FilmResponse> response) {
                        if(response != null) {
                            ArrayList<TMDBFilm> TMDBFilms = response.body().getResults();
                            Log.d(TAG, "TMDB - Requested top rated movies. " +
                                    "Number of movies received: " + TMDBFilms.size());
                            data.setValue(TMDBFilms);
                        } else {
                            data.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<FilmResponse> call, Throwable t) {
                        Log.e(TAG, t.toString());
                        data.setValue(null);
                    }
                });
            }
        });
        return data;
    }

    /**
     * get /configuration
     * https://developers.themoviedb.org/3/configuration/get-api-configuration
     */
    public LiveData<Images> getAPIConfiguration(@NonNull final String api_key) {
        final MutableLiveData<Images> data = new MutableLiveData<>();
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mTMDBApiClient.getConfiguration(api_key).enqueue(new Callback<APIConfigurationResponse>() {
                    @Override
                    public void onResponse(Call<APIConfigurationResponse> call,
                                           Response<APIConfigurationResponse> response) {
                        if(response != null) {
                            Images images = Images.getInstance();
                            images.setImageFields(response.body().getImages());
                            Log.d(TAG, "TMDB API Config received");
                            data.setValue(images);
                        } else {
                            data.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<APIConfigurationResponse> call, Throwable t) {
                        Log.e(TAG, t.toString());
                        data.setValue(null);
                    }
                });
            }
        });
        return data;
    }

    /**
     * get /genre/movie/list
     * https://developers.themoviedb.org/3/genres/get-movie-list
     */
    public LiveData<GenresResponse> getGenres(@NonNull final String api_key) {
        final MutableLiveData<GenresResponse> data = new MutableLiveData<>();
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mTMDBApiClient.getGenres(api_key).enqueue(new Callback<GenresResponse>() {
                    @Override
                    public void onResponse(Call<GenresResponse> call,
                                           Response<GenresResponse> response) {
                        if(response != null) {
                            GenresResponse genres = GenresResponse.getInstance();
                            genres.setGenres(response.body().getGenres());
                            Log.d(TAG, "TMDB Genres received");
                            data.setValue(genres);
                        } else {
                            data.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<GenresResponse> call, Throwable t) {
                        Log.e(TAG, t.toString());
                        data.setValue(null);
                    }
                });
            }
        });
        return data;
    }

    /**
     * get /movie/{id}/videos
     * https://developers.themoviedb.org/3/movies/get-movie-videos
     */
    public LiveData<List<Video>> getMovieTrailers(@NonNull final int film_id,
                                                   @NonNull final String api_key) {
        final MutableLiveData<List<Video>> data = new MutableLiveData<>();
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mTMDBApiClient.getMovieTrailers(film_id, api_key).enqueue(new Callback<VideosResponse>() {
                    @Override
                    public void onResponse(Call<VideosResponse> call,
                                           Response<VideosResponse> response) {
                        if(response != null) {
                            List<Video> trailers = response.body().getResults();
                            Log.d(TAG, "TMDB - Requested trailers for film_id: " + film_id +
                                    ". Number of trailers received: " + trailers.size());
                            data.setValue(trailers);
                        } else {
                            data.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<VideosResponse> call, Throwable t) {
                        Log.e(TAG, t.toString());
                        data.setValue(null);
                    }
                });
            }
        });
        return data;
    }

    /**
     * get /movie/{id}/reviews
     * https://developers.themoviedb.org/3/movies/get-movie-reviews
     */
    public LiveData<List<Review>> getMovieReviews(@NonNull final int film_id,
                                                   @NonNull final String api_key) {
        final MutableLiveData<List<Review>> data = new MutableLiveData<>();
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mTMDBApiClient.getMovieReviews(film_id, api_key).enqueue(new Callback<ReviewsResponse>() {
                    @Override
                    public void onResponse(Call<ReviewsResponse> call,
                                           Response<ReviewsResponse> response) {
                        if(response != null) {
                            List<Review> reviews = response.body().getResults();
                            Log.d(TAG, "TMDB - Requested reviews for film_id: " + film_id +
                                    ". Number of reviews received: " + reviews.size());
                            data.setValue(reviews);
                        } else {
                            data.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                        Log.e(TAG, t.toString());
                        data.setValue(null);
                    }
                });
            }
        });
        return data;
    }

}
