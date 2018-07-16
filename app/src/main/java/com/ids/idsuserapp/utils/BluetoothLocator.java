package com.ids.idsuserapp.utils;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.util.Log;

import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.entityhandlers.UserRequestHandler;
import com.ids.idsuserapp.percorso.animation.ShowProgressAnimation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_POWER;
import static java.lang.Thread.sleep;

public class BluetoothLocator {
    private Context context;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner scanner;
    private LocatorCallbacks locatorCallbacks;
    private ScanCallback scanCallback;
    private HashMap strongestBeacon;
    private ArrayList<String> beaconWhiteList;
    private boolean scanning;
    static String TAG = "BTLocator";

    public BluetoothLocator(Context context) {
        this.context = context;
        setBtManager();
        initializeStrongestBeacon();
        enableBTAndWait();

        scanner = mBluetoothAdapter.getBluetoothLeScanner();
        Log.v(TAG,scanner.toString());
    }

    public void enableBTAndWait() {
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
            while(!mBluetoothAdapter.isEnabled()){
            }
        }
    }

    public void disableBTAndWait(){
        mBluetoothAdapter.disable();
        while(mBluetoothAdapter.isEnabled()){
        }

    }

    public void restartBt(){
        disableBTAndWait();
        enableBTAndWait();
    }

    private void setBtManager() {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
    }

    public void stopScan(){
        if (scanning){
            scanning = false;
            scanner.stopScan(scanCallback);
        }
    }

    public void startScan(){
        if(mBluetoothAdapter.isEnabled()) {
//            Timer timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    scanner.startScan(scanCallback);
//                    scanning = true;
//                }
//            }, 2000);
            scanner.startScan(scanCallback);
            scanning = true;

        }else if(!scanning){
            enableBTAndWait();
            scanning = true;
        }
    }

    public void setupLocatorScanner(){
        locatorCallbacks = (LocatorCallbacks) context;
        setupLocatorScanCallBack();
    }

    private void setupLocatorScanCallBack() {
        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                if(scanning) {
                    super.onScanResult(callbackType, result);
                    BluetoothDevice scannedDevice = result.getDevice();
                    setStrongestBeacon(scannedDevice.toString(), result.getRssi());
                    if (positionChanged()) {
                        if (isBeacon(scannedDevice)) {
                            Log.v(TAG, context.toString());
                            locatorCallbacks.sendCurrentPosition(scannedDevice);

//                            scanner.flushPendingScanResults(scanCallback);
                            stopScan();
                        }
                    }
                }
            }
        };
    }

    public interface LocatorCallbacks {
        void sendCurrentPosition(BluetoothDevice device);
    }

    private void initializeStrongestBeacon() {
        strongestBeacon = new HashMap<>();
        strongestBeacon.put("nome", "");
        strongestBeacon.put("rssi", -200);
        strongestBeacon.put("scanNumber", 0);
    }

    public void setStrongestBeacon(String resultDevice, int resultRssi) {
        if (!resultDevice.equals(strongestBeacon.get("nome")) && (int) strongestBeacon.get("rssi") < resultRssi) {
            strongestBeacon.put("nome", resultDevice);
            strongestBeacon.put("rssi", resultRssi);
            strongestBeacon.put("scanNumber", 1);
            Log.v("Device", strongestBeacon.toString());
        } else if (resultDevice.equals(strongestBeacon.get("nome"))) {
            strongestBeacon.put("rssi", resultRssi);
            strongestBeacon.put("scanNumber", (int) strongestBeacon.get("scanNumber") + 1);
            Log.v("Device", strongestBeacon.toString());
        }
    }

    public boolean positionChanged() {
        return (int) strongestBeacon.get("scanNumber") >= 5;
    }

    public HashMap getStrongestBeacon() {
        return strongestBeacon;
    }

    public boolean isBeacon(BluetoothDevice resultDevice) {
        String deviceName = resultDevice.getName();
        return resultDevice != null && deviceName!= null && (deviceName.equals("CC2650 SensorTag"));
    }

    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }


}
