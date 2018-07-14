package com.ids.idsuserapp.autentication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ids.idsuserapp.HomeActivity;
import com.ids.idsuserapp.MainActivity;
import com.ids.idsuserapp.R;
import com.ids.idsuserapp.authentication.RegistrationFragment;
import com.ids.idsuserapp.entityhandlers.UserRequestHandler;

public class RegistrationActivity extends AppCompatActivity implements UserRequestHandler.ProgressInterface{

    private ProgressDialog progressDialog;

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

    @Override
    public void showProgressBar(String title, String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }
}
