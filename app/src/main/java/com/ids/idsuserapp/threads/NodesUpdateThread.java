package com.ids.idsuserapp.threads;

import android.app.Application;
import android.content.Context;

import com.ids.idsuserapp.entityhandlers.BeaconDataHandler;
import com.ids.idsuserapp.utils.ConnectionChecker;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;

public class NodesUpdateThread extends Thread {

    private BeaconDataHandler beaconDataHandler;
    private Context context;
    private boolean running;
    private static int SLEEP_TIME = 40000;

    public NodesUpdateThread(Context context){
        this.context = context;
        BeaconViewModel beaconViewModel = new BeaconViewModel((Application) context.getApplicationContext());
        beaconDataHandler = new BeaconDataHandler(context, beaconViewModel);
    }

    @Override
    public void run() {
        super.run();
        running = true;

        while(running){
            if(ConnectionChecker.getInstance().isNetworkAvailable(context))
                beaconDataHandler.retrieveBeaconDataset();
            try {
                sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }
}
