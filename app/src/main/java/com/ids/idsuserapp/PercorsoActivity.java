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
import com.ids.idsuserapp.utils.BluetoothLocator;
import com.ids.idsuserapp.viewmodel.ArcoViewModel;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;
import com.ids.idsuserapp.wayfinding.Dijkstra;
import com.ids.idsuserapp.wayfinding.Grafo;
import com.ids.idsuserapp.wayfinding.Percorso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PercorsoActivity extends AppCompatActivity implements BluetoothLocator.NavigationCallbacks {

    TextView locationText;
    BluetoothLocator bluetoothLocator;
    ArrayList<String> beaconDeviceList;
    Percorso percorso;
    Dijkstra dijkstra;
    Beacon origine;
    Beacon destinazione;
    ArcoViewModel arcoViewModel;
    BeaconViewModel beaconViewModel;
    HashMap strongestBeacon;
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

        setBeaconDeviceList();
        setOrigineDestinazione();
        setSupportActionBar(toolbar);

        initializeStrongestBeacon();
        setBluetoothLocator();

        setDijkstra();
        percorso = dijkstra.ricerca(destinazione);
        Log.v("percorso", percorso.toString());
    }

    private void setBtAdapter() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setBeaconDeviceList() {
        beaconDeviceList = new ArrayList<String>();
        List<Beacon> beacons = beaconViewModel.getAllSynchronous();
        for(Beacon beacon : beacons){
            String currDevice = beacon.getDevice();
            if (!currDevice.equals("null"))
                beaconDeviceList.add(currDevice);
        }
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
        bluetoothLocator = new BluetoothLocator(this);
        bluetoothLocator.setupNavigationScanner();
    }

    private void initializeStrongestBeacon() {
        strongestBeacon = new HashMap<>();
        strongestBeacon.put("nome", "");
        strongestBeacon.put("rssi", -200);
        strongestBeacon.put("scanNumber", 0);
    }

    @Override
    public void readScannedDevice(ScanResult result) {
        int resultRssi = result.getRssi();
        String resultDevice = result.getDevice().toString();
        if(isBeacon(resultDevice)) {
            bluetoothLocator.setStrongestBeacon(resultDevice,resultRssi);
            if(bluetoothLocator.positionChanged())
                setCurrentPosition();
        }
    }

    private boolean isBeacon(String resultDevice) {
            return beaconDeviceList.indexOf(resultDevice) != -1;
    }


    private void setUserBeacon() {
        userPositionBeacon = beaconViewModel.findByDevice(userPosition);
    }

    // index = -1 indica che il beacon non era nel percorso indicato e che ho sbagliato strada, quindi ricalcolo
    @Override
    public void nextStep() {
        int index = percorso.indexOf(userPositionBeacon);
        if (userPositionBeacon!= null && userPositionBeacon.equals(destinazione)) {
            Toast.makeText(this, "Sei giunto a destinazione", Toast.LENGTH_LONG).show();
            bluetoothLocator.stopScan();
        }
        else if (index != -1) {
                Beacon nextBeacon = percorso.get(index + 1);
                Toast.makeText(this,"Prosegui verso " + nextBeacon.getNome(), Toast.LENGTH_LONG).show();
//                getIndicazioni(userPositionBeacon, nextBeacon);
        } else {
            dijkstra.inizio(userPositionBeacon);
            dijkstra.ricerca(destinazione);
            Beacon nextBeacon = percorso.get(1);
            Toast.makeText(this,"Prosegui verso " + nextBeacon.getNome(), Toast.LENGTH_LONG).show();
//            getIndicazioni(userPositionBeacon, nextBeacon);
        }
    }

    private void setCurrentPosition() {
        userPosition = (String) bluetoothLocator.getStrongestBeacon().get("nome");
        setUserBeacon();
        locationText.setText(userPosition);
//        locationText.
    }



}