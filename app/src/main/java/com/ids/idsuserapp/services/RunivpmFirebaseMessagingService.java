package com.ids.idsuserapp.services;

import android.app.KeyguardManager;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.PowerManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ids.idsuserapp.HomeActivity;
import com.ids.idsuserapp.MainActivity;
import com.ids.idsuserapp.PercorsoActivity;
import com.ids.idsuserapp.R;
import com.ids.idsuserapp.StopEmergencyActivity;

import java.util.logging.SocketHandler;

/** da usare se si voglio effettuare delle implementazioni custom sui metodi di FirebaseMessagingService.
 * Qui dovrebbero essere implementati i metodi che scattano alla ricezione del messaggio  quando la app
 * è in foreground.
 *
 */
public class RunivpmFirebaseMessagingService extends FirebaseMessagingService {
    private String TAG = "RunivpmFirebaseMessagingService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.auth_preference), MODE_PRIVATE);
        if (remoteMessage.getData() != null && !sharedPreferences.getString("token","").equals("")) {

            Intent intent;
            if (Boolean.parseBoolean(remoteMessage.getData().get("Emergenza"))) {
                SharedPreferences locationPref = getSharedPreferences(getString(R.string.local_position), MODE_PRIVATE);
                if(!locationPref.getString("position", "").equals(""))
                    intent = GetPercorsoIntent();
                else
                    intent = getScegliOrigineIntent();
            } else {
                intent = getStopEmergencyIntent();
            }
            startActivity(intent);
            // da qui in poi puo essere inutile perche il resto avviene nella activity
            //il il wakelock del powermanager è utile al risveglio automatico del telefono in caso di emergenza
//            PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
//            PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
//            wakeLock.acquire();
//            //utile allo sblocco automatico del telefono nel caso di emergenza
//            KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
//            KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
//            keyguardLock.disableKeyguard();
        }

    }

    private Intent getStopEmergencyIntent() {
        Intent intent = new Intent(getApplicationContext(), StopEmergencyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("stop", true);
        return intent;
    }

    private Intent getScegliOrigineIntent() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("emergency", true);
        intent.putExtra("offline", false);
        return intent;
    }

    private Intent GetPercorsoIntent() {
        Intent intent = new Intent(getApplicationContext(), PercorsoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("emergency", true);
        intent.putExtra("offline", false);
        return intent;
    }

    private void handleNow() {
    }

    private void scheduleJob() {
    }
}
