package com.udacity.popularmovies.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.APIConfigurationResponse;
import com.udacity.popularmovies.model.Film;
import com.udacity.popularmovies.model.FilmResponse;
import com.udacity.popularmovies.model.Images;
import com.udacity.popularmovies.rest.ApiClient;
import com.udacity.popularmovies.rest.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getName();

    // TODO - insert your themoviedb.org API KEY here
    private final static String API_KEY = "***REMOVED***";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Please obtain your API KEY first from themoviedb.org",
                    Toast.LENGTH_LONG).show();
            return;
        }

        NetworkInfo networkInfo = getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            // If no connectivity, cancel task and update Callback with null data.
            updateUI(null);
        } else {

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<APIConfigurationResponse> callConfig = apiService.getConfiguration(API_KEY);
            callConfig.enqueue(new Callback<APIConfigurationResponse>() {
                @Override
                public void onResponse(Call<APIConfigurationResponse> call, Response<APIConfigurationResponse> response) {
                    final Images images = response.body().getImages();
                    Log.d(TAG, "Config received");
                    updateAPIConfiguration(images);
                }

                @Override
                public void onFailure(Call<APIConfigurationResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                }
            });

            Call<FilmResponse> call = apiService.getTopRatedMovies(API_KEY);
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
            case R.id.action_settings:
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

    private void updateAPIConfiguration(Images images) {
        // Update your UI here based on result of download.
        MainActivityFragment mainActivityFragment = (MainActivityFragment)
                getFragmentManager().findFragmentById(R.id.fr_main);
        if (mainActivityFragment != null) {
            mainActivityFragment.updateAPIConfiguration(images);
        }
    }

}
