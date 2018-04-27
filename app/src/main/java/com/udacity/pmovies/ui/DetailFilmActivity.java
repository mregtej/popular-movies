package com.udacity.pmovies.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.pmovies.R;
import com.udacity.pmovies.model.Film;
import com.udacity.pmovies.model.Genres;
import com.udacity.pmovies.model.GenresResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFilmActivity extends Activity {

    private static final String DEFAULT_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String DEFAULT_POSTER_WIDTH = "w185";

    @BindView(R.id.iv_film_backdrop_detail_view) ImageView mFilmBackDropImageView;
    @BindView(R.id.tv_film_title_detail_view) TextView mFilmTitleTextView;
    @BindView(R.id.tv_film_release_date_value_detail_view) TextView mFilmReleaseDateTextView;
    @BindView(R.id.iv_film_poster_detail_view) ImageView mFilmPosterImageView;
    @BindView(R.id.tv_film_overview_detail_view) TextView mFilmOverviewTextView;
    @BindView(R.id.tv_film_rating_detail_view) TextView mFilmRatingTextView;
    @BindView(R.id.tv_film_genres_detail_view) TextView mFilmGenresTextView;

    private static final String TAG = DetailFilmActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);
        ButterKnife.bind(this);

        Film film = (Film) getIntent().getParcelableExtra("film");
        if (film != null) {
            populateUI(film);

        }
    }

    /**
     *
     * @param film
     */
    private void populateUI(@NonNull Film film) {
        mFilmTitleTextView.setText(film.getTitle());
        String filmPosterURL =
                    DEFAULT_BASE_URL + "/"
                            + DEFAULT_POSTER_WIDTH + "/"
                            + film.getPosterPath();
        String filmBackDropURL =
                DEFAULT_BASE_URL + "/"
                        + "w780" + "/"
                        + film.getBackdropPath();
        Picasso.with(this)
                .load(filmPosterURL)
                .centerCrop()
                .fit()
                .error(R.drawable.im_image_not_available)
                .into(mFilmPosterImageView);
        Picasso.with(this)
                .load(filmBackDropURL)
                .centerCrop()
                .fit()
                .error(R.drawable.im_image_not_available)
                .into(mFilmBackDropImageView);
        mFilmReleaseDateTextView.setText(film.getReleaseDate());
        mFilmOverviewTextView.setText(film.getOverview());
        mFilmGenresTextView.setText(buildGenresString(film));
        mFilmOverviewTextView.setMovementMethod(new ScrollingMovementMethod());
        mFilmRatingTextView.setText(getString(R.string.film_rating,
                Double.toString(film.getVoteAverage())));

        Log.d(TAG, "Detail Film-Activity loaded: ");
    }

    private String buildGenresString(@NonNull Film film) {
        StringBuffer sGenres = new StringBuffer("");
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
                            sGenres.append(genre.getName() + ", ");
                        }
                        break;
                    }
                }
            }
            return sGenres.toString();
        } else {
            return getString(R.string.no_film_categories);
        }

    }
}
