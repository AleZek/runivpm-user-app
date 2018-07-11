package com.ids.idsuserapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ids.idsuserapp.entityhandlers.BeaconDataHandler;
import com.ids.idsuserapp.entityhandlers.DataRetriever;
import com.ids.idsuserapp.threads.NodesUpdateThread;

public class NodesUpdateService extends Service implements DataRetriever {
    NodesUpdateThread nodesUpdateThread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        nodesUpdateThread = new NodesUpdateThread(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        nodesUpdateThread.run();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        nodesUpdateThread.interrupt();
    }


    @Override
    public void retrieveBeacons() {

    }

    @Override
    public void retrieveArchi() {

    }
}
