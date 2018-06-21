package com.ids.idsuserapp.percorso.Tasks;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;


import com.ids.idsuserapp.R;
import com.ids.idsuserapp.db.entity.Mappa;
import com.ids.idsuserapp.viewmodel.MappaViewModel;

/**
 * Represents an asynchronous login/registration task used to authenticate the user.
 */
public class MappaStaticaTask extends AsyncTask<Integer, Void, Boolean> {
    private static final String LOG_TAG = MappaStaticaTask.class.getSimpleName();
    private Mappa map;
    private TaskListener<Mappa> listener;
    private Exception thrownException;
    public MappaViewModel mappaViewModel;
    private Context context;

    public MappaStaticaTask(Context context, TaskListener<Mappa> listener) {
        this.listener = listener;
        this.context = context;
        mappaViewModel = new MappaViewModel((Application) context.getApplicationContext());
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        int floor = params[0];
        try {

            Log.d(LOG_TAG, Integer.toString(floor));
            map = new Mappa("Quota "+floor);


        } catch (Exception e) {
            thrownException = e;
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            listener.onTaskSuccess(map);
        } else {
            listener.onTaskError(thrownException);
        }
        listener.onTaskComplete();
    }

    @Override
    protected void onCancelled() {
        listener.onTaskCancelled();
    }
}