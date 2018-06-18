package com.ids.idsuserapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ids.idsuserapp.db.entity.Tronco;
import com.ids.idsuserapp.entityhandlers.ArcoDataHandler;
import com.ids.idsuserapp.entityhandlers.BeaconDataHandler;
import com.ids.idsuserapp.entityhandlers.MappaDataHandler;
import com.ids.idsuserapp.fragment.BeaconRecyclerFragment;
import com.ids.idsuserapp.utils.ConnectionChecker;
import com.ids.idsuserapp.viewmodel.ArcoViewModel;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;
import com.ids.idsuserapp.viewmodel.MappaViewModel;
import com.ids.idsuserapp.wayfinding.Grafo;

import java.util.List;

public class MainActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_main);

        beaconViewModel = new BeaconViewModel(getApplication());
        mappaViewModel = new MappaViewModel(getApplication());
        arcoViewModel = new ArcoViewModel(getApplication());

        beaconDataHandler = new BeaconDataHandler(this, beaconViewModel);
        mappaDataHandler = new MappaDataHandler(this, mappaViewModel,beaconViewModel);
        arcoDataHandler = new ArcoDataHandler(this, arcoViewModel, beaconViewModel);
       // handleFilePermissions();




        Intent logoIntent = new Intent(MainActivity.this,LogoActivity.class);
        startActivity(logoIntent);




       /* //controlla se la connessione ad internet è attiva dato l application context,
        //se si allora viene pulita la lista dei beacon e viene aggiornato il dataset
        if (ConnectionChecker.getInstance().isNetworkAvailable(getApplicationContext()))
            getDatasetFromServer();

        List<Tronco> tronchi = arcoViewModel.getTronchi();

        Grafo grafo = new Grafo(tronchi);

        //inizializza il fragment dei beacon
        setupBeaconFragment();
    }*/


    //gestisce i permessi dei file , con output nel log se sono  stati garantiti. Altrimenti vengono effettutate le richieste
    // in app. Questo metodo è chiamato in onCreate.
   /* private void handleFilePermissions(){
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
        mappaDataHandler.retrieveMappeDataset();
        beaconDataHandler.retrieveBeaconDataset();
        arcoDataHandler.retrieveArchiDataset();
    }

    private void cleanBeacon() {
        beaconViewModel.deleteAll();
    }

    private void setupHomeFragment(){

    }
   /*private void setupBeaconFragment() {
        BeaconRecyclerFragment fragment = new BeaconRecyclerFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.beaconfragmentcontainer, fragment).commit();
    }
*/




}}
