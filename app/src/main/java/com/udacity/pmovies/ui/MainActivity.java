package com.udacity.pmovies.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.udacity.pmovies.R;
import com.udacity.pmovies.model.APIConfigurationResponse;
import com.udacity.pmovies.model.Film;
import com.udacity.pmovies.model.FilmResponse;
import com.udacity.pmovies.model.Genres;
import com.udacity.pmovies.model.GenresResponse;
import com.udacity.pmovies.model.Images;
import com.udacity.pmovies.rest.ApiClient;
import com.udacity.pmovies.rest.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getName();

    private ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getString(R.string.TMDB_API_KEY).isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Please obtain your API KEY first from themoviedb.org",
                    Toast.LENGTH_LONG).show();
            return;
        }

        NetworkInfo networkInfo = getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            // TODO Handle properly no connectivity issues
            // If no connectivity, cancel task and update Callback with null data.
            updateUI(null);
        } else {

            apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            // get /configuration
            // https://developers.themoviedb.org/3/configuration/get-api-configuration
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

            // get /genre/movie/list
            // https://developers.themoviedb.org/3/genres/get-movie-list
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

            // TODO Default call. Use OnSharedPreferences to remember http request at initialization
            // get /movie/popular
            // https://developers.themoviedb.org/3/movies/get-popular-movies
            Call<FilmResponse> call = apiService.getMostPopularMovies(
                    getString(R.string.TMDB_API_KEY));
            call.enqueue(new Callback<FilmResponse>() {
                @Override
                public void onResponse(Call<FilmResponse> call, Response<FilmResponse> response) {
                    ArrayList<Film> films = response.body().getResults();
                    Log.d(TAG, "Number of movies received: " + films.size());
                    updateUI(films);
                }

                @Override
                public void onFailure(Call<FilmResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                }
            });
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
                    // get /movie/popular
                    // https://developers.themoviedb.org/3/movies/get-popular-movies
                    Call<FilmResponse> call = apiService.getMostPopularMovies(
                            getString(R.string.TMDB_API_KEY));
                    call.enqueue(new Callback<FilmResponse>() {
                        @Override
                        public void onResponse(Call<FilmResponse> call,
                                               Response<FilmResponse> response) {
                            ArrayList<Film> films = response.body().getResults();
                            Log.d(TAG, "TMDB - Requested most popular movies. " +
                                    "Number of movies received: " + films.size());
                            updateUI(films);
                        }

                        @Override
                        public void onFailure(Call<FilmResponse> call, Throwable t) {
                            // Log error here since request failed
                            Log.e(TAG, t.toString());
                        }
                    });
                }
                return true;
            case R.id.menuSortByTopRated:
                if(!item.isChecked()) {
                    item.setChecked(true);
                    // get /movie/top_rated
                    // https://developers.themoviedb.org/3/movies/get-top-rated-movies
                    Call<FilmResponse> call = apiService.getTopRatedMovies(
                            getString(R.string.TMDB_API_KEY));
                    call.enqueue(new Callback<FilmResponse>() {
                        @Override
                        public void onResponse(Call<FilmResponse> call,
                                               Response<FilmResponse> response) {
                            ArrayList<Film> films = response.body().getResults();
                            Log.d(TAG, "TMDB - Requested top rated movies. " +
                                    "Number of movies received: " + films.size());
                            updateUI(films);
                        }

                        @Override
                        public void onFailure(Call<FilmResponse> call, Throwable t) {
                            // Log error here since request failed
                            Log.e(TAG, t.toString());
                        }
                    });
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    private void updateUI(ArrayList<Film> filmList) {
        // Update your UI here based on result of download.
        MainActivityFragment mainActivityFragment = (MainActivityFragment)
                getFragmentManager().findFragmentById(R.id.fr_main);
        if (mainActivityFragment != null) {
            mainActivityFragment.updateAdapter(filmList);
        }
    }

}
