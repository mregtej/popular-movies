package com.udacity.pmovies.comms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Connectivity library support (WiFi/3G).
 */
public class ConnectivityHandler extends BroadcastReceiver {

    private static final String TAG = ConnectivityHandler.class.getSimpleName();

    public static ConnectivityHandlerListener connectivityHandlerListener;

    public ConnectivityHandler() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

        if (connectivityHandlerListener != null) {
            connectivityHandlerListener.onNetworkConnectionChanged(isConnected);
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
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


    public interface ConnectivityHandlerListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

    /**
     * Checks the device WiFi-connectivity.
     *
     * @param	activity	Current Activity.
     * @return	(boolean)	True, if WiFi is connected or connecting;
     * 							false, otherwise.
     */
    private static boolean checkWifiConnectivity(Activity activity) {

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
    private static boolean check3GConnectivity(Activity activity) {

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
