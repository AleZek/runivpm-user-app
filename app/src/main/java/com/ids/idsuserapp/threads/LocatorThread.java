package com.ids.idsuserapp.threads;

import android.content.Context;
import android.util.Log;

import com.ids.idsuserapp.utils.BluetoothLocator;

public class LocatorThread extends Thread {
    public BluetoothLocator bluetoothLocator;
    boolean running = false;
    private int mode;
    public static int STANDARD_MODE = 300000;
    public static int NAVIGATION_MODE = 10000;
    public static int EMERGENCY_MODE = 3000;
    static String TAG = "LocatorThread";

    public LocatorThread(Context context, int mode) {
        bluetoothLocator = new BluetoothLocator(context);
        this.mode = mode;
    }

    public BluetoothLocator getBluetoothLocator() {
        return bluetoothLocator;
    }

    @Override
    public void run() {
        super.run();
        running = true;
        bluetoothLocator.setupLocatorScanner();
        while(running){
            bluetoothLocator.startScan();
            try {
                sleep(mode);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void interrupt() {
        bluetoothLocator.stopScan();
        running = false;
        super.interrupt();
    }
}
