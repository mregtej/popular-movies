package com.udacity.pmovies.ui;

import android.arch.lifecycle.Observer;
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
import com.udacity.pmovies.globals.GlobalsPopularMovies;
import com.udacity.pmovies.tmdb_model.Film;
import com.udacity.pmovies.tmdb_model.Review;
import com.udacity.pmovies.tmdb_model.Video;
import com.udacity.pmovies.rest.TMDBApiClient;
import com.udacity.pmovies.rest.TMDBApiInterface;
import com.udacity.pmovies.view_model.FavoriteMoviesViewModel;
import com.udacity.pmovies.view_model.TMDBViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * PMovies DetailFilmActivity
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
    // Add/Remove film to/from favs Toast
    private Toast mAddToFavsToast;
    /** Film object */
    private static Film mFilm;
    /** Flag for identifying if film is in favorite movie list */
    private boolean mIsFilmInFavs;

    /** FavMovies ViewModel instance */
    private FavoriteMoviesViewModel mFavoriteMoviesViewModel;
    /** TMDB ViewModel instance */
    private TMDBViewModel mTmdbViewModel;
    /** TMDB API client */
    private TMDBApiInterface apiService;

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

        /**************************************************************/
        /*                 FavoriteMoviesViewModel                    */
        /**************************************************************/
        // Create FavoriteMoviesViewModel Factory for param injection
        FavoriteMoviesViewModel.Factory favMov_factory = new FavoriteMoviesViewModel.Factory(
                this.getApplication());
        // Get instance of FavoriteMoviesViewModel
        mFavoriteMoviesViewModel = ViewModelProviders.of(this, favMov_factory)
                .get(FavoriteMoviesViewModel.class);


        /**************************************************************/
        /*                        TMDBViewModel                       */
        /**************************************************************/
        // Create TMDB API client
        apiService = TMDBApiClient.getClient().create(TMDBApiInterface.class);
        // Create TMDBViewModel Factory for param injection
        TMDBViewModel.Factory tmdb_factory = new TMDBViewModel.Factory(
                this.getApplication(), apiService, getString(R.string.TMDB_API_KEY));
        // Get instance of TMDBViewModel
        mTmdbViewModel = ViewModelProviders.of(this, tmdb_factory)
                .get(TMDBViewModel.class);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Create TMDB API client
        apiService =
                TMDBApiClient.getClient().create(TMDBApiInterface.class);

        // Get FavMovie Trailers
        mTmdbViewModel.getMovieTrailers(mFilm.getId()).observe(this, new Observer<List<Video>>() {
                    @Override
                    public void onChanged(@Nullable List<Video> trailers) {
                        if (trailers == null || trailers.isEmpty()) {
                            return;
                        } else {
                            populateUITrailers(trailers);
                        }
                    }
                });

        // Get FavMovie Reviews
        mTmdbViewModel.getMovieReviews(mFilm.getId()).observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(@Nullable List<Review> reviews) {
                if (reviews == null || reviews.isEmpty()) {
                    return;
                } else {
                    populateUIReviews(reviews);
                }
            }
        });

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
                getSupportFragmentManager().findFragmentById(R.id.fr_detail_film_data);
        if(detailFilmActivityDataFragment != null) {
            detailFilmActivityDataFragment.setmOnFavoriteFilmItemClickListener(this);
        }
        detailFilmActivityReviewsFragment = (DetailFilmActivityReviewsFragment)
                getSupportFragmentManager().findFragmentById(R.id.fr_detail_film_reviews);
        detailFilmActivityTrailersFragment = (DetailFilmActivityTrailersFragment)
                getSupportFragmentManager().findFragmentById(R.id.fr_detail_film_trailers);
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
    private void populateUITrailers(List<Video> trailers) {
        if(detailFilmActivityTrailersFragment != null) {
            detailFilmActivityTrailersFragment.updateAdapter(trailers);
        }
    }

    /**
     * Populates the DetailFilmActivityReviewsFragment views with film reviews info
     *
     * @param   reviews    Film Reviews
     */
    private void populateUIReviews(List<Review> reviews) {
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
    //                         Fragment --> Activity Comms.                           |
    //--------------------------------------------------------------------------------|

    @Override
    public void onItemClick() {
        if(mIsFilmInFavs) {
            // TODO Use Repository pattern and re-use film object
            mFavoriteMoviesViewModel.delete(mFilm);
            displayToast(getString(R.string.remove_from_favorites_toast, mFilm.getTitle()));
        } else {
            // TODO Use Repository pattern and re-use film object
            mFavoriteMoviesViewModel.insert(mFilm);
            displayToast(getString(R.string.add_to_favorites_toast, mFilm.getTitle()));
        }
        mIsFilmInFavs = !mIsFilmInFavs;
        updateFavStateInDataFragment();
    }
}
