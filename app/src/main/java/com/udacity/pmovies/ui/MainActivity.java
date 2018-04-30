package com.udacity.pmovies.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.udacity.pmovies.R;
import com.udacity.pmovies.comms.ConnectivityHandler;
import com.udacity.pmovies.model.APIConfigurationResponse;
import com.udacity.pmovies.model.Film;
import com.udacity.pmovies.model.FilmResponse;
import com.udacity.pmovies.model.GenresResponse;
import com.udacity.pmovies.model.Images;
import com.udacity.pmovies.rest.ApiClient;
import com.udacity.pmovies.rest.ApiInterface;
import com.udacity.pmovies.ui.widgets.AlertDialogHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * PMovies MainActiviy
 *
 * TODO Implement MVP pattern
 */
public class MainActivity extends AppCompatActivity {

    /*********************************************************************************/
    /*                               Constants                                       */
    /*********************************************************************************/

    /** Class name - Log TAG */
    private final static String TAG = MainActivity.class.getName();


    /*********************************************************************************/
    /*                               Params                                          */
    /*********************************************************************************/

    /** TMDB API client */
    private ApiInterface apiService;


    /*********************************************************************************/
    /*                              Override methods                                 */
    /*********************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check network connectivity
        if(!ConnectivityHandler.checkConnectivity(this)) {
            displayConnectivityAlertDialog();
        } else {
            // Check empty API_KEY
            if (getString(R.string.TMDB_API_KEY).isEmpty()) {
                displayNoApiKeyAlertDialog();
            } else {
                // Create TMDB APO client
                apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                // Get API Config - Global access (Singleton pattern)
                getAPIConfiguration();
                // Get Film Genres - Global access (Singleton pattern)
                getGenres();
                // Get Most Popular Movies
                // TODO Default call. Use OnSharedPreferences to remember http request at init
                getMostPopularMovies();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menuSortByMostPopular:
                if(!item.isChecked()) {
                    item.setChecked(true);
                    getMostPopularMovies();
                }
                return true;
            case R.id.menuSortByTopRated:
                if(!item.isChecked()) {
                    item.setChecked(true);
                    getTopRatedMovies();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*********************************************************************************/
    /*                              UI View methods                                  */
    /*********************************************************************************/

    /**
     * Populate UI elements with data retrieved from TMDB.
     * ArrayList of Films shall be propagated to the Fragment.
     *
     * @param   filmList    ArrayList of films retrieved from TMDB response.
     */
    private void populateUI(ArrayList<Film> filmList) {
        // Update your UI here based on result of download.
        MainActivityFragment mainActivityFragment = (MainActivityFragment)
                getFragmentManager().findFragmentById(R.id.fr_main);
        if (mainActivityFragment != null) {
            mainActivityFragment.updateAdapter(filmList);
        }
    }

    /**
     * Display an AlertDialog to warn user that there is no Internet connectivity
     */
    private void displayConnectivityAlertDialog() {
        AlertDialog connectivityDialog = AlertDialogHelper.createMessage(
                this,
                this.getResources().getString(R.string.network_failure),
                this.getResources().getString(R.string.network_user_choice),
                this.getResources().getString(R.string.network_user_choice_wifi),
                this.getResources().getString(R.string.network_user_choice_3g),
                this.getResources().getString(R.string.network_user_choice_no),
                false
        );
        connectivityDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                this.getResources().getString(R.string.network_user_choice_wifi),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(
                                android.provider.Settings.ACTION_WIFI_SETTINGS));
                    }
                });
        connectivityDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                this.getResources().getString(R.string.network_user_choice_3g),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent
                                (android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS));
                    }
                });
        connectivityDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                this.getResources().getString(R.string.network_user_choice_no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        connectivityDialog.show();
    }

    /**
     * Display an AlertDialog to warn user that no API was found.
     *
     * TODO Allow users to add it manually (EditText + storage in SharedPreferences?
     */
    private void displayNoApiKeyAlertDialog() {
        AlertDialog noApiKeyDialog = AlertDialogHelper.createMessage(
                this,
                this.getResources().getString(R.string.no_api_key_failure),
                this.getResources().getString(R.string.no_api_key_user_choice),
                this.getResources().getString(R.string.no_api_key_open_website),
                false
        );
        noApiKeyDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                this.getResources().getString(R.string.no_api_key_open_website),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String url = "http://www.themoviedb.org/faq/api?language=en";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        finish();
                    }
                });
        noApiKeyDialog.show();
    }


    /*********************************************************************************/
    /*                      TMDB API Requests methods                                */
    /*********************************************************************************/

    /**
     * get /movie/popular
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     */
    private void getMostPopularMovies() {
        Call<FilmResponse> call = apiService.getMostPopularMovies(
                getString(R.string.TMDB_API_KEY));
        call.enqueue(new Callback<FilmResponse>() {
            @Override
            public void onResponse(Call<FilmResponse> call,
                                   Response<FilmResponse> response) {
                ArrayList<Film> films = response.body().getResults();
                Log.d(TAG, "TMDB - Requested most popular movies. " +
                        "Number of movies received: " + films.size());
                populateUI(films);
            }

            @Override
            public void onFailure(Call<FilmResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    /**
     * get /movie/top_rated
     * https://developers.themoviedb.org/3/movies/get-top-rated-movies
     */
    private void getTopRatedMovies() {
        Call<FilmResponse> call = apiService.getTopRatedMovies(
                getString(R.string.TMDB_API_KEY));
        call.enqueue(new Callback<FilmResponse>() {
            @Override
            public void onResponse(Call<FilmResponse> call,
                                   Response<FilmResponse> response) {
                ArrayList<Film> films = response.body().getResults();
                Log.d(TAG, "TMDB - Requested top rated movies. " +
                        "Number of movies received: " + films.size());
                populateUI(films);
            }

            @Override
            public void onFailure(Call<FilmResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    /**
     * get /configuration
     * https://developers.themoviedb.org/3/configuration/get-api-configuration
     */
    private void getAPIConfiguration() {
        Call<APIConfigurationResponse> callConfig =
                apiService.getConfiguration(getString(R.string.TMDB_API_KEY));
        callConfig.enqueue(new Callback<APIConfigurationResponse>() {
            @Override
            public void onResponse(Call<APIConfigurationResponse> call,
                                   Response<APIConfigurationResponse> response) {
                Images images = Images.getInstance();
                images.setImageFields(response.body().getImages());
                Log.d(TAG, "TMDB API Config received");
            }

            @Override
            public void onFailure(Call<APIConfigurationResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    /**
     * get /genre/movie/list
     * https://developers.themoviedb.org/3/genres/get-movie-list
     */
    private void getGenres() {
        Call<GenresResponse> callGenres =
                apiService.getGenres(getString(R.string.TMDB_API_KEY));
        callGenres.enqueue(new Callback<GenresResponse>() {
            @Override
            public void onResponse(Call<GenresResponse> call,
                                   Response<GenresResponse> response) {
                GenresResponse genres = GenresResponse.getInstance();
                genres.setGenres(response.body().getGenres());
                Log.d(TAG, "TMDB Genres received");
            }

            @Override
            public void onFailure(Call<GenresResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

}
