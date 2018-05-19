package com.udacity.pmovies.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.pmovies.R;
import com.udacity.pmovies.globals.GlobalsPopularMovies;
import com.udacity.pmovies.model.Film;
import com.udacity.pmovies.model.Review;
import com.udacity.pmovies.model.ReviewsResponse;
import com.udacity.pmovies.model.Video;
import com.udacity.pmovies.model.VideosResponse;
import com.udacity.pmovies.rest.ApiClient;
import com.udacity.pmovies.rest.ApiInterface;

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
public class DetailFilmActivity extends Activity {

    //--------------------------------------------------------------------------------|
    //                               Constants                                        |
    //--------------------------------------------------------------------------------|

    /** Class name - Log TAG */
    private static final String TAG = DetailFilmActivity.class.getName();
    /** Key for storing film Backdrop URL */
    private static final String BACKDROP_URL_KEY = "backdrop-url";
    /** Film Backdrop URL */
    private String filmBackDropURL;


    //--------------------------------------------------------------------------------|
    //                               Params                                           |
    //--------------------------------------------------------------------------------|

    /** Film backdrop image */
    @BindView(R.id.iv_film_backdrop_detail_view) ImageView mFilmBackDropImageView;
    /** TMDB API client */
    private ApiInterface apiService;
    /** Movie uuid */
    private static int movieID;


    //--------------------------------------------------------------------------------|
    //                               Override Methods                                 |
    //--------------------------------------------------------------------------------|

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);
        ButterKnife.bind(this);

        if(savedInstanceState != null) {
            filmBackDropURL = savedInstanceState.getString(BACKDROP_URL_KEY);
            updateUI(filmBackDropURL);
        } else {
            // Retrieve film object from Intent.extras
            Film film = getIntent().getParcelableExtra("film");
            if (film != null) {
                // Save Movie uuid (other HTTP requests)
                movieID = film.getId();
                // Populate UI
                populateUI(film);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Create TMDB API client
        apiService =
                ApiClient.getClient().create(ApiInterface.class);

        // Get Movie Trailers
        getMovieTrailers();

        // Get Movie Reviews
        getMovieReviews();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BACKDROP_URL_KEY, filmBackDropURL);
    }

    //--------------------------------------------------------------------------------|
    //                               UI View Methods                                  |
    //--------------------------------------------------------------------------------|

    private void updateUI(String filmBackDropURL) {
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
    private void populateUI(@NonNull Film film) {
        filmBackDropURL =
                GlobalsPopularMovies.DEFAULT_BASE_URL + "/"
                        + GlobalsPopularMovies.DEFAULT_BACKDROP_WIDTH + "/"
                        + film.getBackdropPath();
        Picasso.with(this)
                .load(filmBackDropURL)
                .centerCrop()
                .fit()
                .error(R.drawable.im_image_not_available)
                .into(mFilmBackDropImageView);

        // Propagate film object to Fragment (populate UI)
        DetailFilmActivityDataFragment detailFilmActivityDataFragment = (DetailFilmActivityDataFragment)
                getFragmentManager().findFragmentById(R.id.fr_detail_film_data);
        if (detailFilmActivityDataFragment != null) {
            detailFilmActivityDataFragment.populateUI(film);
        }

        Log.d(TAG, "Film backdrop URL: " + filmBackDropURL);
    }

    private void populateUITrailers(ArrayList<Video> trailers) {
        DetailFilmActivityTrailersFragment detailFilmActivityTrailersFragment = (DetailFilmActivityTrailersFragment)
                getFragmentManager().findFragmentById((R.id.fr_detail_film_trailers));
        if(detailFilmActivityTrailersFragment != null) {
            detailFilmActivityTrailersFragment.updateAdapter(trailers);
        }
    }

    private void populateUIReviews(ArrayList<Review> reviews) {
        DetailFilmActivityReviewsFragment detailFilmActivityReviewsFragment = (DetailFilmActivityReviewsFragment)
                getFragmentManager().findFragmentById((R.id.fr_detail_film_reviews));
        if(detailFilmActivityReviewsFragment != null) {
            detailFilmActivityReviewsFragment.updateAdapter(reviews);
        }
    }

    /**
     * get /movie/{id}/videos
     * https://developers.themoviedb.org/3/movies/get-movie-videos
     */
    private void getMovieTrailers() {
        Call<VideosResponse> call = apiService.getMovieTrailers(movieID,
                getString(R.string.TMDB_API_KEY));
        call.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call,
                                   Response<VideosResponse> response) {
                ArrayList<Video> trailers = response.body().getResults();
                Log.d(TAG, "TMDB - Requested trailers for film_id: " + movieID +
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
    private void getMovieReviews() {
        Call<ReviewsResponse> call = apiService.getMovieReviews(movieID,
                getString(R.string.TMDB_API_KEY));
        call.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call,
                                   Response<ReviewsResponse> response) {
                ArrayList<Review> reviews = response.body().getResults();
                Log.d(TAG, "TMDB - Requested reviews for film_id: " + movieID +
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

}
