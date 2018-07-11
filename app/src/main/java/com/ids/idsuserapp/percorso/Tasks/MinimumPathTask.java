package com.ids.idsuserapp.percorso.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.ids.idsuserapp.PercorsoActivity;
import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.db.entity.Tronco;
import com.ids.idsuserapp.viewmodel.ArcoViewModel;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;
import com.ids.idsuserapp.wayfinding.Dijkstra;
import com.ids.idsuserapp.wayfinding.Grafo;
import com.ids.idsuserapp.wayfinding.Percorso;

import java.util.List;

public class MinimumPathTask extends AsyncTask<Beacon, Void, Boolean> {
    public static final String TAG = MinimumPathTask.class.getName();

    private Context context;
    private Exception thrownException;
    private List<Percorso> searchResult;
    private boolean emergency;
    private ArcoViewModel arcoViewModel;
    private BeaconViewModel beaconViewModel;
    private TaskListener<List<Percorso>> listener;
    Dijkstra dijkstra;


    public MinimumPathTask(Context context,
                           TaskListener<List<Percorso>> listener, ArcoViewModel arcoViewModel,BeaconViewModel beaconViewModel, boolean emergency) {
        this.context = context;
        this.listener = listener;
        this.arcoViewModel= arcoViewModel;
        this.beaconViewModel = beaconViewModel;
        this.emergency = emergency;

    }

    @Override
    protected Boolean doInBackground(Beacon... params) {
        try {
            Beacon origine = params[0];
            setDijkstra(origine);
            if(!emergency) {
                Beacon destinazione = params[1];
                searchResult = dijkstra.searchDoublePath(destinazione);
            }else {
                List<Beacon> uscite = beaconViewModel.getUscite();
                searchResult = dijkstra.searchNearestExits(uscite);
            }
        } catch (Exception e) {
            thrownException = e;
            return false;
        }

        return searchResult != null;
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

    private void setDijkstra(Beacon origine) {
        List<Tronco> tronchi = arcoViewModel.getTronchi(); //classi con un arco e due beacon
        Grafo grafo = new Grafo(tronchi);

        dijkstra = new Dijkstra();
        dijkstra.inizio(origine);
        dijkstra.in(grafo);
        dijkstra.setNormalizationBasis(1.0);
    }
}
