package com.ids.idsuserapp.authentication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ids.idsuserapp.R;
import com.ids.idsuserapp.entityhandlers.UserRequestHandler;

public class LoginActivity extends AppCompatActivity implements UserRequestHandler.ProgressInterface {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        if (savedInstanceState == null) {
            LoginFragment loginFragment =new LoginFragment();
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.login_content_pane, loginFragment, LoginFragment.TAG)
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
