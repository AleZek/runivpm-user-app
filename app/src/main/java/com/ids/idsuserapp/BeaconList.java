package com.ids.idsuserapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.android.volley.toolbox.Volley;
import com.ids.idsuserapp.adapters.BeaconRecyclerAdapter;
import com.ids.idsuserapp.entityhandlers.BeaconDataHandler;
import com.ids.idsuserapp.fragment.BeaconRecyclerFragment;
import com.ids.idsuserapp.utils.ConnectionChecker;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;
import com.ids.idsuserapp.wayfinding.PercorsoFragment;

public class BeaconList extends FragmentActivity {

    private BeaconViewModel beaconViewModel;
    private com.android.volley.RequestQueue serverRequestQueue;
    private Integer idMappa;
    private BeaconDataHandler beaconDataHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        idMappa = getIntent().getIntExtra("idMappa",0);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_beac1on_list);
        beaconViewModel = new BeaconViewModel(getApplication());
        serverRequestQueue = Volley.newRequestQueue(this);
        beaconDataHandler = new BeaconDataHandler(this, beaconViewModel);

        if (ConnectionChecker.getInstance().isNetworkAvailable(getApplicationContext()))
            getDatasetFromServer();
        setupBeaconFragment();



    }



    //TODO mettere nella prima activity dell'app la parte relativa al recupero dati
    private void getDatasetFromServer() {
      //  cleanBeacon();
        beaconDataHandler.retrieveBeaconDataset();
    }




    private void cleanBeacon() {
   //     beaconViewModel.deleteAll();
    }

    private void setupBeaconFragment() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BeaconRecyclerFragment fragment = new BeaconRecyclerFragment();
        fragmentTransaction.add(R.id.beaconfragmentcontainer, fragment).commit();
    }












/*
    @Override
    public void creaBeacon(String nome,  String quota, double larghezza, int x, int y, int x_meter, int y_meter, int mappa) {
        beaconDataHandler.creaBeacon(nome,quota,larghezza,x,y,x_meter,y_meter,idMappa);
    }

    @Override
    public void creaBeaconServer(String nome,  String quota, double larghezza, int x, int y, int x_meter, int y_meter, String mappa) {
        beaconDataHandler.creaBeaconServer(nome,quota,larghezza,x,y,x_meter,y_meter,"/api/mappas/"+idMappa);

    }

    @Override
    public void modificaBeacon(final int idBeacon, String nomeBeacon,  String quota, double larghezza, int x, int y, int x_meter, int y_meter){
        beaconDataHandler.putBeacon(idBeacon,nomeBeacon, quota,larghezza,x,y,x_meter,y_meter, "/api/mappas/"+idMappa);
    }
*/
}
