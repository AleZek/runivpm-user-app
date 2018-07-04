package com.ids.idsuserapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.ids.idsuserapp.entityhandlers.ArcoDataHandler;
import com.ids.idsuserapp.entityhandlers.BeaconDataHandler;
import com.ids.idsuserapp.entityhandlers.DataRetriever;
import com.ids.idsuserapp.entityhandlers.MappaDataHandler;
import com.ids.idsuserapp.percorso.BaseFragment;
import com.ids.idsuserapp.percorso.HomeFragment;
import com.ids.idsuserapp.services.LocatorService;
import com.ids.idsuserapp.threads.LocatorThread;
import com.ids.idsuserapp.utils.ConnectionChecker;
import com.ids.idsuserapp.utils.PermissionsUtil;
import com.ids.idsuserapp.viewmodel.ArcoViewModel;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;
import com.ids.idsuserapp.viewmodel.MappaViewModel;

public class HomeActivity extends AppCompatActivity implements DataRetriever{
    public static final String TAG = HomeActivity.class.getSimpleName();
    private MappaViewModel mappaViewModel;
    private BeaconViewModel beaconViewModel;
    private ArcoViewModel arcoViewModel;
    private BeaconDataHandler beaconDataHandler;
    private MappaDataHandler mappaDataHandler;
    private ArcoDataHandler arcoDataHandler;
    private PermissionsUtil permissionsUtil;
    private static final int BT_ENABLED = 1;
    private boolean offline;
    public static final String OFFLINE_USAGE = "offline_usage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupMessageReception(savedInstanceState);
        setupViewModels();
        setupDataHandlers();
//        startLocatorThread();

        //controlla se la connessione ad internet è attiva dato l application context,
        //se si allora viene pulita la lista dei beacon e viene aggiornato il dataset
        if (ConnectionChecker.getInstance().isNetworkAvailable(getApplicationContext()))
            getDatasetFromServer();
        permissionsUtil = new PermissionsUtil(this);
        if(permissionsUtil.requestEnableBt())
            startLocatorService(LocatorThread.STANDARD_MODE);
    }

    private void setupMessageReception(Bundle savedInstanceState) {
        offline = true;

         /* if (!offline) {
          // Handle deviceToken for pushNotification
            // [START handle_device_token]
            SaveDeviceTokenTask task = new SaveDeviceTokenTask(this, new TaskListener<Void>() {
                @Override
                public void onTaskSuccess(Void aVoid) {
                    Log.d(TAG, "Device key save succesfully");
                }

                @Override
                public void onTaskError(Exception e) {
                    Log.e(TAG, "Save deviceKey error", e);
                }

                @Override
                public void onTaskComplete() {
                }

                @Override
                public void onTaskCancelled() {
                }
            });
            task.execute();
            // [END handle_device_token]
        }*/

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        boolean emergency = false;
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                if (key.equals("emergency")) {
                    emergency = true;
                }
                String value = getIntent().getExtras().getString(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]

        if (savedInstanceState == null) {
            HomeFragment homeFragment = HomeFragment.newInstance(emergency, offline);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.navigation_content_pane, homeFragment, HomeFragment.TAG)
                    .commit();
        }

        //segmento di codice utile all unlock automaitico
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                + WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

    }

    private void startLocatorService(int mode) {
        Intent serviceIntent = new Intent(this, LocatorService.class);
        serviceIntent.setAction(Integer.toString(mode));
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
        Intent intent = new Intent(this, LocatorService.class);
        stopService(intent);
    }



    private void getDatasetFromServer() {
        cleanDb();
        mappaDataHandler.retrieveMappeDataset();
    }

    private void cleanDb() {
        beaconViewModel.deleteAll();
        arcoViewModel.deleteAll();
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


    /**
     * Cambia il fragment
     *
     * @param fragment
     */
    public void changeFragment(BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.navigation_content_pane, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(fragment.TAG)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case BT_ENABLED:
                if(resultCode == RESULT_OK)
                    startLocatorService(LocatorThread.STANDARD_MODE);
              else
                  permissionsUtil.btAlert();
              break;
        }
    }
}
