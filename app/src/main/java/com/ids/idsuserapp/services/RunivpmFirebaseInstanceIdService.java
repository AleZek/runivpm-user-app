package com.ids.idsuserapp.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
/**
 * usato per inserire un override del comportamento della classe FirebaseInstanceIdService.
 * OnTokenRefresh viene chiamato quando il token cambia per vari motivi come ad esempio reinstall della
 * app, cancellazione dati etc. è bene quindi recuperare il token che serve a firebase per comunicare con il dispositivo
 * specifico.
*/
public class RunivpmFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        //TODO: implementare qui le azioni da intraprendere in caso di cambiamento del token
    }

}
