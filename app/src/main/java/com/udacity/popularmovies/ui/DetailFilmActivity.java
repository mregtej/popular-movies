package com.udacity.popularmovies.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.Film;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFilmActivity extends Activity {

    private static final String DEFAULT_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String DEFAULT_POSTER_WIDTH = "w185";

    @BindView(R.id.tv_film_title_detail_view) TextView mFilmTitleTextView;
    @BindView(R.id.tv_film_release_date_detail_view) TextView mFilmReleaseDateTextView;
    @BindView(R.id.iv_film_poster_detail_view) ImageView mFilmPosterImageView;
    @BindView(R.id.tv_film_overview_detail_view) TextView mFilmOverviewTextView;
    @BindView(R.id.tv_film_rating_detail_view) TextView mFilmRatingTextView;

    private static final String TAG = DetailFilmActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);
        ButterKnife.bind(this);

        Film film = (Film) getIntent().getParcelableExtra("film");
        if (film != null) {
            // TODO Populate UI
            populateUI(film);

        }
    }

    /**
     * TODO Improve performance
     * @param film
     */
    private void populateUI(@NonNull Film film) {
        mFilmTitleTextView.setText(film.getTitle());
        String filmPosterURL =
                    DEFAULT_BASE_URL + "/"
                            + DEFAULT_POSTER_WIDTH + "/"
                            + film.getPosterPath();
        Picasso.with(this)
                .load(filmPosterURL)
                .centerCrop()
                .fit()
                .error(R.drawable.image_not_available_drawable)
                .into(mFilmPosterImageView);
        mFilmReleaseDateTextView.setText(film.getReleaseDate());
        mFilmOverviewTextView.setText(film.getOverview());
        mFilmRatingTextView.setText(Double.toString(film.getVoteAverage()));

        Log.d(TAG, "Detail Film-Activity loaded: ");
    }


}
