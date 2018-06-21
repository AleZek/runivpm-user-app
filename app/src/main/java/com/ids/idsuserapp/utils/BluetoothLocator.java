package com.ids.idsuserapp.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import static android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_POWER;

public class BluetoothLocator {
    private Context context;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner scanner;
    private NavigationCallbacks positionUpdaters;
    private LocatorCallbacks locatorCallbacks;
    private ScanCallback scanCallback;
    private HashMap strongestBeacon;

    public BluetoothLocator(Context context) {
        this.context = context;
        setBtManager();
        initializeStrongestBeacon();
        scanner = mBluetoothAdapter.getBluetoothLeScanner();
    }

    private void setBtManager() {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
    }

    private ScanSettings generateNavigationScanSettings() {
        return new ScanSettings.Builder().setScanMode(SCAN_MODE_LOW_POWER).build();
    }

    private void generateNavigationScanCallback() {
        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                if (result.getDevice() != null) {
                    positionUpdaters.readScannedDevice(result);
                    if (positionChanged())
                        positionUpdaters.nextStep();
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            }
        };
    }


    public void setupNavigationScanner() {
        positionUpdaters = (NavigationCallbacks) context;
        ScanSettings scanSettings = generateNavigationScanSettings();

        scanner = mBluetoothAdapter.getBluetoothLeScanner();

        scanner.startScan(null, scanSettings, scanCallback);
    }

    public void stopScan(){
        scanner.stopScan(scanCallback);
    }

    public void startScan(){
        scanner.startScan(scanCallback);
    }

    public void setupLocatorScanner(){
        locatorCallbacks = (LocatorCallbacks) context;
        setupLocatorScanCallBack();

        startScan();
    }

    private void setupLocatorScanCallBack() {
        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                setStrongestBeacon(result.getDevice().toString(), result.getRssi());
                if(positionChanged()) {
                    locatorCallbacks.sendCurrentPosition();
                    scanner.flushPendingScanResults(scanCallback);
                    stopScan();
                }
            }
        };
    }


    public interface NavigationCallbacks {

        void readScannedDevice(ScanResult result);

        void nextStep();
    }

    public interface LocatorCallbacks {
        void sendCurrentPosition();
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
        return (int) strongestBeacon.get("scanNumber") == 5;
    }

    public HashMap getStrongestBeacon() {
        return strongestBeacon;
    }
}
