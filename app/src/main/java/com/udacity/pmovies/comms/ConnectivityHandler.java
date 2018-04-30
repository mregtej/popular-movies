package com.udacity.pmovies.comms;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Connectivity library support (WiFi/3G).
 */
public class ConnectivityHandler {

    private static final String TAG = ConnectivityHandler.class.getSimpleName();

    /**
     * Checks the device connectivity (WiFi and/or 3G).
     *
     * @param	activity	Current Activity.
     * @return	(boolean)	True, if the device is connected or connecting;
     * 							false, otherwise.
     */
    public static boolean checkConnectivity(Activity activity) {

        ConnectivityManager connManager = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;

        if(connManager == null) {
            Log.w( TAG , "Unable to retrieve android.net.ConnectivityManager" );
        } else {
            networkInfo = connManager.getActiveNetworkInfo();
        }

        return networkInfo != null && networkInfo.isConnectedOrConnecting()
                && networkInfo.isAvailable();

    }

    /**
     * Checks the device WiFi-connectivity.
     *
     * @param	activity	Current Activity.
     * @return	(boolean)	True, if WiFi is connected or connecting;
     * 							false, otherwise.
     */
    public static boolean checkWifiConnectivity(Activity activity) {

        ConnectivityManager connManager = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;

        if(connManager == null) {
            Log.w( TAG , "Unable to retrieve android.net.ConnectivityManager" );
        } else {
            networkInfo =
                    connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }

        return networkInfo!=null && networkInfo.isConnected();

    }

    /**
     * Checks the device 3G-connectivity.
     *
     * @param	activity	Current Activity.
     * @return	(boolean)	True, if 3G is connected or connecting;
     * 							false, otherwise.
     */
    public static boolean check3GConnectivity(Activity activity) {

        ConnectivityManager connManager = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;

        if(connManager == null) {
            Log.w( TAG , "Unable to retrieve android.net.ConnectivityManager" );
        } else {
            networkInfo =
                    connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        }

        return networkInfo!=null && networkInfo.isConnected();

    }

}