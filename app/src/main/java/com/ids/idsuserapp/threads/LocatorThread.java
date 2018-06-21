package com.ids.idsuserapp.threads;

import android.content.Context;

import com.ids.idsuserapp.utils.BluetoothLocator;

public class LocatorThread extends Thread {
    public BluetoothLocator bluetoothLocator;
    boolean running = false;

    public LocatorThread(Context context) {
        bluetoothLocator = new BluetoothLocator(context);
    }

    @Override
    public void run() {
        super.run();
        running = true;
        bluetoothLocator.setupLocatorScanner();
        while(running){
            bluetoothLocator.startScan();
            try {
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
