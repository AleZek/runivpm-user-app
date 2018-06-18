package com.ids.idsuserapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ids.idsuserapp.fragment.MapNavigationFragment;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public class MapNavigationActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_navigation);

                    /*
                    Fragment fr = new FirstFragment();
fr.setArguments(args);
FragmentManager fm = getFragmentManager();
FragmentTransaction fragmentTransaction = fm.beginTransaction();
fragmentTransaction.replace(R.id.fragment_place, fr);
fragmentTransaction.commit();
                     */
    }
}




  /*  private void setupMapNavigationFragment() {
        MapNavigationFragment fragment = new MapNavigationFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.mapnavigationcontainer, fragment).commit();
    }*/


