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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * PMovies DetailFilmActivity
 */
public class DetailFilmActivity extends Activity {

    /*********************************************************************************/
    /*                               Constants                                       */
    /*********************************************************************************/

    /** Class name - Log TAG */
    private static final String TAG = DetailFilmActivity.class.getName();
    /** Key for storing film Backdrop URL */
    private static final String BACKDROP_URL_KEY = "backdrop-url";
    /** Film Backdrop URL */
    private String filmBackDropURL;


    /*********************************************************************************/
    /*                               Params                                          */
    /*********************************************************************************/

    /** Film backdrop image */
    @BindView(R.id.iv_film_backdrop_detail_view) ImageView mFilmBackDropImageView;


    /*********************************************************************************/
    /*                              Override methods                                 */
    /*********************************************************************************/

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
            Film film = (Film) getIntent().getParcelableExtra("film");
            if (film != null) {
                // Populate UI
                populateUI(film);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BACKDROP_URL_KEY, filmBackDropURL);
    }

    /*********************************************************************************/
    /*                              UI View methods                                  */
    /*********************************************************************************/

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

}
