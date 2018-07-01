package com.ids.idsuserapp.utils;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.ids.idsuserapp.LogoActivity;

public class PermissionsUtil {
    private Context context;
    static int BT_ENABLED = 1;

    public PermissionsUtil(Context context) {
        this.context = context;
    }

    //gestisce i permessi dei file , con output nel log se sono  stati garantiti. Altrimenti vengono effettutate le richieste
    // in app. Questo metodo è chiamato in onCreate.
    public void handleFilePermissions(){
        if (!isFilePermissionGranted()) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
        }
    }
    private boolean isFilePermissionGranted(){
        if (Build.VERSION.SDK_INT >= 23) {
            return context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        else
            return false;
    }


    public void handleLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check 
            if (ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Questa applicazione ha bisogno dei permessi relativi alla Posizione");
                builder.setMessage("Consentire l'accesso alla posizione.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onDismiss(DialogInterface dialog) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
                    }
                });
                builder.show();
            }
        }
    }

    public boolean requestEnableBt() {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = null;
        if (bluetoothManager != null) {
            bluetoothAdapter = bluetoothManager.getAdapter();
        }
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent =
                    new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            Activity activity = (Activity) context;
            activity.startActivityForResult(enableBtIntent, BT_ENABLED);
            return false;
        }

        return true;
    }


    public void btAlert(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Questa applicazione ha bisogno del Bluetooth per poter funzionare");
        builder.setMessage("Si prega di attivare il bluetooth.");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                requestEnableBt();
            }
        });
        builder.setNegativeButton("Esci", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(activity, LogoActivity.class);
                activity.startActivity(intent);
            }
        });

        builder.show();
    }


}
