package com.ids.idsuserapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.percorso.BaseFragment;
import com.ids.idsuserapp.percorso.HomeFragment;
import com.ids.idsuserapp.percorso.Tasks.TaskListener;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;

import org.apache.commons.lang3.SerializationUtils;

public class HomeActivity extends AppCompatActivity {
    public static final String TAG = HomeActivity.class.getSimpleName();

    private Button selezionaMappaOrigineButton;
    private Button selezionaMappaDestinazioneButton;
    private Button selezionaBeaconOrigineButton;
    private Button selezionaBeaconDestinazioneButton;

    private View mappaContainer;

    private View selezionaMappaOrigineLayout;
    private View selezionaBeaconOrigineLayout;
    private View selezionaMappaDestinazioneLayout;
    private View selezionaBeaconDestinazioneLayout;

    private Button selezionaOrigineButton;
    private Button selezionaDestinazioneButton;
    private BeaconViewModel mBeaconViewModel;

    public static final int ORIGIN_SELECTION_REQUEST_CODE = 200;
    public static final int DESTINATION_SELECTION_REQUEST_CODE = 201;

    private boolean visible = false;
    private boolean choosenOrigin=false;
    private boolean choosenDestination = false;
    private boolean offline;


    private static Beacon origin = null, destination = null;
    public static final String INTENT_KEY_POSITION = "position";
    private int indexOfPathSelected;
    public static final String OFFLINE_USAGE = "offline_usage";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        Log.d(TAG, String.valueOf(emergency));
        if (savedInstanceState == null) {
            HomeFragment homeFragment = HomeFragment.newInstance(emergency, offline);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.navigation_content_pane, homeFragment, HomeFragment.TAG)
                    .commit();
        }
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


}







