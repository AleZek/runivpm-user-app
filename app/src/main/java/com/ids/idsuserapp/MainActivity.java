package com.ids.idsuserapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //questo metodo permette alla app di sottoscriversi al topic emergenza, questo permette a firebase
    // di mandare messaggi broadcast alle istanze della app
//    private void subscribeTopic(final String topic){
//        FirebaseMessaging.getInstance().subscribeToTopic(topic)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "Sottoscrizione avvenuta a ";
//                        if (!task.isSuccessful()) {
//                            msg = "sottoscrizione fallita a ";
//                        }
//                        Log.d(TAG, msg + topic); // sono mostrati dei messaggi nel log e nella app se la sottoscrizione avviene o meno
//                        Toast.makeText(MainActivity.this, msg + topic, Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//    }
}
