package com.ids.idsuserapp.percorso.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.ids.idsuserapp.PercorsoActivity;
import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.db.entity.Tronco;
import com.ids.idsuserapp.viewmodel.ArcoViewModel;
import com.ids.idsuserapp.wayfinding.Dijkstra;
import com.ids.idsuserapp.wayfinding.Grafo;
import com.ids.idsuserapp.wayfinding.Percorso;

import java.util.List;

public class MinimumPathTask extends AsyncTask<Beacon, Void, Boolean> {
    public static final String TAG = MinimumPathTask.class.getName();

    private Context context;
    private Exception thrownException;
    private List<Percorso> searchResult;
    ArcoViewModel arcoViewModel;
    private TaskListener<List<Percorso>> listener;
    Dijkstra dijkstra;


    public MinimumPathTask(Context context,
                           TaskListener<List<Percorso>> listener, ArcoViewModel arcoViewModel) {
        this.context = context;
        this.listener = listener;
        this.arcoViewModel= arcoViewModel;

    }

    @Override
    protected Boolean doInBackground(Beacon... params) {
        try {
            Beacon origine = params[0];
            Beacon destinazione = params[1];
            List<Tronco> tronchi = arcoViewModel.getTronchi(); //classi con un arco e due beacon
            Grafo grafo = new Grafo(tronchi);

            dijkstra = new Dijkstra();
            dijkstra.inizio(origine);
            dijkstra.in(grafo);
            dijkstra.setNormalizationBasis(1.0);
            searchResult = dijkstra.searchDoublePath(destinazione);
            return (searchResult != null);
        } catch (Exception e) {
            thrownException = e;
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            listener.onTaskSuccess(searchResult);
        } else {
            listener.onTaskError(thrownException);
        }

        listener.onTaskComplete();
    }


}
