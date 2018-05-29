package com.udacity.pmovies.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
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
import com.udacity.pmovies.application.PMoviesApp;
import com.udacity.pmovies.comms.ConnectivityHandler;
import com.udacity.pmovies.database_model.FavFilm;
import com.udacity.pmovies.globals.GlobalsPopularMovies;
import com.udacity.pmovies.tmdb_model.TMDBFilm;
import com.udacity.pmovies.tmdb_model.Review;
import com.udacity.pmovies.tmdb_model.Video;
import com.udacity.pmovies.rest.TMDBApiClient;
import com.udacity.pmovies.rest.TMDBApiInterface;
import com.udacity.pmovies.ui.utils.TextUtils;
import com.udacity.pmovies.ui.widgets.AlertDialogHelper;
import com.udacity.pmovies.view_model.FavoriteMoviesViewModel;
import com.udacity.pmovies.view_model.TMDBViewModel;

import java.util.ArrayList;
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
    /** Key for retrieving TMDBFilm parcelable object from Intent */
    private static final String FILM_EXTRA = "film";
    /** Key for identifying if film is in favorite movie list from Intent */
    private static final String IS_IN_FAVS_EXTRA = "is-in-favs";


    //--------------------------------------------------------------------------------|
    //                               Params                                           |
    //--------------------------------------------------------------------------------|

    /** TMDBFilm Backdrop URL */
    private String filmBackDropURL;
    // Add/Remove film to/from favs Toast
    private Toast mAddToFavsToast;
    /** TMDBFilm object */
    private static TMDBFilm mTMDBFilm;
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

    /** TMDBFilm backdrop image */
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

        // Check network connectivity
        if (!ConnectivityHandler.isConnected(this)) {
            displayConnectivityAlertDialog();
        }

        else {

            if (savedInstanceState != null) {

                // Reload film backdrop image
                filmBackDropURL = savedInstanceState.getString(BACKDROP_URL_KEY);
                updateBackDropImage(filmBackDropURL);
                // Reload favorite film info. Activity --> Fragment comm
                mIsFilmInFavs = savedInstanceState.getBoolean(IS_IN_FAVS_KEY);
                updateFavStateInDataFragment();

            } else {

                Intent i = getIntent();
                if (i.hasExtra(FILM_EXTRA)) {  // Retrieve film object from Intent.extras
                    mTMDBFilm = i.getParcelableExtra(FILM_EXTRA);
                    populateUIDataFragment(mTMDBFilm);
                }
                if (i.hasExtra(IS_IN_FAVS_EXTRA)) {  // Retrieve favorite film info
                    mIsFilmInFavs = i.getBooleanExtra(IS_IN_FAVS_EXTRA, false);
                    updateFavStateInDataFragment();
                }

                // Subscribes DetailFilmActivity to receive notifications from TMDBViewModel
                subscribeToTMDBViewModel();

                // Get movie trailers from TMDBViewModel
                getMovieTrailers();
                // Get movie reviews from TMDBViewModel
                getMovieReviews();

            }

            // Subscribes DetailFilmActivity to receive notifications from FavoriteMoviesViewModel
            subscribeToFavoriteMoviesViewModel();
        }
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
     * @param filmBackDropURL   TMDBFilm backdrop url
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
     * Populate UI elements of Detailed TMDBFilm screen with data retrieved
     * from TMDBFilm model object.
     *
     * @param   TMDBFilm    TMDBFilm model object
     */
    private void populateUIDataFragment(@NonNull TMDBFilm TMDBFilm) {
        filmBackDropURL =
                GlobalsPopularMovies.DEFAULT_BASE_URL + "/"
                        + GlobalsPopularMovies.DEFAULT_BACKDROP_WIDTH + "/"
                        + TMDBFilm.getBackdropPath();
        updateBackDropImage(filmBackDropURL);
        // Propagate TMDBFilm object to Fragment (populate UI)
        if (detailFilmActivityDataFragment != null) {
            detailFilmActivityDataFragment.populateUI(TMDBFilm);
        }
        Log.d(TAG, "TMDBFilm backdrop URL: " + filmBackDropURL);
    }

    /**
     * Populates the DetailFilmActivityTrailersFragment views with film trailers info
     *
     * @param   trailers    TMDBFilm Trailers
     */
    private void populateUITrailers(ArrayList<Video> trailers) {
        if(detailFilmActivityTrailersFragment != null) {
            detailFilmActivityTrailersFragment.updateAdapter(trailers);
        }
    }

    /**
     * Populates the DetailFilmActivityReviewsFragment views with film reviews info
     *
     * @param   reviews    TMDBFilm Reviews
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

    /**
     * Display an AlertDialog to warn user that there is no Internet connectivity
     */
    private void displayConnectivityAlertDialog() {

        DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(
                        android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent
                        (android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS));
            }
        };

        DialogInterface.OnClickListener neutralListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        };

        DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        };

        AlertDialogHelper.createMessage(
                this,
                this.getResources().getString(R.string.network_failure),
                this.getResources().getString(R.string.network_user_choice),
                this.getResources().getString(R.string.network_user_choice_wifi),
                this.getResources().getString(R.string.network_user_choice_3g),
                this.getResources().getString(R.string.network_user_choice_no),
                okListener, cancelListener, neutralListener, dismissListener,
                true
        ).show();
    }

    //--------------------------------------------------------------------------------|
    //                         Fragment --> ModelView comms                           |
    //--------------------------------------------------------------------------------|

    private void subscribeToFavoriteMoviesViewModel() {
        // Create FavoriteMoviesViewModel Factory for param injection
        FavoriteMoviesViewModel.Factory favMov_factory = new FavoriteMoviesViewModel.Factory(
                this.getApplication());
        // Get instance of FavoriteMoviesViewModel
        mFavoriteMoviesViewModel = ViewModelProviders.of(this, favMov_factory)
                .get(FavoriteMoviesViewModel.class);
    }

    private void subscribeToTMDBViewModel() {
        // Create TMDB API client
        apiService = TMDBApiClient.getClient().create(TMDBApiInterface.class);
        // Create TMDBViewModel Factory for param injection
        TMDBViewModel.Factory tmdb_factory = new TMDBViewModel.Factory(
                this.getApplication(), apiService, getString(R.string.TMDB_API_KEY));
        // Get instance of TMDBViewModel
        mTmdbViewModel = ViewModelProviders.of(this, tmdb_factory)
                .get(TMDBViewModel.class);
    }

    private void getMovieTrailers() {
        mTmdbViewModel.getMovieTrailers(mTMDBFilm.getId()).observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(@Nullable List<Video> trailers) {
                if (trailers == null || trailers.isEmpty()) {
                    return;
                } else {
                    populateUITrailers(new ArrayList<>(trailers));
                }
            }
        });
    }

    private void getMovieReviews() {
        mTmdbViewModel.getMovieReviews(mTMDBFilm.getId()).observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(@Nullable List<Review> reviews) {
                if (reviews == null || reviews.isEmpty()) {
                    return;
                } else {
                    populateUIReviews(new ArrayList<>(reviews));
                }
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
            mFavoriteMoviesViewModel.delete(new FavFilm(
                    mTMDBFilm.getId(),
                    mTMDBFilm.getTitle(),
                    mTMDBFilm.getPosterPath(),
                    mTMDBFilm.isAdult(),
                    mTMDBFilm.getOverview(),
                    mTMDBFilm.getReleaseDate(),
                    mTMDBFilm.getOriginalTitle(),
                    mTMDBFilm.getOriginalLanguage(),
                    mTMDBFilm.getBackdropPath(),
                    mTMDBFilm.getPopularity(),
                    mTMDBFilm.getVoteCount(),
                    mTMDBFilm.getVideo(),
                    mTMDBFilm.getVoteAverage(),
                    TextUtils.convertListOfIntToString(mTMDBFilm.getGenreIds())
            ));
            displayToast(getString(R.string.remove_from_favorites_toast, mTMDBFilm.getTitle()));
        } else {
            // TODO Use Repository pattern and re-use film object
            mFavoriteMoviesViewModel.insert(new FavFilm(
                    mTMDBFilm.getId(),
                    mTMDBFilm.getTitle(),
                    mTMDBFilm.getPosterPath(),
                    mTMDBFilm.isAdult(),
                    mTMDBFilm.getOverview(),
                    mTMDBFilm.getReleaseDate(),
                    mTMDBFilm.getOriginalTitle(),
                    mTMDBFilm.getOriginalLanguage(),
                    mTMDBFilm.getBackdropPath(),
                    mTMDBFilm.getPopularity(),
                    mTMDBFilm.getVoteCount(),
                    mTMDBFilm.getVideo(),
                    mTMDBFilm.getVoteAverage(),
                    TextUtils.convertListOfIntToString(mTMDBFilm.getGenreIds())
            ));
            displayToast(getString(R.string.add_to_favorites_toast, mTMDBFilm.getTitle()));
        }
        mIsFilmInFavs = !mIsFilmInFavs;
        updateFavStateInDataFragment();
    }

}
