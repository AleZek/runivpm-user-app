package com.ids.idsuserapp.percorso.views;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.davemorrissey.labs.subscaleview.ImageSource;
import com.ids.idsuserapp.R;
import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.db.entity.Mappa;
import com.ids.idsuserapp.percorso.MultiFloorPath;
import com.ids.idsuserapp.percorso.Path;
import com.ids.idsuserapp.percorso.Tasks.MappaStaticaTask;
import com.ids.idsuserapp.percorso.Tasks.TaskListener;
import com.ids.idsuserapp.percorso.animation.ShowProgressAnimation;
import com.ids.idsuserapp.percorso.views.exceptions.DestinationNotSettedException;
import com.ids.idsuserapp.percorso.views.exceptions.OriginNotSettedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MapView extends LinearLayout {
    private static final String TAG = MapView.class.getName();
    private ViewHolder holder;
    private int currentFloor;
    private Beacon origin, destination;
    private MultiFloorPath route;
    private boolean offline;

    public boolean isOffline() {
        return offline;
    }

    public MapView setOffline(boolean offline) {
        this.offline = offline;
        return this;
    }

    public MapView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.map_view, this);
        holder = new ViewHolder(this);
        holder.floorButtonContainer.setVisibility(View.GONE);
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Disegna l'origine
     *
     * @param origin Nodo di origine
     * @return Istanza di MapView
     */
    public MapView setOrigin(Beacon origin) {
        this.origin = origin;
        this.currentFloor = origin.getFloor();
        changeImage(String.valueOf(currentFloor));
        drawPins(currentFloor);
        return this;
    }

    /**
     * Update the PinView Map image based on the floor. Handles all recylcing bitmap problems
     * N.B. Metodo fortemente scorretto: viene delegata alla vista un parte di logica. La view non dovrebbe neanche
     * sapere cos'è un thread! Purtroppo al momento non si siamo riusciti a gestire diversamente
     * il gargabe collector e le bitmap
     *
     * @param floor Floor
     */
    private void changeImage(String floor) {
        holder.placeholderView.setVisibility(View.GONE);
        holder.progressAnimation = new ShowProgressAnimation(
                holder.mapContainer,
                holder.downloadProgress,
                20);

        holder.progressAnimation.showProgress(true);
        AsyncTask<Integer, Void, Boolean> mapLoader;
        if(offline) {
            mapLoader = new MappaStaticaTask(getContext(), new MapListener());
        } else {
            mapLoader = new MappaStaticaTask(getContext(), new MapListener()); //new MapsDownloaderTask(getContext(), new MapListener());
        }
        mapLoader.execute(Integer.valueOf(floor));
    }

    public void showSpinner(boolean show) {
        holder.progressAnimation.showProgress(show);
    }

    /**
     * Disegna i pin
     *
     * @param floor Piano
     */
    private void drawPins(int floor) {
        ArrayList<MapPin> pins = new ArrayList<>();

        if (origin != null && origin.getFloor() == floor) {
            pins.add(new MapPin(origin.toPointF(), MapPin.Colors.RED));
        }
        if (destination != null && destination.getFloor() == floor) {
            pins.add(new MapPin(destination.toPointF(), MapPin.Colors.BLUE));
        }

        holder.pinView.setMultiplePins(pins);
    }

    /**
     * Disegna la destinazione
     *
     * @param destination Nodo destinazione
     * @return MapView
     */
    public MapView setDestination(Beacon destination) {
        this.destination = destination;
        this.currentFloor = destination.getFloor();
        changeImage(String.valueOf(currentFloor));
        drawPins(currentFloor);
        return this;
    }

    /**
     * Disegna il percorso sulla mappa
     *
     * @param route Soluzione
     * @return MapView
     * @throws OriginNotSettedException
     * @throws DestinationNotSettedException
     */
    public MapView drawRoute(MultiFloorPath route)
            throws OriginNotSettedException, DestinationNotSettedException {
        this.route = route;
        this.origin = (Beacon) route.getOrigin();
        this.destination = (Beacon) route.getDestination();

        if (origin == null) {
            throw new OriginNotSettedException();
        }
        if (destination == null) {
            throw new DestinationNotSettedException();
        }

        currentFloor = origin.getFloor();

        changeImage(String.valueOf(currentFloor));
        drawPath(String.valueOf(currentFloor));
        drawPins(currentFloor);
        setupFloorButtons();

        return this;
    }

    public MapView reset() {
        origin = null;
        destination = null;
        route = null;
        holder.pinView.resetPath();
        return this;
    }

    /**
     * Disegna il percorso di un piano
     *
     * @param floor Piano
     */
    private void drawPath(String floor) {
        if (this.route != null) {
            holder.pinView.setPath(this.route.get(floor));
        }
    }

    /**
     * Imposta i listener sui bottoni dei piani e li nasconde se non contenuti nella soluzione
     */
    private void setupFloorButtons() {
        // UI operations
        holder.hideFloorButtons()
                .setupFloorButtonsUI(String.valueOf(currentFloor));

        // Soluzione per piano non vuota -> visualizzare il piano
        // Soluzione per piano con un solo punto
        //              -> destinazione => visualizzare il piano
        //              -> != destinazione => nascondere il piano
        // Soluzione per piano vuota -> nascondere il piano
        Set<String> pianiNellaSoluzione = route.keySet();
        Path solutionPerFloor;
        boolean onePointSolution, destinationSolution, multiplePointSolution, originSolution;

        for (String floor : pianiNellaSoluzione) {
            solutionPerFloor = route.get(floor);

            onePointSolution = solutionPerFloor.size() == 1;
            multiplePointSolution = solutionPerFloor.size() > 1;
            destinationSolution = (onePointSolution && solutionPerFloor.get(0).equals(destination));
            originSolution = (onePointSolution && solutionPerFloor.get(0).equals(origin));

            if (multiplePointSolution || destinationSolution || originSolution) {
                holder.floorButtons.get(floor).setVisibility(View.VISIBLE);
                holder.floorButtons.get(floor).setOnClickListener(new FloorButtonListener());
            }
        }
    }

    /**
     * Listener che gestisce il click sui bottoni dei piani
     */
    private class FloorButtonListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            currentFloor = Integer.parseInt(((Button) v).getText().toString());
            holder.setupFloorButtonsUI(String.valueOf(currentFloor));

            changeImage(String.valueOf(currentFloor));
            drawPath(String.valueOf(currentFloor));
            drawPins(currentFloor);
        }
    }

    /**
     * Callback del MapDownloaderTask
     */
    private class MapListener implements TaskListener<Mappa> {

        @Override
        public void onTaskSuccess(Mappa map) {
            holder.pinView.setImage(ImageSource.asset(map.getImmagine()));
        }

        @Override
        public void onTaskError(Exception e) {
            Log.e(TAG, "Map download error: ", e);
        }

        @Override
        public void onTaskComplete() {
            holder.progressAnimation.showProgress(false);
        }

        @Override
        public void onTaskCancelled() {

        }
    }

    /**
     * View Holder
     */
    private class ViewHolder {
        public final PinView pinView;
        public final LinearLayout floorButtonContainer;
        public final HashMap<String, Button> floorButtons;
        public final ViewGroup mapContainer;
        public final ProgressBar downloadProgress;
        public final ViewGroup placeholderView;
        public ShowProgressAnimation progressAnimation;

        public ViewHolder(View view) {
            pinView = (PinView) view.findViewById(R.id.map_image);
            floorButtonContainer = (LinearLayout) view.findViewById(R.id.floor_button_container);
            mapContainer = (ViewGroup) view.findViewById(R.id.map_container);
            downloadProgress = (ProgressBar) view.findViewById(R.id.downloading_progress);
            floorButtons = getFloorButtons();
            placeholderView = (ViewGroup) view.findViewById(R.id.map_placeholder);
        }

        /**
         * Ritorna la lista dei bottoni dei piani
         *
         * @return Hashmap di bottoni
         */
        private HashMap<String, Button> getFloorButtons() {
            ViewGroup buttonContainer = floorButtonContainer;
            HashMap<String, Button> result = new HashMap<>();

            for (int i = 0; i < buttonContainer.getChildCount(); i++) {
                View v = buttonContainer.getChildAt(i);
                if (v instanceof Button) {
                    Button button = (Button) v;
                    result.put(button.getText().toString(), button);
                }
            }

            return result;
        }

        /**
         * Nasconde i pulsanti dei piani
         *
         * @return Istanza di Viewholder
         */
        private ViewHolder hideFloorButtons() {
            floorButtonContainer.setVisibility(View.VISIBLE);
            for (String key : floorButtons.keySet()) {
                floorButtons.get(key).setVisibility(View.GONE);
            }
            return this;
        }

        /**
         * Imposta la selezione sui bottoni di piano
         */
        private ViewHolder setupFloorButtonsUI(String floor) {
            for (String key : floorButtons.keySet()) {
                floorButtons.get(key).setTextColor(getResources().getColor(R.color.colorBlack));
            }
            floorButtons.get(floor).setTextColor(getResources().getColor(R.color.linkText));

            return this;
        }
    }
}
