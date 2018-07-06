package com.ids.idsuserapp;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.db.entity.Tronco;
import com.ids.idsuserapp.fragment.NavigatorFragment;
import com.ids.idsuserapp.percorso.BaseFragment;
import com.ids.idsuserapp.percorso.NavigationActivity;
import com.ids.idsuserapp.percorso.Tasks.MinimumPathTask;
import com.ids.idsuserapp.percorso.Tasks.TaskListener;
import com.ids.idsuserapp.percorso.views.MapView;
import com.ids.idsuserapp.percorso.views.exceptions.DestinationNotSettedException;
import com.ids.idsuserapp.percorso.views.exceptions.OriginNotSettedException;
import com.ids.idsuserapp.threads.LocatorThread;
import com.ids.idsuserapp.utils.BluetoothLocator;
import com.ids.idsuserapp.viewmodel.ArcoViewModel;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;
import com.ids.idsuserapp.wayfinding.Dijkstra;
import com.ids.idsuserapp.wayfinding.Grafo;
import com.ids.idsuserapp.wayfinding.IndiciNavigazione;
import com.ids.idsuserapp.wayfinding.Percorso;
import com.ids.idsuserapp.wayfinding.PercorsoMultipiano;

import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

public class PercorsoActivity extends AppCompatActivity implements BluetoothLocator.LocatorCallbacks {
    public static final String TAG = PercorsoActivity.class.getSimpleName();

    TextView locationText;
    BluetoothLocator bluetoothLocator;
    LocatorThread locatorThread;
    Dijkstra dijkstra;
    Beacon origine;
    Beacon destinazione;
    ArcoViewModel arcoViewModel;
    BeaconViewModel beaconViewModel;

    private List<Percorso> solutionPaths = null;
    private Percorso selectedSolution;
    private boolean emergency = false;
    private boolean offline = true;
    private IndiciNavigazione indiciNavigazione;


    public ViewHolderPercorso holder;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percorso);

        locationText = findViewById(R.id.locationtext);


        beaconViewModel = new BeaconViewModel(getApplication());
        arcoViewModel = new ArcoViewModel(getApplication());

        setOrigineDestinazione(getIntent());
        holder = new ViewHolderPercorso();
        holder.setupMapView();

        startLocatorThread();
        setBluetoothLocator();

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


    private void setOrigineDestinazione(Intent data) {
        byte[] serializedDataOrigine;
        byte[] serializedDataDestinazione;
        try {
            serializedDataOrigine = data.getByteArrayExtra("beaconOrigine");
            serializedDataDestinazione = data.getByteArrayExtra("beaconDestinazione");
            if (serializedDataOrigine == null || serializedDataDestinazione == null) {
                throw new NullPointerException("Null array data");
            }
            origine = (Beacon) SerializationUtils.deserialize(serializedDataOrigine);
            destinazione = (Beacon) SerializationUtils.deserialize(serializedDataDestinazione);
        } catch (NullPointerException ee) {
            Log.e(TAG, "NullPointer", ee);
        }
    }

    private void setDijkstra() {
        List<Tronco> tronchi = arcoViewModel.getTronchi(); //classi con un arco e due beacon
        Grafo grafo = new Grafo(tronchi);

        dijkstra = new Dijkstra();
        dijkstra.in(grafo);
        dijkstra.inizio(origine);
        dijkstra.setNormalizationBasis(1.0);
    }

    private void setBluetoothLocator() {
        bluetoothLocator = locatorThread.getBluetoothLocator();
    }


    // index = -1 indica che il beacon non era nel percorso indicato e che ho sbagliato strada, quindi ricalcolo

    public void nextStepBeacon(Beacon currentBeacon) {
        int index = selectedSolution.indexOf(currentBeacon);
        if (selectedSolution != null) {
            if (currentBeacon.equals(destinazione)) {
                Toast.makeText(this, "Sei giunto a destinazione", Toast.LENGTH_LONG).show();
                locatorThread.interrupt();
                bluetoothLocator.stopScan();
            } else if (index != -1) {
                indiciNavigazione = new IndiciNavigazione(selectedSolution.indexOf(currentBeacon), selectedSolution.indexOf(currentBeacon) + 1);
                holder.launchSearchPathTask(currentBeacon);
            } else {
                selectedSolution = null;
                holder.launchSearchPathTask(currentBeacon);
//            getIndicazioni(userPositionBeacon, nextBeacon);
            }
        }
    }

    private void setCurrentPosition(String device) {
        Beacon location = beaconViewModel.findByDevice(device);
        nextStepBeacon(location);

    }

    @Override
    public void sendCurrentPosition(BluetoothDevice device) {
        //Testing con device
        ArrayList<String> devices = new ArrayList<>();
        if (bluetoothLocator.isBeacon(device) || devices.indexOf(device) != -1) {
            Log.v("ooo", "no beacon");
            setCurrentPosition(device.toString());
            bluetoothLocator.getStrongestBeacon().put("scanNumber", 0);
        }
    }


    //segmento di codice utile all unlock automaitico
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
//                + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
//                + WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


    //questo metodo permette alla app di sottoscriversi al topic emergenza, questo permette a firebase
    // di mandare messaggi broadcast alle istanze della app
//    private void subscribeTopic(final String topic){
//        FirebaseMessaging.getInstance().subscribeToTopic(topic)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "Sottoscrizione avvenuta a ";
//                        if (!task.isSuccessful()) {
//                            msg = "sottoscrizione fallita a ";
//                        }
//                        Log.d(TAG, msg + topic); // sono mostrati dei messaggi nel log e nella app se la sottoscrizione avviene o meno
//                        Toast.makeText(MainActivity.this, msg + topic, Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//    }


    // @TODO Esternalizzare
    private class MinimumPathListener implements TaskListener<List<Percorso>> {
        @Override
        public void onTaskSuccess(List<Percorso> searchResult) {
            solutionPaths = searchResult;
            if (selectedSolution == null)
                selectedSolution = new Percorso(solutionPaths.get(0));
            Percorso pathToDraw = new Percorso(selectedSolution.subList(indiciNavigazione.current, selectedSolution.size() - 1));
            PercorsoMultipiano multiFloorSolution = pathToDraw.toMultiFloorPath();

            try {
                holder.mapView.drawRoute(multiFloorSolution);
            } catch (OriginNotSettedException | DestinationNotSettedException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onTaskError(Exception e) {
            Log.e(TAG, "Errore nel calcolo del percorso minimo", e);
        }

        @Override
        public void onTaskComplete() {
        }

        @Override
        public void onTaskCancelled() {

        }
    }

    private class NavigationButtonAvantiListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (!holder.fabButtonIndietro.isClickable()) {
                holderButtonEnabled("Indietro", true);
            }
            indiciNavigazione = new IndiciNavigazione(indiciNavigazione.next, indiciNavigazione.next + 1);
            holder.setupMapView();
        }
    }

    private void holderButtonEnabled(String bottone, boolean stato) {
        switch (bottone) {
            case "Indietro":
                holder.fabButtonIndietro.setEnabled(stato);
                holder.fabButtonIndietro.setClickable(stato);
                break;
            case "Avanti":
                holder.fabButtonAvanti.setEnabled(stato);
                holder.fabButtonAvanti.setClickable(stato);
                break;
        }
    }

    private class NavigationButtonIndietroListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (!holder.fabButtonAvanti.isClickable()) {
                holderButtonEnabled("Avanti", true);
            }
            indiciNavigazione = new IndiciNavigazione(indiciNavigazione.current - 1, indiciNavigazione.current);
            holder.setupMapView();
        }
    }

    private void openNavigatorFragment() {
        NavigatorFragment navigatorFragment = NavigatorFragment.newInstance(selectedSolution, emergency, offline);
        NavigationActivity navActivity = new NavigationActivity();
        navActivity.changeFragment(navigatorFragment);
    }


    public class ViewHolderPercorso extends BaseFragment.ViewHolder {
        public final MapView mapView;
        public final FloatingActionButton fabButtonAvanti;
        public final FloatingActionButton fabButtonIndietro;


        public ViewHolderPercorso() {
            fabButtonAvanti = findViewById(R.id.navigation_fab_avanti);
            fabButtonIndietro = findViewById(R.id.navigation_fab_indietro);
            fabButtonAvanti.setOnClickListener(new NavigationButtonAvantiListener());
            fabButtonIndietro.setOnClickListener(new NavigationButtonIndietroListener());

            mapView = findViewById(R.id.navigation_map_image_percorso);


        }

        public void setupMapView() {
            Beacon currentBeacon;
            if (selectedSolution != null) {
                if (indiciNavigazione.current == 0) {
                    holderButtonEnabled("Indietro", false);
                }
                if (indiciNavigazione.current >= selectedSolution.size() - 2) {
                    holderButtonEnabled("Avanti", false);
                }
                currentBeacon = selectedSolution.get(indiciNavigazione.current);
                if (currentBeacon != destinazione)
                    launchSearchPathTask(currentBeacon);
            } else {
                firstPathSearch();
            }
        }

        public void firstPathSearch() {
            indiciNavigazione = new IndiciNavigazione(0, 1);
            launchSearchPathTask(origine);
            holderButtonEnabled("Indietro", false);
        }

        private void launchSearchPathTask(Beacon currentBeacon) {
            MinimumPathTask minimumPathTask = new MinimumPathTask(getBaseContext(), new MinimumPathListener(), arcoViewModel);
            minimumPathTask.execute(currentBeacon, destinazione);
        }
    }


}