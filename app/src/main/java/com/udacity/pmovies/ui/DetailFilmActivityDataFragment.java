package com.udacity.pmovies.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.pmovies.R;
import com.udacity.pmovies.globals.GlobalsPopularMovies;
import com.udacity.pmovies.rest.TMDBApiClient;
import com.udacity.pmovies.rest.TMDBApiInterface;
import com.udacity.pmovies.tmdb_model.TMDBFilm;
import com.udacity.pmovies.tmdb_model.Genres;
import com.udacity.pmovies.tmdb_model.GenresResponse;
import com.udacity.pmovies.view_model.FavoriteMoviesViewModel;
import com.udacity.pmovies.view_model.TMDBViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * PMovies DetailFilmActivityDataFragment
 */
public class DetailFilmActivityDataFragment extends Fragment {

    //--------------------------------------------------------------------------------|
    //                               Constants                                        |
    //--------------------------------------------------------------------------------|

    /** Class name - Log TAG */
    private static final String TAG = DetailFilmActivityDataFragment.class.getName();

    private static final String TITLE_KEY = "title";
    private static final String RELEASE_DATE_KEY = "release-date";
    private static final String POSTER_URL_KEY = "poster-url";
    private static final String OVERVIEW_KEY = "overview";
    private static final String RATING_KEY = "rating";
    private static final String GENRES_KEY = "genres";
    private static final String ADD_TO_FAVS_KEY = "add-to-favs";


    //--------------------------------------------------------------------------------|
    //                               Params                                           |
    //--------------------------------------------------------------------------------|

    /** DetailFilmActivity context */
    private Context mContext;
    /** TMDBFilm Poster URL */
    private String filmPosterURL;
    /** Add/Remove to/from favorite movie list OnClickListener */
    private OnFavoriteFilmItemClickListener mOnFavoriteFilmItemClickListener;
    /** Flag for identifying if film is in favorites movie list */
    private boolean isFavFilm;

    /** FavMovies ViewModel instance */
    private FavoriteMoviesViewModel mFavoriteMoviesViewModel;
    /** TMDB ViewModel instance */
    private TMDBViewModel mTmdbViewModel;
    /** TMDB API client */
    private TMDBApiInterface apiService;

    /** TMDBFilm title */
    @BindView(R.id.tv_film_title_detail_view) TextView mFilmTitleTextView;
    /** TMDBFilm release date */
    @BindView(R.id.tv_film_release_date_value_detail_view) TextView mFilmReleaseDateTextView;
    /** TMDBFilm poster */
    @BindView(R.id.iv_film_poster_detail_view) ImageView mFilmPosterImageView;
    /** TMDBFilm overview */
    @BindView(R.id.tv_film_overview_detail_view) TextView mFilmOverviewTextView;
    /** TMDBFilm rating (over 10) */
    @BindView(R.id.tv_film_rating_detail_view) TextView mFilmRatingTextView;
    /** TMDBFilm genres */
    @BindView(R.id.tv_film_genres_detail_view) TextView mFilmGenresTextView;
    /** TMDBFilm add to favorites button */
    @BindView(R.id.iv_film_add_to_favorite_icon_detail_view) ImageView mAddFilmToFavsImageView;
    /** TMDBFilm add/remove to/from favorites Text */
    @BindView(R.id.tv_film_add_to_favorite_label_detail_view) TextView mAddRemoveFilmToFromTextView;


    //--------------------------------------------------------------------------------|
    //                               Constructors                                     |
    //--------------------------------------------------------------------------------|

    /**
     * Empty constructor
     */
    public DetailFilmActivityDataFragment() { }


    //--------------------------------------------------------------------------------|
    //                                    Setters                                     |
    //--------------------------------------------------------------------------------|

    /**
     * Sets the Add/Remove to/from favorite movie list OnClickListener
     *
     * @param   mOnFavoriteFilmItemClickListener    Listener
     */
    public void setmOnFavoriteFilmItemClickListener(
            OnFavoriteFilmItemClickListener mOnFavoriteFilmItemClickListener) {
        this.mOnFavoriteFilmItemClickListener = mOnFavoriteFilmItemClickListener;
    }

    /**
     * Sets favorite movie list (retrieved from FavMovie ViewModel)
     *
     * @param   favFilm     Favorite movie list
     */
    public void setFavFilm(boolean favFilm) {
        this.isFavFilm = favFilm;
    }

    //--------------------------------------------------------------------------------|
    //                               Override Methods                                 |
    //--------------------------------------------------------------------------------|

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /**************************************************************/
        /*                 FavoriteMoviesViewModel                    */
        /**************************************************************/
        // Create FavoriteMoviesViewModel Factory for param injection
        FavoriteMoviesViewModel.Factory favMov_factory = new FavoriteMoviesViewModel.Factory(
                getActivity().getApplication());
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
                getActivity().getApplication(), apiService, getString(R.string.TMDB_API_KEY));
        // Get instance of TMDBViewModel
        mTmdbViewModel = ViewModelProviders.of(this, tmdb_factory)
                .get(TMDBViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate Views
        View rootView = inflater.inflate(R.layout.fragment_detail_film_data,
                container, false);
        ButterKnife.bind(this, rootView);
        mContext = rootView.getContext();

        if(savedInstanceState != null) {
            mFilmTitleTextView.setText(savedInstanceState.getString(TITLE_KEY));
            filmPosterURL = savedInstanceState.getString(POSTER_URL_KEY);
            Picasso.with(mContext)
                    .load(filmPosterURL)
                    .centerCrop()
                    .fit()
                    .error(R.drawable.im_image_not_available)
                    .into(mFilmPosterImageView);
            mFilmReleaseDateTextView.setText(savedInstanceState.getString(RELEASE_DATE_KEY));
            mFilmOverviewTextView.setText(savedInstanceState.getString(OVERVIEW_KEY));
            mFilmOverviewTextView.setMovementMethod(new ScrollingMovementMethod());
            mFilmRatingTextView.setText(savedInstanceState.getString(RATING_KEY));
            mFilmGenresTextView.setText(savedInstanceState.getString(GENRES_KEY));

            isFavFilm = savedInstanceState.getBoolean(ADD_TO_FAVS_KEY);
        }

        // Set touch listeners for handling ScrollableNestedViews
        mFilmOverviewTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mFilmOverviewTextView.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mFilmOverviewTextView.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        // Sets callback for Add/Remove to/from favorite movie list OnClickListener
        mAddFilmToFavsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFavoriteFilmItemClickListener.onItemClick();
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(TITLE_KEY, mFilmTitleTextView.getText().toString());
        savedInstanceState.putString(RELEASE_DATE_KEY,
                mFilmReleaseDateTextView.getText().toString());
        savedInstanceState.putString(POSTER_URL_KEY, filmPosterURL);
        savedInstanceState.putString(OVERVIEW_KEY, mFilmOverviewTextView.getText().toString());
        savedInstanceState.putString(RATING_KEY, mFilmRatingTextView.getText().toString());
        savedInstanceState.putString(GENRES_KEY, mFilmGenresTextView.getText().toString());
        savedInstanceState.putBoolean(ADD_TO_FAVS_KEY, isFavFilm);
    }


    //--------------------------------------------------------------------------------|
    //                               UI View Methods                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Populate UI elements of Detailed TMDBFilm screen with data retrieved
     * from TMDBFilm model object.
     *
     * @param   TMDBFilm    TMDBFilm model object
     */
    public void populateUI(@NonNull final TMDBFilm TMDBFilm) {
        mFilmTitleTextView.setText(TMDBFilm.getTitle());
        filmPosterURL =
                GlobalsPopularMovies.DEFAULT_BASE_URL + "/"
                        + GlobalsPopularMovies.DEFAULT_POSTER_WIDTH + "/"
                        + TMDBFilm.getPosterPath();
        Picasso.with(mContext)
                .load(filmPosterURL)
                .centerCrop()
                .fit()
                .error(R.drawable.im_image_not_available)
                .into(mFilmPosterImageView);
        mFilmReleaseDateTextView.setText(TMDBFilm.getReleaseDate());
        mFilmOverviewTextView.setText(TMDBFilm.getOverview());
        mFilmGenresTextView.setText(buildGenresString(TMDBFilm));
        mFilmOverviewTextView.setMovementMethod(new ScrollingMovementMethod());
        mFilmRatingTextView.setText(getString(R.string.film_rating,
                Double.toString(TMDBFilm.getVoteAverage())));

        Log.d(TAG, "TMDBFilm title: " + TMDBFilm.getTitle());
        Log.d(TAG, "TMDBFilm poster URL: " + filmPosterURL);
        Log.d(TAG, "TMDBFilm release-date: " + TMDBFilm.getReleaseDate());
        Log.d(TAG, "TMDBFilm overview: " + TMDBFilm.getOverview());
        Log.d(TAG, "TMDBFilm rating " + getString(R.string.film_rating,
                Double.toString(TMDBFilm.getVoteAverage())));
        Log.d(TAG, "TMDBFilm added on favs? " + (isFavFilm ? "TRUE" : "FALSE"));
    }

    /**
     * Update UI views depending on if film was added to favs or removed from them.
     */
    public void updateAddToFavsUIViews() {
        if(isFavFilm) {
            mAddFilmToFavsImageView.setBackground(
                    ContextCompat.getDrawable(mContext, R.drawable.ic_rating));
            mAddRemoveFilmToFromTextView.setText(
                    getString(R.string.remove_from_favorites_title));
        } else {
            mAddFilmToFavsImageView.setBackground(
                    ContextCompat.getDrawable(mContext, R.drawable.ic_add_to_favs));
            mAddRemoveFilmToFromTextView.setText(
                    getString(R.string.add_to_favorites_title));
        }
    }



    //--------------------------------------------------------------------------------|
    //                               Support Methods                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Retrieve TMDBFilm genres from Genres look-up table (get genre/movie/list - TMDB API Request)
     *
     * @param   TMDBFilm TMDBFilm model object
     * @return  TMDBFilm genres (String)
     */
    private String buildGenresString(@NonNull TMDBFilm TMDBFilm) {
        StringBuilder sGenres = new StringBuilder("");
        GenresResponse genres = GenresResponse.getInstance();
        if(genres != null) {
            List<Integer> genreIds = TMDBFilm.getGenreIds();
            for (int genreID : genreIds) {
                for (Genres genre : genres.getGenres()) {
                    if (genre.getId() == genreID) {
                        if (genreID == genreIds.size() - 1) {
                            sGenres.append(genre.getName());
                        } else {
                            sGenres.append(genre.getName()).append(", ");
                        }
                        break;
                    }
                }
            }
            Log.d(TAG, "TMDBFilm genres: " + sGenres.toString());
            return sGenres.toString();
        } else {
            return getString(R.string.no_film_categories);
        }
    }

    //--------------------------------------------------------------------------------|
    //                      Fragment--Activity Comm Interfaces                        |
    //--------------------------------------------------------------------------------|

    /**
     * Interface for handling the film-click
     */
    public interface OnFavoriteFilmItemClickListener {
        void onItemClick();
    }

}
