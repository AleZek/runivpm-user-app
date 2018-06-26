package com.ids.idsuserapp;

import android.bluetooth.le.ScanResult;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.db.entity.Tronco;
import com.ids.idsuserapp.threads.LocatorThread;
import com.ids.idsuserapp.utils.BluetoothLocator;
import com.ids.idsuserapp.viewmodel.ArcoViewModel;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;
import com.ids.idsuserapp.wayfinding.Dijkstra;
import com.ids.idsuserapp.wayfinding.Grafo;
import com.ids.idsuserapp.wayfinding.Percorso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PercorsoActivity extends AppCompatActivity implements  BluetoothLocator.LocatorCallbacks {

    TextView locationText;
    BluetoothLocator bluetoothLocator;
    LocatorThread locatorThread;
    Percorso percorso;
    Dijkstra dijkstra;
    Beacon origine;
    Beacon destinazione;
    ArcoViewModel arcoViewModel;
    BeaconViewModel beaconViewModel;
    String userPosition = "";
    Beacon userPositionBeacon;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percorso);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        locationText = findViewById(R.id.locationtext);

        beaconViewModel = new BeaconViewModel(getApplication());
        arcoViewModel = new ArcoViewModel(getApplication());

        setOrigineDestinazione();
        setSupportActionBar(toolbar);

        startLocatorThread();
        setBluetoothLocator();

        setDijkstra();
        percorso = dijkstra.ricerca(destinazione);
        Log.v("percorso", percorso.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locatorThread.interrupt();
    }

    private void startLocatorThread() {
        locatorThread = new LocatorThread(this, LocatorThread.NAVIGATION_MODE);
        locatorThread.start();
    }

    private void setBeaconWhiteList() {
        List<Beacon> beaconList = beaconViewModel.getAllSynchronous();
        bluetoothLocator.setBeaconWhiteList(beaconList);
    }

    private void setOrigineDestinazione() {
        String nomeOrigine = getIntent().getExtras().getString("origine");
        String nomeDestinazione = getIntent().getExtras().getString("destinazione");
        origine = beaconViewModel.findByName(nomeOrigine);
        destinazione = beaconViewModel.findByName(nomeDestinazione);
    }

    private void setDijkstra() {
        List<Tronco> tronchi = arcoViewModel.getTronchi();
        Grafo grafo = new Grafo(tronchi);

        dijkstra = new Dijkstra();
        dijkstra.in(grafo);
        dijkstra.inizio(origine);
        dijkstra.setNormalizationBasis(1.0);
    }

    private void setBluetoothLocator() {
        bluetoothLocator = locatorThread.getBluetoothLocator();
        setBeaconWhiteList();
    }

    private void setUserBeacon() {
        userPositionBeacon = beaconViewModel.findByDevice(userPosition);
    }

    // index = -1 indica che il beacon non era nel percorso indicato e che ho sbagliato strada, quindi ricalcolo

    public void nextStep() {
        int index = percorso.indexOf(userPositionBeacon);
        if(userPositionBeacon!= null) {
            if (userPositionBeacon.equals(destinazione)) {
                Toast.makeText(this, "Sei giunto a destinazione", Toast.LENGTH_LONG).show();
                locatorThread.interrupt();
                bluetoothLocator.stopScan();
            } else if (index != -1) {
                Beacon nextBeacon = percorso.get(index + 1);
                Toast.makeText(this, "Prosegui verso " + nextBeacon.getNome(), Toast.LENGTH_LONG).show();
//                getIndicazioni(userPositionBeacon, nextBeacon);
            } else {
                dijkstra.inizio(userPositionBeacon);
                dijkstra.ricerca(destinazione);
                Beacon nextBeacon = percorso.get(1);
                Toast.makeText(this, "Prosegui verso " + nextBeacon.getNome(), Toast.LENGTH_LONG).show();
//            getIndicazioni(userPositionBeacon, nextBeacon);
            }
        }
    }

    private void setCurrentPosition() {
        userPosition = (String) bluetoothLocator.getStrongestBeacon().get("nome");
        setUserBeacon();
        locationText.setText(userPosition);
    }

    @Override
    public void sendCurrentPosition(String device) {
        if(bluetoothLocator.isBeacon(device)) {
            setCurrentPosition();
            nextStep();
        }
    }
}