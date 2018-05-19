package com.udacity.pmovies.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.pmovies.R;
import com.udacity.pmovies.adapter.TrailerAdapter;
import com.udacity.pmovies.globals.GlobalsPopularMovies;
import com.udacity.pmovies.model.Film;
import com.udacity.pmovies.model.Genres;
import com.udacity.pmovies.model.GenresResponse;
import com.udacity.pmovies.model.Video;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * PMovies DetailFilmActivityDataFragment
 *
 * TODO Implement MVP pattern
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
    /** Film Poster URL */
    private String filmPosterURL;
    // TODO Remove after DB integration
    private static boolean wasFilmAddedToFavs = false;
    // Add/Remove film to/from favs Toast
    private Toast mAddToFavsToast;


    /** Film title */
    @BindView(R.id.tv_film_title_detail_view) TextView mFilmTitleTextView;
    /** Film release date */
    @BindView(R.id.tv_film_release_date_value_detail_view) TextView mFilmReleaseDateTextView;
    /** Film poster */
    @BindView(R.id.iv_film_poster_detail_view) ImageView mFilmPosterImageView;
    /** Film overview */
    @BindView(R.id.tv_film_overview_detail_view) TextView mFilmOverviewTextView;
    /** Film rating (over 10) */
    @BindView(R.id.tv_film_rating_detail_view) TextView mFilmRatingTextView;
    /** Film genres */
    @BindView(R.id.tv_film_genres_detail_view) TextView mFilmGenresTextView;
    /** Film add to favorites button */
    @BindView(R.id.iv_film_add_to_favorite_icon_detail_view) ImageView mAddFilmToFavsImageView;
    /** Film add/remove to/from favorites Text */
    @BindView(R.id.tv_film_add_to_favorite_label_detail_view) TextView mAddRemoveFilmToFromTextView;


    //--------------------------------------------------------------------------------|
    //                               Constructors                                     |
    //--------------------------------------------------------------------------------|

    /**
     * Empty constructor
     */
    public DetailFilmActivityDataFragment() { }


    //--------------------------------------------------------------------------------|
    //                               Override Methods                                 |
    //--------------------------------------------------------------------------------|

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
            wasFilmAddedToFavs = savedInstanceState.getBoolean(ADD_TO_FAVS_KEY);
        }

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
        savedInstanceState.putBoolean(ADD_TO_FAVS_KEY, wasFilmAddedToFavs);
    }


    //--------------------------------------------------------------------------------|
    //                               UI View Methods                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Populate UI elements of Detailed Film screen with data retrieved
     * from Film model object.
     *
     * @param   film    Film model object
     */
    public void populateUI(@NonNull final Film film) {
        mFilmTitleTextView.setText(film.getTitle());
        filmPosterURL =
                GlobalsPopularMovies.DEFAULT_BASE_URL + "/"
                        + GlobalsPopularMovies.DEFAULT_POSTER_WIDTH + "/"
                        + film.getPosterPath();
        Picasso.with(mContext)
                .load(filmPosterURL)
                .centerCrop()
                .fit()
                .error(R.drawable.im_image_not_available)
                .into(mFilmPosterImageView);
        mFilmReleaseDateTextView.setText(film.getReleaseDate());
        mFilmOverviewTextView.setText(film.getOverview());
        mFilmGenresTextView.setText(buildGenresString(film));
        mFilmOverviewTextView.setMovementMethod(new ScrollingMovementMethod());
        mFilmRatingTextView.setText(getString(R.string.film_rating,
                Double.toString(film.getVoteAverage())));
        // TODO Check if film was previously stored in Favs. For now, local variable
        mAddFilmToFavsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wasFilmAddedToFavs) {
                    // TODO Implement film deletion from Favs
                    // Update icon status and string
                    updateAddToFavsUIViews(false,film.getTitle());
                } else {
                    // TODO Implement film insertion to Favs
                    // Update icon status and string
                    updateAddToFavsUIViews(true,film.getTitle());
                }
                wasFilmAddedToFavs = !wasFilmAddedToFavs;
            }
        });

        Log.d(TAG, "Film title: " + film.getTitle());
        Log.d(TAG, "Film poster URL: " + filmPosterURL);
        Log.d(TAG, "Film release-date: " + film.getReleaseDate());
        Log.d(TAG, "Film overview: " + film.getOverview());
        Log.d(TAG, "Film rating " + getString(R.string.film_rating,
                Double.toString(film.getVoteAverage())));
        Log.d(TAG, "Film added on favs? " + (wasFilmAddedToFavs ? "TRUE" : "FALSE"));
    }

    /**
     * Update UI views depending on if film was added to favs or removed from them.
     *
     * @param addedStatus   true: added to favs, false = removed from favs
     * @param title         Film title
     */
    private void updateAddToFavsUIViews(boolean addedStatus, String title) {
        if(addedStatus) {
            mAddFilmToFavsImageView.setBackground(
                    ContextCompat.getDrawable(mContext, R.drawable.ic_rating));
            mAddRemoveFilmToFromTextView.setText(
                    getString(R.string.remove_from_favorites_title));
            if(mAddToFavsToast != null) {
                mAddToFavsToast.cancel();
            }
            mAddToFavsToast = Toast.makeText(
                    mContext,
                    getString(R.string.add_to_favorites_toast, title),
                    Toast.LENGTH_SHORT);
            Log.d(TAG, getString(R.string.add_to_favorites_toast,title));
        } else {
            mAddFilmToFavsImageView.setBackground(
                    ContextCompat.getDrawable(mContext, R.drawable.ic_add_to_favs));
            mAddRemoveFilmToFromTextView.setText(
                    getString(R.string.add_to_favorites_title));
            if(mAddToFavsToast != null) {
                mAddToFavsToast.cancel();
            }
            mAddToFavsToast = Toast.makeText(
                    mContext,
                    getString(R.string.remove_from_favorites_toast, title),
                    Toast.LENGTH_SHORT);
            Log.d(TAG, getString(R.string.remove_from_favorites_toast,title));
        }
        mAddToFavsToast.show();
    }


    //--------------------------------------------------------------------------------|
    //                               Support Methods                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Retrieve film genres from Genres look-up table (get genre/movie/list - TMDB API Request)
     *
     * @param   film Film model object
     * @return  Film genres (String)
     */
    private String buildGenresString(@NonNull Film film) {
        StringBuilder sGenres = new StringBuilder("");
        GenresResponse genres = GenresResponse.getInstance();
        if(genres != null) {
            ArrayList<Integer> filmGenres = film.getGenreIds();
            for (int i = 0; i < filmGenres.size(); i++) {
                int genreID = filmGenres.get(i);
                for (Genres genre : genres.getGenres()) {
                    if (genre.getId() == genreID) {
                        if (i == filmGenres.size() - 1) {
                            sGenres.append(genre.getName());
                        } else {
                            sGenres.append(genre.getName()).append(", ");
                        }
                        break;
                    }
                }
            }
            Log.d(TAG, "Film genres: " + sGenres.toString());
            return sGenres.toString();
        } else {
            return getString(R.string.no_film_categories);
        }
    }

}
