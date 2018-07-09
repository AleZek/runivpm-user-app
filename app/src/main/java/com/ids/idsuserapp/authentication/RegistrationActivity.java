package com.ids.idsuserapp.authentication;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ids.idsuserapp.HomeActivity;
import com.ids.idsuserapp.MainActivity;
import com.ids.idsuserapp.R;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        if (savedInstanceState == null) {
            RegistrationFragment registrationFragment =new RegistrationFragment();
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.registraion_content_pane, registrationFragment, RegistrationFragment.TAG)
                    .commit();
        }


    }


}
