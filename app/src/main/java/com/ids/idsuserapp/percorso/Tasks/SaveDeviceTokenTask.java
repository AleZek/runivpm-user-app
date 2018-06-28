package com.ids.idsuserapp.percorso.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;


public class SaveDeviceTokenTask extends AsyncTask<Void, Void, Boolean> {
    public static final String TAG = SaveDeviceTokenTask.class.getName();

    private Context context;
    private Exception thrownException;
    private TaskListener<Void> listener;

    public SaveDeviceTokenTask(Context context, TaskListener<Void> listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
          try {
          /*String ipAddress = (PreferenceManager.getDefaultSharedPreferences(context))
                    .getString(SettingsActivity.WESCAPE_HOSTNAME,
                               SettingsActivity.WESCAPE_DEFAULT_HOSTNAME);
            WescapeSessionManager sessionManager = new WescapeSessionManager(context, ipAddress);
            WescapeService service = ApiBuilder.buildWescapeService(ipAddress);

            String accessToken = sessionManager.getBearer();
            Log.d(TAG, "Access token: " + accessToken);

            Call<UserResponse> call = service.getCurrentUser(accessToken);
            Response<UserResponse> response = call.execute();
            UserForm userForm = new UserForm();

            switch (response.code()) {
                case HttpURLConnection.HTTP_OK: {
                    UserResponse userResponse = response.body();
                    Log.d(TAG, userResponse.toString());

                    String deviceKey = FirebaseInstanceId.getInstance().getToken();
                    userForm.setId(userResponse.getId())
                            .setEmail(userResponse.getEmail())
                            .setDeviceKey(deviceKey);
                    break;
                }
                default:
                    Log.e(TAG, "Errore HTTP: " + String.valueOf(response.code()));
            }

            if (userForm.getEmail() != null) {
                Call<UserResponse> callUpdate = service.updateUser(accessToken, userForm.getId(), userForm);
                Response<UserResponse> responseUpdate = callUpdate.execute();

                switch (responseUpdate.code()) {
                    case HttpURLConnection.HTTP_OK: {
                        Log.d(TAG, responseUpdate.toString());
                        break;
                    }
                    default:
                        Log.e(TAG, "Errore HTTP: " + String.valueOf(responseUpdate.code()));
                }
            }
            */
            return true;
        } catch (Exception e) {
            thrownException = e;
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            listener.onTaskSuccess(null);
        } else {
            listener.onTaskError(thrownException);
        }
        listener.onTaskComplete();
    }
}
