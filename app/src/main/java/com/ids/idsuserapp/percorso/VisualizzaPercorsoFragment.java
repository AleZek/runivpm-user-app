package com.ids.idsuserapp.percorso;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ids.idsuserapp.R;
import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.db.entity.Mappa;
import com.ids.idsuserapp.percorso.Tasks.MappaStaticaTask;
import com.ids.idsuserapp.percorso.Tasks.MinimumPathTask;
import com.ids.idsuserapp.percorso.Tasks.TaskListener;
import com.ids.idsuserapp.percorso.views.MapView;
import com.ids.idsuserapp.percorso.views.PinView;
import com.ids.idsuserapp.percorso.views.exceptions.DestinationNotSettedException;
import com.ids.idsuserapp.percorso.views.exceptions.OriginNotSettedException;

import java.util.ArrayList;
import java.util.List;

import static com.ids.idsuserapp.HomeActivity.OFFLINE_USAGE;


@SuppressLint("ValidFragment")
public class VisualizzaPercorsoFragment extends BaseFragment {
    public Beacon origine;
    public Beacon destinazione;
    private List<Path> solutionPaths = null;
    private Path selectedSolution;
    private int indexOfPathSelected;

    public int originFloor;
    public boolean offline;

    public ViewHolderPercorso holder;


    public VisualizzaPercorsoFragment(Beacon origine, Beacon destinazione) {
        this.origine = origine;
        this.destinazione = destinazione;

    }

    /**
     * Use this factory method to create a new instance of this fragment using the provided parameters.
     *
     * @return A new instance of fragment ResetPasswordFragment.
     */
    public static VisualizzaPercorsoFragment newInstance(Beacon origine, Beacon destinazione) {

        VisualizzaPercorsoFragment fragment = new VisualizzaPercorsoFragment(origine, destinazione);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        View view = inflater.inflate(R.layout.fragment_visualizza_percorso, container, false);
        holder = new ViewHolderPercorso(view);

        holder.floor155Button.setOnClickListener(new FloorButtonListener());
        holder.floor150Button.setOnClickListener(new FloorButtonListener());
        holder.floor145Button.setOnClickListener(new FloorButtonListener());
        holder.setupMapView();

        return view;
    }


    private class FloorButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            holder.floor155Button.setTextColor(color(R.color.colorWhite));
            holder.floor150Button.setTextColor(color(R.color.colorWhite));
            holder.floor145Button.setTextColor(color(R.color.colorWhite));
            button.setTextColor(color(R.color.linkText));
            int floor = origine.getFloorInt();
            originFloor = floor;
        }
    }


    public class ViewHolderPercorso extends BaseFragment.ViewHolder {
        public final MapView mapView;
        public final Button floor155Button;
        public final Button floor150Button;
        public final Button floor145Button;
        public final FloatingActionButton pathsFabButton;


        @SuppressWarnings("unchecked")
        public <T extends View> T find(View view, int id) {
            View resultView = view.findViewById(id);
            try {
                return (T) resultView;
            } catch (ClassCastException e) {
                return null;
            }
        }

        public ViewHolderPercorso(View v) {
            mapView = find(v, R.id.navigation_map_image);
            floor155Button = find(v, R.id.floor_button_155_visualizza);
            floor150Button = find(v, R.id.floor_button_150_visualizza);
            floor145Button = find(v, R.id.floor_button_145_visualizza);
            pathsFabButton = (FloatingActionButton) v.findViewById(R.id.navigation_fab_paths);


        }

        public void setupMapView() {
            holder.mapView.setOrigin(origine);
            holder.mapView.setDestination(destinazione);
            MinimumPathTask minimumPathTask = new MinimumPathTask(
                    getContext(), new MinimumPathListener());
            minimumPathTask.execute(origine, destinazione);
        }

    }


    // @TODO Esternalizzare
    private class MinimumPathListener implements TaskListener<List<Path>> {
        @Override
        public void onTaskSuccess(List<Path> searchResult) {
            solutionPaths = searchResult;
            selectedSolution = new Path(solutionPaths.get(0));
            MultiFloorPath multiFloorSolution = selectedSolution.toMultiFloorPath();

            try {
                holder.mapView.drawRoute(multiFloorSolution);
            } catch (OriginNotSettedException | DestinationNotSettedException e) {
                e.printStackTrace();
            }

            holder.pathsFabButton.show();
        }

        @Override
        public void onTaskError(Exception e) {
            Log.e(TAG, "Errore nel calcolo del percorso minimo", e);
        }

        @Override
        public void onTaskComplete() {
        }

        @Override
        public void onTaskCancelled() {

        }
    }

    /**
     * Listener per gestire la selezione di un percorso diverso
     */
    private class PathButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ArrayList<String> options = new ArrayList<>(solutionPaths.size());
            for (int i = 0; i < solutionPaths.size(); i++) {
                options.add(getString(R.string.label_select_path, i + 1));
            }

            new MaterialDialog.Builder(getContext())
                    .title(getContext().getString(R.string.select_path))
                    .items(options)
                    .itemsCallbackSingleChoice(indexOfPathSelected, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            indexOfPathSelected = which;
                            selectedSolution = new Path(solutionPaths.get(indexOfPathSelected));
                            MultiFloorPath multiFloorPath = solutionPaths.get(which).toMultiFloorPath();
                            try {
                                holder.mapView.drawRoute(multiFloorPath);
                            } catch (OriginNotSettedException | DestinationNotSettedException e) {
                                e.printStackTrace();
                            }
                            return true;
                        }
                    })
                    .widgetColorRes(R.color.colorPrimary)
                    .positiveText(R.string.action_confirm)
                    .positiveColorRes(R.color.colorPrimaryDark)
                    .negativeText(R.string.action_back)
                    .negativeColorRes(R.color.colorBlack)
                    .show();
        }
    }
}

