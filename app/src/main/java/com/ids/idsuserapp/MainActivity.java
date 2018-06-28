package com.ids.idsuserapp;

import android.content.Intent;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import com.ids.idsuserapp.entityhandlers.ArcoDataHandler;
import com.ids.idsuserapp.entityhandlers.BeaconDataHandler;
import com.ids.idsuserapp.entityhandlers.DataRetriever;
import com.ids.idsuserapp.entityhandlers.MappaDataHandler;

import com.ids.idsuserapp.utils.ConnectionChecker;
import com.ids.idsuserapp.viewmodel.ArcoViewModel;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;
import com.ids.idsuserapp.viewmodel.MappaViewModel;

public class MainActivity extends AppCompatActivity implements DataRetriever {
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

        if (ConnectionChecker.getInstance().isNetworkAvailable(getApplicationContext()))
            getDatasetFromServer();

        Intent logoIntent = new Intent(MainActivity.this,LogoActivity.class);
        startActivity(logoIntent);


}


    private void getDatasetFromServer() {
        cleanArchi();
        cleanBeacon();
        cleanMappe();
        mappaDataHandler.retrieveMappeDataset();
    }

    private void cleanBeacon() {
        beaconViewModel.deleteAll();
    }
    private void cleanArchi(){
        arcoViewModel.deleteAll();
    }
    private void cleanMappe(){
        mappaViewModel.deleteAll();
    }

    @Override
    public void retrieveBeacons() {
        beaconDataHandler.retrieveBeaconDataset();
    }

    @Override
    public void retrieveArchi() {
        arcoDataHandler.retrieveArchiDataset();
    }

}
