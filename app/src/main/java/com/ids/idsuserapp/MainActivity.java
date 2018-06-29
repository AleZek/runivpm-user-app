package com.ids.idsuserapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ids.idsuserapp.entityhandlers.ArcoDataHandler;
import com.ids.idsuserapp.entityhandlers.BeaconDataHandler;
import com.ids.idsuserapp.entityhandlers.MappaDataHandler;
import com.ids.idsuserapp.utils.ConnectionChecker;
import com.ids.idsuserapp.viewmodel.ArcoViewModel;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;
import com.ids.idsuserapp.viewmodel.MappaViewModel;

public class MainActivity extends AppCompatActivity {
    final String TAG = this.getClass().getSimpleName();
    private MappaViewModel mappaViewModel;
    private BeaconViewModel beaconViewModel;
    private ArcoViewModel arcoViewModel;
    private com.android.volley.RequestQueue serverRequestQueue;
    private BeaconDataHandler beaconDataHandler;
    private MappaDataHandler mappaDataHandler;
    private ArcoDataHandler arcoDataHandler;
    private static final int PICKFILE_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscribeTopic("emergenza");
        setContentView(R.layout.activity_main);

        beaconViewModel = new BeaconViewModel(getApplication());
        mappaViewModel = new MappaViewModel(getApplication());
        arcoViewModel = new ArcoViewModel(getApplication());
//
//        beaconDataHandler = new BeaconDataHandler(this, beaconViewModel);
//        mappaDataHandler = new MappaDataHandler(this, mappaViewModel,beaconViewModel);
//        arcoDataHandler = new ArcoDataHandler(this, arcoViewModel, beaconViewModel);
        handleFilePermissions();

        //controlla se la connessione ad internet è attiva dato l application context,
        //se si allora viene pulita la lista dei beacon e viene aggiornato il dataset
        if (ConnectionChecker.getInstance().isNetworkAvailable(getApplicationContext()))
            getDatasetFromServer();

        Intent logoIntent = new Intent(MainActivity.this,LogoActivity.class);
        startActivity(logoIntent);
//        List<Tronco> tronchi = arcoViewModel.getTronchi();

//        Grafo grafo = new Grafo(tronchi);



    }


    //gestisce i permessi dei file , con output nel log se sono  stati garantiti. Altrimenti vengono effettutate le richieste
    // in app. Questo metodo è chiamato in onCreate.
    private void handleFilePermissions(){
        if (isFilePermissionGranted()) {
            Log.v("File Permission", "Permission is granted");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        }
    }

    private boolean isFilePermissionGranted(){
        if (Build.VERSION.SDK_INT >= 23) {
            return checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        else
            return false;
    }

    private void getDatasetFromServer() {
//        cleanBeacon();
//        mappaDataHandler.retrieveMappeDataset();
//        beaconDataHandler.retrieveBeaconDataset();
//        arcoDataHandler.retrieveArchiDataset();
    }

    private void cleanBeacon() {
        beaconViewModel.deleteAll();
    }


    //questo metodo permette alla app di sottoscriversi al topic emergenza, questo permette a firebase
    // di mandare messaggi broadcast alle istanze della app
    private void subscribeTopic(final String topic){
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Sottoscrizione avvenuta a ";
                        if (!task.isSuccessful()) {
                            msg = "sottoscrizione fallita a ";
                        }
                        Log.d(TAG, msg + topic); // sono mostrati dei messaggi nel log e nella app se la sottoscrizione avviene o meno
                        Toast.makeText(MainActivity.this, msg + topic, Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
