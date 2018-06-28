package com.ids.idsuserapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ids.idsuserapp.db.entity.Tronco;
import com.ids.idsuserapp.entityhandlers.ArcoDataHandler;
import com.ids.idsuserapp.entityhandlers.BeaconDataHandler;
import com.ids.idsuserapp.entityhandlers.DataRetriever;
import com.ids.idsuserapp.entityhandlers.MappaDataHandler;
import com.ids.idsuserapp.fragment.BeaconRecyclerFragment;
import com.ids.idsuserapp.services.LocatorService;
import com.ids.idsuserapp.threads.LocatorThread;
import com.ids.idsuserapp.utils.BluetoothLocator;
import com.ids.idsuserapp.utils.ConnectionChecker;
import com.ids.idsuserapp.viewmodel.ArcoViewModel;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;
import com.ids.idsuserapp.viewmodel.MappaViewModel;
import com.ids.idsuserapp.wayfinding.Grafo;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements DataRetriever{
    private android.support.v7.widget.SearchView origineSearchView;
    private android.support.v7.widget.SearchView destinazioneSearchView;
    private MappaViewModel mappaViewModel;
    private BeaconViewModel beaconViewModel;
    private ArcoViewModel arcoViewModel;
    private BeaconDataHandler beaconDataHandler;
    private MappaDataHandler mappaDataHandler;
    private ArcoDataHandler arcoDataHandler;
    private LocatorThread locatorThread;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 124;
    private static final int REQUEST_ENABLE_BT = 125;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        setupViewModels();
        setupDataHandlers();
        handleFilePermissions();
        handleLocationPermissions();
        handleBtAdapter();
//        startLocatorThread();

        //controlla se la connessione ad internet è attiva dato l application context,
        //se si allora viene pulita la lista dei beacon e viene aggiornato il dataset
        if (ConnectionChecker.getInstance().isNetworkAvailable(getApplicationContext()))
            getDatasetFromServer();

        List<Tronco> tronchi = arcoViewModel.getTronchi();

        Grafo grafo = new Grafo(tronchi);

        //inizializza il fragment dei beacon
        setupBeaconFragment();
//        startLocatorService();

    }

    private void startLocatorService() {
        Intent serviceIntent = new Intent(this, LocatorService.class);
        startService(serviceIntent);
    }

    private void setupDataHandlers() {
        beaconDataHandler = new BeaconDataHandler(this, beaconViewModel);
        mappaDataHandler = new MappaDataHandler(this, mappaViewModel,beaconViewModel);
        arcoDataHandler = new ArcoDataHandler(this, arcoViewModel, beaconViewModel);
    }

    private void setupViewModels() {
        beaconViewModel = new BeaconViewModel(getApplication());
        mappaViewModel = new MappaViewModel(getApplication());
        arcoViewModel = new ArcoViewModel(getApplication());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        locatorThread.interrupt();
    }

    private void startLocatorThread() {
        locatorThread = new LocatorThread(this, LocatorThread.STANDARD_MODE);
        locatorThread.start();
        BluetoothLocator threadBtLocator =locatorThread.getBluetoothLocator();
        threadBtLocator.setBeaconWhiteList(beaconViewModel.getAllSynchronous());

    }



    private void handleBtAdapter() {
        BluetoothManager bluetoothManager = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = null;
        if (bluetoothManager != null) {
            bluetoothAdapter = bluetoothManager.getAdapter();
        }
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent =
                    new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
        }
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

    private void setupBeaconFragment() {
        BeaconRecyclerFragment fragment = new BeaconRecyclerFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.beaconfragmentcontainer, fragment).commit();
        origineSearchView = fragment.getSearchViewPartenza();
        destinazioneSearchView = fragment.getSearchViewDestinazione();

    }

    private void handleLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check 
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Questa applicazione ha bisogno dei permessi relativi alla Posizione");
                builder.setMessage("Consentire l'accesso alla posizione.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }
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
