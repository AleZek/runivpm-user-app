package com.ids.idsuserapp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.ids.idsuserapp.db.dao.MappaDao;
import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.db.entity.Mappa;
import com.ids.idsuserapp.entityhandlers.ServerUserLocator;
import com.ids.idsuserapp.threads.LocatorThread;
import com.ids.idsuserapp.utils.BluetoothLocator;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LocatorService extends Service implements BluetoothLocator.LocatorCallbacks {
    LocatorThread locatorThread;
    ServerUserLocator serverUserLocator;
    public static int STANDARD_MODE = 10000;

    public LocatorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serverUserLocator = new ServerUserLocator(getApplication());
        locatorThread = new LocatorThread(this, LocatorThread.STANDARD_MODE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        locatorThread.start();
        return START_STICKY;
    }

    @Override
    public void sendCurrentPosition(String device) {
        serverUserLocator.sendPosition(device);
        Log.v("locator", "callback chiamata");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locatorThread.interrupt();
    }
}
