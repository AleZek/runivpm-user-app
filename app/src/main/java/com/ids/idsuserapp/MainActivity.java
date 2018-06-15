package com.ids.idsuserapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ids.idsuserapp.entityhandlers.BeaconDataHandler;
import com.ids.idsuserapp.entityhandlers.MappaDataHandler;
import com.ids.idsuserapp.fragment.BeaconRecyclerFragment;
import com.ids.idsuserapp.utils.ConnectionChecker;
import com.ids.idsuserapp.utils.MyFirebaseInstanceIdService;
import com.ids.idsuserapp.utils.MyFirebaseMessagingService;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;
import com.ids.idsuserapp.viewmodel.MappaViewModel;

/**
 * è stato inserto un file json all'interno del package utile a firebase per comunicare con le app.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity"; //variabile di TAG da usare nelle operazioni di logcat
    private MappaViewModel mappaViewModel;
    private BeaconViewModel beaconViewModel;
    private com.android.volley.RequestQueue serverRequestQueue;
    private BeaconDataHandler beaconDataHandler;
    private MappaDataHandler mappaDataHandler;
    private static final int PICKFILE_REQUEST_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beaconViewModel = new BeaconViewModel(getApplication());
        mappaViewModel = new MappaViewModel(getApplication());

        beaconDataHandler = new BeaconDataHandler(this, beaconViewModel);
        mappaDataHandler = new MappaDataHandler(this, mappaViewModel,beaconViewModel);

        handleFilePermissions();

        //controlla se la connessione ad internet è attiva dato l application context,
        //se si allora viene pulita la lista dei beacon e viene aggiornato il dataset
        if (ConnectionChecker.getInstance().isNetworkAvailable(getApplicationContext()))
            getDatasetFromServer();

        //inizializza il fragment dei beacon
        setupBeaconFragment();

        String token  = FirebaseInstanceId.getInstance().getToken(); //token utile alla comunicazione con device singolo per firebase
        Log.d(TAG, "token firebase: " + token); // output nel log debug
    }


    //gestisce i permessi dei file , con output nel log se sono  stati garantiti. Altrimenti vengono effettutate le richieste
    // in app. Questo metodo è chiamato in onCreate.
    private void handleFilePermissions(){
        if (isFilePermissionGranted()) {
            Log.v("File Permission", "Permission is granted");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
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
        cleanBeacon();
        beaconDataHandler.retrieveBeaconDataset();
        mappaDataHandler.retrieveMappeDataset();
    }

    private void cleanBeacon() {
        beaconViewModel.deleteAll();
    }

    private void setupBeaconFragment() {
        BeaconRecyclerFragment fragment = new BeaconRecyclerFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.beaconfragmentcontainer, fragment).commit();
    }
}
