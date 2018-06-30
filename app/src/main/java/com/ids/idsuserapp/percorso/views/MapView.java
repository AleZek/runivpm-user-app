package com.ids.idsuserapp.percorso.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.BuddhistCalendar;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.ids.idsuserapp.R;
import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.db.entity.Mappa;
import com.ids.idsuserapp.percorso.Tasks.MapsStaticLoaderTask;
import com.ids.idsuserapp.percorso.Tasks.TaskListener;
import com.ids.idsuserapp.percorso.animation.ShowProgressAnimation;
import com.ids.idsuserapp.percorso.views.exceptions.DestinationNotSettedException;
import com.ids.idsuserapp.percorso.views.exceptions.OriginNotSettedException;
import com.ids.idsuserapp.wayfinding.Percorso;
import com.ids.idsuserapp.wayfinding.PercorsoMultipiano;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MapView extends LinearLayout {
    private static final String TAG = MapView.class.getName();
    private ViewHolder holder;
    private int currentFloor;
    private Beacon origin, destination;
    private PercorsoMultipiano route;
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
        this.currentFloor = origin.getFloorInt();
        changeImage(currentFloor);
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
    private void changeImage(int floor) {

        holder.progressAnimation = new ShowProgressAnimation(
                holder.mapContainer,
                holder.downloadProgress,
                20);

        holder.progressAnimation.showProgress(true);

        AsyncTask<Integer, Void, Boolean> mapLoader;
        mapLoader = new MapsStaticLoaderTask(getContext(), new MapListener());

        mapLoader.execute(floor);
    }


    /**
     * Disegna i pin
     *
     * @param floor Piano
     */
    private void drawPins(int floor) {
        ArrayList<MapPin> pins = new ArrayList<>();

        if (origin != null && origin.getFloorInt() == floor) {
            pins.add(new MapPin(origin.toPointF(), MapPin.Colors.RED));
        }
        if (destination != null && destination.getFloorInt() == floor) {
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
        this.currentFloor = destination.getFloorInt();
        changeImage(currentFloor);
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
    public MapView drawRoute(PercorsoMultipiano route)
            throws OriginNotSettedException, DestinationNotSettedException {
        this.route = route;
        this.origin = (Beacon) route.getOrigine();
        this.destination = (Beacon) route.getDestinazione();

        if (origin == null) {
            throw new OriginNotSettedException();
        }
        if (destination == null) {
            throw new DestinationNotSettedException();
        }

        currentFloor = origin.getFloorInt();

        changeImage(currentFloor);
        drawPath(currentFloor);
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
    private void drawPath(int floor) {
        if (this.route != null) {
            holder.pinView.setPath(this.route.get(String.valueOf(floor)));
        }
    }

    /**
     * Imposta i listener sui bottoni dei piani e li nasconde se non contenuti nella soluzione
     */
    private void setupFloorButtons() {
        // UI operations
        holder.hideFloorButtons()
                .setupFloorButtonsUI(String.valueOf(currentFloor));
        Set<String> pianiNellaSoluzione = route.keySet();
        Percorso solutionPerFloor;
        boolean onePointSolution, destinationSolution, multiplePointSolution, originSolution;

        for (String floor : pianiNellaSoluzione) {
            solutionPerFloor = route.get(floor);

            onePointSolution = solutionPerFloor.size() == 1;
            multiplePointSolution = solutionPerFloor.size() > 1;
            destinationSolution = (onePointSolution && solutionPerFloor.get(0).equals(destination));
            originSolution = (onePointSolution && solutionPerFloor.get(0).equals(origin));

            if (multiplePointSolution || destinationSolution || originSolution) {
                holder.floorButtons.get("Quota " +floor).setVisibility(View.VISIBLE);
                holder.floorButtons.get("Quota "+floor).setOnClickListener(new FloorButtonListener());
            }
        }
    }

    /**
     * Listener che gestisce il click sui bottoni dei piani
     */
    private class FloorButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            currentFloor = Integer.parseInt(((Button) v).getText().toString().substring(6));
            holder.setupFloorButtonsUI(String.valueOf(currentFloor));

            changeImage(currentFloor);
            drawPath(currentFloor);
            drawPins(currentFloor);
        }
    }

    /**
     * Callback del MapDownloaderTask
     */
    private class MapListener implements TaskListener<Mappa> {

        @Override
        public void onTaskSuccess(Mappa mappa) {

            int floor = Integer.parseInt(mappa.getName().substring(6));
            Bitmap bitmap;

            switch (floor) {
                case 145:
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.floor_145);
                    break;
                case 150:
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.floor_150);
                    break;
                case 155:
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.floor_155);
                    break;
                default:
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.floor_155);
                    break;
            }
            holder.pinView.setImage(bitmap);
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

        public Button floor155Button;
        public Button floor145Button;
        public Button floor150Button;
        public final ViewGroup mapContainer;
        public final HashMap<String, Button> floorButtons;
        public final LinearLayout floorButtonContainer;

        public final ProgressBar downloadProgress;
        public ShowProgressAnimation progressAnimation;

        public ViewHolder(View view) {
            floorButtonContainer = (LinearLayout) view.findViewById(R.id.floor_button_container);

            pinView = (PinView) view.findViewById(R.id.map_image);
            mapContainer = (ViewGroup) view.findViewById(R.id.map_container);
            downloadProgress = (ProgressBar) view.findViewById(R.id.downloading_progress);
            floor145Button = (Button) view.findViewById(R.id.floor_button_145);
            floor155Button = (Button) view.findViewById(R.id.floor_button_155);
            floor150Button = (Button) view.findViewById(R.id.floor_button_150);
            floorButtons = getFloorButtons();

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
            Button prova = floorButtons.get("Quota " +floor);//.setTextColor(getResources().getColor(R.color.linkText));
            prova.setTextColor(getResources().getColor(R.color.linkText));
            return this;
        }
    }

}
