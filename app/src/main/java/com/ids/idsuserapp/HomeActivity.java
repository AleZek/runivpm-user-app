package com.ids.idsuserapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ids.idsuserapp.db.repository.BeaconRepository;
import com.ids.idsuserapp.fragment.MapNavigationFragment;
import com.ids.idsuserapp.percorso.SelezionaMappaFragment;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;

public class HomeActivity extends AppCompatActivity {

    private Button button;
    private BeaconViewModel mBeaconViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        button = findViewById(R.id.scegli_da_mappa_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MapNavigationActivity.class);
                startActivity(intent);
            }
        });

    }





}







