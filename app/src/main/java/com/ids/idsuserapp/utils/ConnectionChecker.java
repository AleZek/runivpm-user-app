package com.ids.idsuserapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by zek on 12/04/18.
 */

public class ConnectionChecker {

    private static ConnectionChecker INSTANCE = null;


    private ConnectionChecker() {};

    public static ConnectionChecker getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConnectionChecker();
        }
        return(INSTANCE);
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }
}
