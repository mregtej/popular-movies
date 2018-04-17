package com.udacity.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.udacity.popularmovies.model.PopularFilm;
import com.udacity.popularmovies.network.DownloadCallback;
import com.udacity.popularmovies.network.NetworkFragment;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements DownloadCallback {

    /** NetworkFragment reference used to execute network ops */
    private NetworkFragment mNetworkFragment;

    /**  Boolean telling us whether a download is in progress */
    private boolean mDownloading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNetworkFragment = NetworkFragment.getInstance(getFragmentManager());

        startDownload("https://www.google.com");
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

    private void startDownload(String url) {
        if (!mDownloading && mNetworkFragment != null) {
            // Execute the async download.
            mNetworkFragment.startDownload(url);
            mDownloading = true;
        }
    }

    @Override
    public void updateFromDownload(Object result) {
        // Update your UI here based on result of download.
        MainActivityFragment mainActivityFragment = (MainActivityFragment)
                getFragmentManager().findFragmentById(R.id.fr_main);
        if (mainActivityFragment != null) {
            mainActivityFragment.updateAdapter((ArrayList<PopularFilm>)result);
        }
        finishDownloading();
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
                break;
            case Progress.CONNECT_SUCCESS:
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                break;
        }
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }
}
