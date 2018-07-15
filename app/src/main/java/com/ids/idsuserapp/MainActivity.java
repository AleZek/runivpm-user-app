package com.ids.idsuserapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ids.idsuserapp.authentication.LoginActivity;
import com.ids.idsuserapp.utils.ConnectionChecker;
import com.ids.idsuserapp.utils.PermissionsUtil;

public class MainActivity extends AppCompatActivity {
    final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscribeTopic("emergenza1");
        setContentView(R.layout.activity_main);

        Intent logoIntent = new Intent(MainActivity.this,LogoActivity.class);
        logoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logoIntent);


    }
    //questo metodo permette alla app di sottoscriversi al topic emergenza, questo permette a firebase
    // di mandare messaggi broadcast alle istanze della app
    private void subscribeTopic(final String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (!task.isSuccessful()) {
                            Log.d(TAG, getString(R.string.emergency_subscribe_error)); // sono mostrati dei messaggi nel log e nella app se la sottoscrizione avviene o meno
                            Toast.makeText(MainActivity.this, R.string.emergency_subscribe_error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }
}
