package com.ids.idsuserapp.authentication;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ids.idsuserapp.LogoActivity;
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


    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Sei sicuro di voler uscire?");
        builder.setCancelable(true);
        builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
                }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
