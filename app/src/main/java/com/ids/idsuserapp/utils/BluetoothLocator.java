package com.ids.idsuserapp.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.util.Log;

import com.ids.idsuserapp.db.entity.Beacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_POWER;

public class BluetoothLocator {
    private Context context;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner scanner;
    private LocatorCallbacks locatorCallbacks;
    private ScanCallback scanCallback;
    private HashMap strongestBeacon;
    private ArrayList<String> beaconWhiteList;
    private boolean scanning;

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

    public void setBeaconWhiteList(List<Beacon> beacons) {
        beaconWhiteList = new ArrayList<>();
        for(Beacon beacon : beacons){
            String currDevice = beacon.getDevice();
            if (!currDevice.equals("null"))
                beaconWhiteList.add(currDevice);
        }
    }

    //    private void generateNavigationScanCallback() {
//        scanCallback = new ScanCallback() {
//            @Override
//            public void onScanResult(int callbackType, ScanResult result) {
//                super.onScanResult(callbackType, result);
//                if (result.getDevice() != null) {
//                    positionUpdaters.readScannedDevice(result);
//                    if (positionChanged())
//                        positionUpdaters.nextStep();
//                }
//            }
//
//            @Override
//            public void onScanFailed(int errorCode) {
//                super.onScanFailed(errorCode);
//            }
//        };
//    }
//
//
//    public void setupNavigationScanner() {
//        positionUpdaters = (NavigationCallbacks) context;
//        ScanSettings scanSettings = generateNavigationScanSettings();
//
//        scanner = mBluetoothAdapter.getBluetoothLeScanner();
//
//        startScan();
//    }

    public void stopScan(){
        if (scanning){
            scanner.stopScan(scanCallback);
            scanning = false;
        }
    }

    public void startScan(){
        scanner.startScan(scanCallback);
        scanning = true;
    }

    public void setupLocatorScanner(){
        locatorCallbacks = (LocatorCallbacks) context;
        setupLocatorScanCallBack();
    }

    private void setupLocatorScanCallBack() {
        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                BluetoothDevice scannedDevice = result.getDevice();
                setStrongestBeacon(scannedDevice.toString(), result.getRssi());
                if(positionChanged()) {
                    if(isBeacon(scannedDevice)) {
                        locatorCallbacks.sendCurrentPosition(scannedDevice);
                        scanner.flushPendingScanResults(scanCallback);
                        stopScan();
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
        return (int) strongestBeacon.get("scanNumber") == 5;
    }

    public HashMap getStrongestBeacon() {
        return strongestBeacon;
    }

    public boolean isBeacon(BluetoothDevice resultDevice) {
        String deviceName = resultDevice.getName();
        return resultDevice != null && deviceName!= null && deviceName.equals("CC2650 SensorTag");
    }
}
