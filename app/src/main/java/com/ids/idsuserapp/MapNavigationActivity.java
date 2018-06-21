package com.ids.idsuserapp;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ids.idsuserapp.fragment.MapNavigationFragment;
import com.ids.idsuserapp.percorso.SelezionaMappaFragment;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public class MapNavigationActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_navigation);
        setupMapNavigationFragment();

    }


    private void setupMapNavigationFragment() {

        SelezionaMappaFragment fragment = new SelezionaMappaFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .addToBackStack(null);
        fragmentTransaction.add(R.id.fragment_place, fragment).commit();
    }


}