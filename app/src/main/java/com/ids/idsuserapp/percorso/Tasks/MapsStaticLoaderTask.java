package com.ids.idsuserapp.percorso.Tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.ids.idsuserapp.R;
import com.ids.idsuserapp.db.entity.Mappa;

/**
 * Represents an asynchronous login/registration task used to authenticate the user.
 */
public class MapsStaticLoaderTask extends AsyncTask<Integer, Void, Boolean> {
    private static final String LOG_TAG = MapsStaticLoaderTask.class.getSimpleName();
    private Mappa map;
    private TaskListener<Mappa> listener;
    private Exception thrownException;
    private Context context;

    public MapsStaticLoaderTask(Context context, TaskListener<Mappa> listener) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        int floor = params[0];
        Bitmap image;
        try {

            Log.d(LOG_TAG, Integer.toString(floor));

            switch (floor) {
                case 145:
                    image = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.floor_145);
                    break;
                case 150:
                    image = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.floor_150);
                    break;
                case 155:
                    image = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.floor_155);
                    break;
                default:
                    image = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.floor_145);
                    break;
            }

            Log.d(LOG_TAG, "Size: "+image.getWidth()+ " " +image.getHeight());

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