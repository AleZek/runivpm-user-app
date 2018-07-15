package com.ids.idsuserapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.ids.idsuserapp.authentication.AutenticationActivity;
import com.ids.idsuserapp.entityhandlers.ArcoDataHandler;
import com.ids.idsuserapp.entityhandlers.BeaconDataHandler;
import com.ids.idsuserapp.entityhandlers.DataRetriever;
import com.ids.idsuserapp.entityhandlers.MappaDataHandler;
import com.ids.idsuserapp.entityhandlers.UserRequestHandler;
import com.ids.idsuserapp.percorso.BaseFragment;
import com.ids.idsuserapp.percorso.HomeFragment;
import com.ids.idsuserapp.services.LocatorService;
import com.ids.idsuserapp.services.NodesUpdateService;
import com.ids.idsuserapp.threads.LocatorThread;
import com.ids.idsuserapp.utils.ConnectionChecker;
import com.ids.idsuserapp.utils.PermissionsUtil;
import com.ids.idsuserapp.viewmodel.ArcoViewModel;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;
import com.ids.idsuserapp.viewmodel.MappaViewModel;

public class HomeActivity extends AppCompatActivity implements DataRetriever {
    public static final String TAG = HomeActivity.class.getSimpleName();
    private MappaViewModel mappaViewModel;
    private BeaconViewModel beaconViewModel;
    private ArcoViewModel arcoViewModel;
    private BeaconDataHandler beaconDataHandler;
    private MappaDataHandler mappaDataHandler;
    private ArcoDataHandler arcoDataHandler;
    private PermissionsUtil permissionsUtil;
    private LocatorThread locatorThread;
    private boolean emergency;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 124;
    private static final int BT_ENABLED = 1;
    private boolean offline;
    public static final String OFFLINE_USAGE = "offline_usage";




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        setupViewModels();
        setupDataHandlers();
        checkOfflineMode();

        emergency = checkEmergency();
        if(emergency) {
            overrideUnlockScreen();
        }
        if (!offline && ConnectionChecker.getInstance().isNetworkAvailable(getApplicationContext()) && !getIntent().hasExtra("stop"))
            getDatasetFromServer();
        permissionsUtil = new PermissionsUtil(this);
        if (permissionsUtil.requestEnableBt())
            startLocatorService(LocatorThread.STANDARD_MODE);
        setupMessageReception(savedInstanceState);

        if (savedInstanceState == null) {
            HomeFragment homeFragment = HomeFragment.newInstance(emergency, offline);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.navigation_content_pane, homeFragment, HomeFragment.TAG)
                    .commit();
        }
    }


    private void overrideUnlockScreen() {
        //segmento di codice utile all unlock automaitico
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                + WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    private boolean checkEmergency() {
        return emergency = getIntent().hasExtra("emergency");
    }

    private void checkOfflineMode() {
        offline = getIntent().getBooleanExtra("offline", false);
    }

    private void setupMessageReception(Bundle savedInstanceState) {
        offline = true;

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

        if (savedInstanceState == null) {
            HomeFragment homeFragment = HomeFragment.newInstance(emergency, offline);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.navigation_content_pane, homeFragment, HomeFragment.TAG)
                    .commit();
        }



    }

    private void startLocatorService(int mode) {
        Intent serviceIntent = new Intent(this, LocatorService.class);
        serviceIntent.setAction(Integer.toString(mode));
        startService(serviceIntent);
    }

    private void setupDataHandlers() {
        beaconDataHandler = new BeaconDataHandler(this, beaconViewModel);
        mappaDataHandler = new MappaDataHandler(this, mappaViewModel);
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
        startNodesUpdateService();
        arcoDataHandler.retrieveArchiDataset();
    }

    private void startNodesUpdateService() {
        Intent serviceIntent = new Intent(this, NodesUpdateService.class);
        startService(serviceIntent);
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
        switch (requestCode) {
            case BT_ENABLED:
                if (resultCode == RESULT_OK)
                    startLocatorService(LocatorThread.STANDARD_MODE);
                else
                    permissionsUtil.btAlert();

        }
    }

    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setMessage("Sei sicuro di voler uscire?");
        builder.setCancelable(true);
        builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                UserRequestHandler userRequestHandler = new UserRequestHandler(getApplicationContext());
                userRequestHandler.logoutUserServer();
                Intent intent = new Intent(getApplicationContext(), AutenticationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Exit", true);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
