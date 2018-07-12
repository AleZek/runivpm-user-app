package com.ids.idsuserapp.authentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ids.idsuserapp.R;

public class LoginActivity extends AppCompatActivity {

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
}
