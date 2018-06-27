package com.ids.idsuserapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

public class LocatorService extends Service {
    public LocatorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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
