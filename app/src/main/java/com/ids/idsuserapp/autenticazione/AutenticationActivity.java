package com.ids.idsuserapp.autenticazione;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ids.idsuserapp.R;

public class AutenticationActivity extends FragmentActivity{

    private Button button1;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentication);


        button1 = findViewById(R.id.login_btn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AutenticationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        button2 = findViewById(R.id.register_btn);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AutenticationActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });



    }










}
