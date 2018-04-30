package com.udacity.pmovies.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.pmovies.R;
import com.udacity.pmovies.globals.GlobalsPopularMovies;
import com.udacity.pmovies.model.Film;
import com.udacity.pmovies.model.Genres;
import com.udacity.pmovies.model.GenresResponse;

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


    //--------------------------------------------------------------------------------|
    //                               Params                                           |
    //--------------------------------------------------------------------------------|

    /** DetailFilmActivity context */
    private Context mContext;
    /** Film Poster URL */
    private String filmPosterURL;

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
        }

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
    public void populateUI(@NonNull Film film) {
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

        Log.d(TAG, "Film title: " + film.getTitle());
        Log.d(TAG, "Film poster URL: " + filmPosterURL);
        Log.d(TAG, "Film release-date: " + film.getReleaseDate());
        Log.d(TAG, "Film overview: " + film.getOverview());
        Log.d(TAG, "Film rating " + getString(R.string.film_rating,
                Double.toString(film.getVoteAverage())));
    }


    //--------------------------------------------------------------------------------|
    //                               Support Methods                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Retrieve film genres from Genres look-up table (get genre/movie/list - TMDB Request)
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
