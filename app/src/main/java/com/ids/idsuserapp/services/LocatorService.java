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
    private final Handler handler = new Handler();

    public LocatorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serverUserLocator = new ServerUserLocator(getApplication());
//        locatorAsyncTask = new LocatorAsyncTask(this);

//        setBeaconWhiteList();
    }

    private void startLocatorThread() {
        locatorThread = new LocatorThread(this, LocatorThread.STANDARD_MODE);
        locatorThread.start();
    }

    private void setBeaconWhiteList() {
//        BeaconViewModel beaconViewModel = new BeaconViewModel(getApplication());
//        List<Beacon> beacons = beaconViewModel.getAllSynchronous();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//        locatorAsyncTask.execute(STANDARD_MODE);
        startLocatorThread();
        return START_STICKY;
    }



//    private static class LocatorAsyncTask extends AsyncTask<Integer, Void, Void> {
//
//        private BluetoothLocator bluetoothLocator;
//
//        boolean running = false;
//
//
//        LocatorAsyncTask(Context context) {
//            bluetoothLocator = new BluetoothLocator(context);
//        }
//
//        @Override
//        protected Void doInBackground(final Integer... params) {
//            running = true;
//            bluetoothLocator.setupLocatorScanner();
//            bluetoothLocator.startScan();
////            while(running){
////                bluetoothLocator.startScan();
////
////                try {
////                    TimeUnit.MILLISECONDS.sleep(params[0]);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////            }
//            return null;
//        }
//
//    }

    @Override
    public void sendCurrentPosition(String device) {
        serverUserLocator.sendPosition(device);
        Log.v("locator", "callback chiamata");
    }

    //
//    public class LocalBinder extends Binder {
//        public LocatorServiceold7 getService() {
//            return LocatorServiceold7.this;
//        }
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return mBinder;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//
//// write your code to post content on server
//            }
//        });
//        return android.app.Service.START_STICKY;
//    }
}
