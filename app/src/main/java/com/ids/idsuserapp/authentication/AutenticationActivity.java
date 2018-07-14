package com.ids.idsuserapp.authentication;

import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ids.idsuserapp.R;
import com.ids.idsuserapp.percorso.BaseFragment;
import com.ids.idsuserapp.utils.PermissionsUtil;

public class AutenticationActivity extends AppCompatActivity{

    //private Button button1;
    //private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentication);
        PermissionsUtil permissionsUtil = new PermissionsUtil(this);
        permissionsUtil.handleFilePermissions();
        permissionsUtil.handleLocationPermissions();
        permissionsUtil.requestEnableBt();

      /*  button1 = findViewById(R.id.login_btn);
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

*
*/


        if (savedInstanceState == null) {
            AutenticationFragment autenticationFragment =new AutenticationFragment();
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.autentication_content_pane, autenticationFragment, AutenticationFragment.TAG)
                    .commit();
        }


    }

    /**
     * Cambia il fragment
     *
     * @param fragment
     */
    public void changeFragment(BaseFragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.autentication_content_pane, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(fragment.TAG)
                .commit();
    }

    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(AutenticationActivity.this);
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


    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(AutenticationActivity.this);
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











