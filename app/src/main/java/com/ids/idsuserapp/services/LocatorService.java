package com.ids.idsuserapp.services;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.ids.idsuserapp.entityhandlers.ServerUserLocator;
import com.ids.idsuserapp.threads.LocatorThread;
import com.ids.idsuserapp.utils.BluetoothLocator;

public class LocatorService extends Service implements BluetoothLocator.LocatorCallbacks {
    LocatorThread locatorThread;
    ServerUserLocator serverUserLocator;
    int mode;

    public LocatorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serverUserLocator = new ServerUserLocator(getApplication());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mode = Integer.parseInt(intent.getAction());
        locatorThread = new LocatorThread(this, LocatorThread.STANDARD_MODE);
        locatorThread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locatorThread.interrupt();
    }

    @Override
    public void sendCurrentPosition(BluetoothDevice device) {
        serverUserLocator.sendPosition(device.toString());
        Log.v("locator", "callback chiamata");
    }


}
