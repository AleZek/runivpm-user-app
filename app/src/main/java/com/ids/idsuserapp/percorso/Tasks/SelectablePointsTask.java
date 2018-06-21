package com.ids.idsuserapp.percorso.Tasks;

import android.app.Application;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;


import com.ids.idsuserapp.MainActivity;
import com.ids.idsuserapp.MapNavigationActivity;
import com.ids.idsuserapp.adapters.BeaconRecyclerAdapter;
import com.ids.idsuserapp.db.AppRoomDatabase;
import com.ids.idsuserapp.db.dao.BeaconDao;
import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.db.entity.Position;
import com.ids.idsuserapp.db.repository.BeaconRepository;
import com.ids.idsuserapp.percorso.SelezionaMappaFragment;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;

import java.util.List;

public class SelectablePointsTask extends AsyncTask<Position, Void, Boolean> {
    private static final String TAG = SelectablePointsTask.class.getName();
    private TaskListener<Beacon> listener;
    private Exception thrownException;
    private Beacon selectedNode = null;
    private int radius;
    private BeaconViewModel mBeaconViewModel;

    public SelectablePointsTask(TaskListener<Beacon> listener,
                                int radius, BeaconViewModel beaconViewModel) {
        this.listener = listener;
        this.radius = radius;
        this.mBeaconViewModel = beaconViewModel;
    }

    @Override
    protected Boolean doInBackground(Position... params) {
        try {
            double minDistance = Double.POSITIVE_INFINITY,
                    distance = 0;
            Position position = params[0];

            List<Beacon> nodes = mBeaconViewModel.getBeaconsByFloor(position.floor, (int) position.x, (int) position.y, radius);

            for (Beacon node : nodes) {
                distance = position.distance(node.getX(), node.getY());
                if (distance < minDistance) {
                    minDistance = distance;
                    selectedNode = node;
                }
            }

            return (selectedNode != null);
        } catch (Exception e) {
            thrownException = e;
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            listener.onTaskSuccess(selectedNode);
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
