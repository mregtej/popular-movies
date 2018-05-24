package com.udacity.pmovies.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.pmovies.R;
import com.udacity.pmovies.database_model.FavMovie;
import com.udacity.pmovies.globals.GlobalsPopularMovies;
import com.udacity.pmovies.tmdb_model.Film;
import com.udacity.pmovies.tmdb_model.Review;
import com.udacity.pmovies.tmdb_model.ReviewsResponse;
import com.udacity.pmovies.tmdb_model.Video;
import com.udacity.pmovies.tmdb_model.VideosResponse;
import com.udacity.pmovies.rest.TMDBApiClient;
import com.udacity.pmovies.rest.TMDBApiInterface;
import com.udacity.pmovies.view_model.FavMoviesViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * PMovies DetailFilmActivity
 *
 * TODO Implement MVP pattern
 */
public class DetailFilmActivity extends FragmentActivity implements
        DetailFilmActivityDataFragment.OnFavoriteFilmItemClickListener {

    //--------------------------------------------------------------------------------|
    //                               Constants                                        |
    //--------------------------------------------------------------------------------|

    /** Class name - Log TAG */
    private static final String TAG = DetailFilmActivity.class.getName();
    /** Key for storing film Backdrop URL */
    private static final String BACKDROP_URL_KEY = "backdrop-url";
    /** Key for storing if film is in favorite movie list */
    private static final String IS_IN_FAVS_KEY = "is-in-favs";
    /** Key for retrieving Film parcelable object from Intent */
    private static final String FILM_EXTRA = "film";
    /** Key for identifying if film is in favorite movie list from Intent */
    private static final String IS_IN_FAVS_EXTRA = "is-in-favs";


    //--------------------------------------------------------------------------------|
    //                               Params                                           |
    //--------------------------------------------------------------------------------|

    /** Film Backdrop URL */
    private String filmBackDropURL;
    /** TMDB API client */
    private TMDBApiInterface apiService;
    // Add/Remove film to/from favs Toast
    private Toast mAddToFavsToast;
    /** Film object */
    private static Film mFilm;
    /** Flag for identifying if film is in favorite movie list */
    private boolean mIsFilmInFavs;
    /** FavMovies ViewModel instance */
    private FavMoviesViewModel mFavMoviesViewModel;
    /** DetailFilmActivityDataFragment instance */
    private DetailFilmActivityDataFragment detailFilmActivityDataFragment;
    /** DetailFilmActivityReviewsFragment instance */
    private DetailFilmActivityReviewsFragment detailFilmActivityReviewsFragment;
    /** DetailFilmActivityTrailersFragment instance */
    private DetailFilmActivityTrailersFragment detailFilmActivityTrailersFragment;

    /** Film backdrop image */
    @BindView(R.id.iv_film_backdrop_detail_view) ImageView mFilmBackDropImageView;

    //--------------------------------------------------------------------------------|
    //                               Override Methods                                 |
    //--------------------------------------------------------------------------------|

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);
        ButterKnife.bind(this);

        // Cast Fragments
        castUIDetailFilmFragments();

        if(savedInstanceState != null) {
            // Reload film backdrop image
            filmBackDropURL = savedInstanceState.getString(BACKDROP_URL_KEY);
            updateBackDropImage(filmBackDropURL);
            // Reload favorite film info. Activity --> Fragment comm
            mIsFilmInFavs = savedInstanceState.getBoolean(IS_IN_FAVS_KEY);
            updateFavStateInDataFragment();
        } else {
            Intent i = getIntent();
            if(i.hasExtra(FILM_EXTRA)) {  // Retrieve film object from Intent.extras
                mFilm = i.getParcelableExtra("film");
                populateUIDataFragment(mFilm);
            }
            if(i.hasExtra(IS_IN_FAVS_EXTRA)) {  // Retrieve favorite film info
                mIsFilmInFavs = i.getBooleanExtra(IS_IN_FAVS_EXTRA, false);
                updateFavStateInDataFragment();
            }
        }

        // Get instance of FavMoviesViewModes (for inserting/deleting film to/from fav list)
        mFavMoviesViewModel = ViewModelProviders.of(this).get(FavMoviesViewModel.class);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Create TMDB API client
        apiService =
                TMDBApiClient.getClient().create(TMDBApiInterface.class);

        // Get FavMovie Trailers
        getMovieTrailers(mFilm.getId());

        // Get FavMovie Reviews
        getMovieReviews(mFilm.getId());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BACKDROP_URL_KEY, filmBackDropURL);
        outState.putBoolean(IS_IN_FAVS_KEY, mIsFilmInFavs);
    }

    //--------------------------------------------------------------------------------|
    //                               UI View Methods                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Casts all DetailFilmFragments and sets the click-event listeners.
     */
    private void castUIDetailFilmFragments() {
        detailFilmActivityDataFragment = (DetailFilmActivityDataFragment)
                getFragmentManager().findFragmentById(R.id.fr_detail_film_data);
        if(detailFilmActivityDataFragment != null) {
            detailFilmActivityDataFragment.setmOnFavoriteFilmItemClickListener(this);
        }
        detailFilmActivityReviewsFragment = (DetailFilmActivityReviewsFragment)
                getFragmentManager().findFragmentById(R.id.fr_detail_film_reviews);
        detailFilmActivityTrailersFragment = (DetailFilmActivityTrailersFragment)
                getFragmentManager().findFragmentById(R.id.fr_detail_film_trailers);
    }

    /**
     * Propagates the favorite film info to the DetailFilmActivityDataFragment
     */
    private void updateFavStateInDataFragment() {
        if(detailFilmActivityDataFragment != null) {
            detailFilmActivityDataFragment.setFavFilm(mIsFilmInFavs);
            detailFilmActivityDataFragment.updateAddToFavsUIViews();
        }
    }

    /**
     * Loads the film backdrop image
     *
     * @param filmBackDropURL   Film backdrop url
     */
    private void updateBackDropImage(String filmBackDropURL) {
        Picasso.with(this)
                .load(filmBackDropURL)
                .centerCrop()
                .fit()
                .error(R.drawable.im_image_not_available)
                .into(mFilmBackDropImageView);
    }

    /**
     * Populate UI elements of Detailed Film screen with data retrieved
     * from Film model object.
     *
     * @param   film    Film model object
     */
    private void populateUIDataFragment(@NonNull Film film) {
        filmBackDropURL =
                GlobalsPopularMovies.DEFAULT_BASE_URL + "/"
                        + GlobalsPopularMovies.DEFAULT_BACKDROP_WIDTH + "/"
                        + film.getBackdropPath();
        updateBackDropImage(filmBackDropURL);
        // Propagate film object to Fragment (populate UI)
        if (detailFilmActivityDataFragment != null) {
            detailFilmActivityDataFragment.populateUI(film);
        }
        Log.d(TAG, "Film backdrop URL: " + filmBackDropURL);
    }

    /**
     * Populates the DetailFilmActivityTrailersFragment views with film trailers info
     *
     * @param   trailers    Film Trailers
     */
    private void populateUITrailers(ArrayList<Video> trailers) {
        if(detailFilmActivityTrailersFragment != null) {
            detailFilmActivityTrailersFragment.updateAdapter(trailers);
        }
    }

    /**
     * Populates the DetailFilmActivityReviewsFragment views with film reviews info
     *
     * @param   reviews    Film Reviews
     */
    private void populateUIReviews(ArrayList<Review> reviews) {
        if(detailFilmActivityReviewsFragment != null) {
            detailFilmActivityReviewsFragment.updateAdapter(reviews);
        }
    }

    /**
     * Displays a toast with a custom msg
     *
     * @param   msg     Toast msg
     */
    private void displayToast(String msg) {
        if(mAddToFavsToast != null) { mAddToFavsToast.cancel(); }
        mAddToFavsToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        mAddToFavsToast.show();
        Log.d(TAG, msg);
    }


    //--------------------------------------------------------------------------------|
    //                               TMDB API Request Methods                         |
    //--------------------------------------------------------------------------------|

    /**
     * get /movie/{id}/videos
     * https://developers.themoviedb.org/3/movies/get-movie-videos
     */
    private void getMovieTrailers(final int film_id) {
        Call<VideosResponse> call = apiService.getMovieTrailers(film_id,
                getString(R.string.TMDB_API_KEY));
        call.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call,
                                   Response<VideosResponse> response) {
                ArrayList<Video> trailers = response.body().getResults();
                Log.d(TAG, "TMDB - Requested trailers for film_id: " + film_id +
                        ". Number of trailers received: " + trailers.size());
                populateUITrailers(trailers);
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    /**
     * get /movie/{id}/reviews
     * https://developers.themoviedb.org/3/movies/get-movie-reviews
     */
    private void getMovieReviews(final int film_id) {
        Call<ReviewsResponse> call = apiService.getMovieReviews(film_id,
                getString(R.string.TMDB_API_KEY));
        call.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call,
                                   Response<ReviewsResponse> response) {
                ArrayList<Review> reviews = response.body().getResults();
                Log.d(TAG, "TMDB - Requested reviews for film_id: " + film_id +
                        ". Number of reviews received: " + reviews.size());
                populateUIReviews(reviews);
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }


    //--------------------------------------------------------------------------------|
    //                         Fragment --> Activity Comms.                           |
    //--------------------------------------------------------------------------------|

    @Override
    public void onItemClick() {
        if(mIsFilmInFavs) {
            // TODO Use Repository pattern and re-use film object
            mFavMoviesViewModel.delete(
                    new FavMovie(
                            mFilm.getId(),
                            mFilm.getTitle(),
                            mFilm.getOriginalTitle(),
                            mFilm.getPosterPath(),
                            mFilm.isAdult(),
                            mFilm.getOverview(),
                            mFilm.getReleaseDate(),
                            mFilm.getOriginalLanguage(),
                            mFilm.getBackdropPath(),
                            mFilm.getPopularity(),
                            mFilm.getVoteCount(),
                            mFilm.getVideo(),
                            mFilm.getVoteAverage()));
            displayToast(getString(R.string.remove_from_favorites_toast, mFilm.getTitle()));
        } else {
            // TODO Use Repository pattern and re-use film object
            mFavMoviesViewModel.insert(
                    new FavMovie(
                            mFilm.getId(),
                            mFilm.getTitle(),
                            mFilm.getOriginalTitle(),
                            mFilm.getPosterPath(),
                            mFilm.isAdult(),
                            mFilm.getOverview(),
                            mFilm.getReleaseDate(),
                            mFilm.getOriginalLanguage(),
                            mFilm.getBackdropPath(),
                            mFilm.getPopularity(),
                            mFilm.getVoteCount(),
                            mFilm.getVideo(),
                            mFilm.getVoteAverage()));
            displayToast(getString(R.string.add_to_favorites_toast, mFilm.getTitle()));
        }
        mIsFilmInFavs = !mIsFilmInFavs;
        updateFavStateInDataFragment();
    }
}
