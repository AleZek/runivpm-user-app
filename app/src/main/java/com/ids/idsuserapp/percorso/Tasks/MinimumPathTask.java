package com.ids.idsuserapp.percorso.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.afollestad.materialdialogs.MaterialDialog;

import com.ids.idsuserapp.R;
import com.ids.idsuserapp.db.entity.Arco;
import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.db.repository.ArcoRepository;
import com.ids.idsuserapp.percorso.Path;
import com.ids.idsuserapp.wayfinding.Dijkstra;
import com.ids.idsuserapp.wayfinding.Grafo;
import com.ids.idsuserapp.wayfinding.Percorso;

import java.util.List;

public class MinimumPathTask extends AsyncTask<Beacon, Void, Boolean> {
    public static final String TAG = MinimumPathTask.class.getName();

    private MaterialDialog dialog;
    private Context context;
    private Exception thrownException;
    private List<Percorso> searchResult;
    private TaskListener<List<Path>> listener;

    public MinimumPathTask(Context context,
                           TaskListener<List<Path>> listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Beacon... params) {
        try {
            Beacon beginBeacon = params[0];
            Beacon endBeacon = params[1];
            Grafo graph = new Grafo();
            Dijkstra dijkstraSolver = new Dijkstra();

            dijkstraSolver.inizio(beginBeacon)
                    .in(graph)
                    .setNormalizationBasis(ArcoRepository.getMaxLength());
            searchResult = dijkstraSolver.searchDoublePath(endBeacon);

            Thread.sleep(1000);
            return (searchResult != null);
        } catch (Exception e) {
            thrownException = e;
            return false;
        }
    }

    @Override
    protected void onPreExecute() {
        dialog = new MaterialDialog.Builder(context)
                .title(context.getString(R.string.computing_route))
                .content(context.getString(R.string.please_wait))
                .progress(true, 0)
                .widgetColorRes(R.color.colorPrimary)
                .show();
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (success) {
            listener.onTaskSuccess(searchResult);
        } else {
            listener.onTaskError(thrownException);
        }
        listener.onTaskComplete();
    }
}
