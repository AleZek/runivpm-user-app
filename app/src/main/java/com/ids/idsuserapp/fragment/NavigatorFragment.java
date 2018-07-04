package com.ids.idsuserapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ids.idsuserapp.R;
import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.db.entity.Tronco;
import com.ids.idsuserapp.percorso.BaseFragment;
import com.ids.idsuserapp.percorso.Tasks.TaskListener;
import com.ids.idsuserapp.percorso.directions.HumanDirection;
import com.ids.idsuserapp.percorso.views.MapView;
import com.ids.idsuserapp.percorso.views.exceptions.DestinationNotSettedException;
import com.ids.idsuserapp.percorso.views.exceptions.OriginNotSettedException;
import com.ids.idsuserapp.wayfinding.IndiciNavigazione;
import com.ids.idsuserapp.wayfinding.Percorso;
import com.ids.idsuserapp.wayfinding.PercorsoMultipiano;
import com.ids.idsuserapp.wayfinding.directions.Actions;
import com.ids.idsuserapp.wayfinding.directions.Directions;
import com.ids.idsuserapp.wayfinding.directions.DirectionsTranslator;

import java.util.Stack;

/**
 * A simple {@link Fragment} subclass. Use the {@link NavigatorFragment#newInstance} factory method
 * to create an instance of this fragment.
 */
public class NavigatorFragment extends BaseFragment {
    public static final String TAG = NavigatorFragment.class.getName();
    private static final String SOLUTION = "solution";
    private static final String EMERGENCY = "emergency";
    private static final String OFFLINE = "offline";
    private ViewHolder holder;
    private Percorso routeToBeFlown;
    private Tronco excludedTrunk;
    private Stack<Beacon> routeTraveled;
    private PercorsoMultipiano multiFloorSolution;
    private Directions directions;
    private DirectionsTranslator translator;
    private boolean emergency;
    private boolean offline;

    /**
     * Use this factory method to create a new instance of this fragment using the provided
     * parameters.
     *
     * @param solution Lista di nodi che costiuiscono la soluzione
     * @return A new instance of fragment NavigatorFragment.
     */
    public static NavigatorFragment newInstance(Percorso solution, boolean emergency, boolean offline) {
        NavigatorFragment fragment = new NavigatorFragment();
        Bundle args = new Bundle();
        args.putSerializable(SOLUTION, solution);
        args.putBoolean(EMERGENCY, emergency);
        args.putBoolean(OFFLINE, offline);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            routeToBeFlown = (Percorso) getArguments().getSerializable(SOLUTION);
            routeTraveled = new Stack<>();
            emergency = getArguments().getBoolean(EMERGENCY);
            offline = getArguments().getBoolean(OFFLINE);

            if (routeToBeFlown != null) {
                excludedTrunk = routeToBeFlown.getTroncoEscluso();
                multiFloorSolution = routeToBeFlown.toMultiFloorPath();
                translator = new DirectionsTranslator(routeToBeFlown);
                directions = translator.calculateDirections()
                        .getDirections();
            } else {
                Log.e(TAG, "Solution is null. Aborting");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigator, container, false);
        holder = new ViewHolder(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        holder.setupUI();

        try {
            holder.mapView.drawRoute(multiFloorSolution);
        } catch (OriginNotSettedException | DestinationNotSettedException e) {
            e.printStackTrace();
        }

        updateDirectionDisplay(new IndiciNavigazione(0, 1));
    }

    private enum ButtonType {NEXT, PREVIOUS}

    private class IndicationButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            ButtonType tag = (ButtonType) v.getTag();
            switch (tag) {
                case NEXT: {
                    next();
                    break;
                }
                case PREVIOUS: {
                    prev();
                    break;
                }
            }
        }
    }

    private void next() {
       /* if (routeToBeFlown.size() >= 2) {
            routeTraveled.push((Beacon) routeToBeFlown.getOrigine());
            ContinuousMPSTask continuousMPSTask = new ContinuousMPSTask(getContext(),
                    new ContinuousMPSTaskListener(),
                    (Tronco) excludedTrunk, emergency, offline);
            continuousMPSTask.execute((Beacon) routeToBeFlown.get(1),
                    (Beacon) routeToBeFlown.getDestinazione());
            holder.mapView.showSpinner(true);
        }*/
    }

    private void prev() {
       /* if (routeTraveled.size() > 0) {
            Beacon backStep = routeTraveled.pop();
            ContinuousMPSTask continuousMPSTask = new ContinuousMPSTask(getContext(),
                    new ContinuousMPSTaskListener(),
                    (Tronco) excludedTrunk, emergency, offline);
            continuousMPSTask.execute(backStep,
                    (Beacon) routeToBeFlown.getDestinazione());
            holder.mapView.showSpinner(true);
        }*/
    }

    /**
     * Aggiorna la vista con le indicazioni da seguire
     *
     * @param indices Tupla di indici dei nodi
     */
    private void updateDirectionDisplay(IndiciNavigazione indices) {
        holder.setCurrentDirection(HumanDirection
                .createHumanDirection(getContext(), directions.getCurrent(indices)));
        if (!indices.isLast()) {
            holder.setNextDirection(
                    HumanDirection.createHumanDirection(getContext(), directions.getNext(indices)),
                    ((Beacon) routeToBeFlown.getNext(indices)).getNome());
        } else {
            holder.setNavigationEnding();
        }
    }

    private class ContinuousMPSTaskListener implements TaskListener<Percorso> {
      //  private final String TAG = ContinuousMPSTask.class.getName();
        private Beacon prev, current, next, nexter;
        private Actions currentAction, nextAction;

        @Override
        public void onTaskSuccess(Percorso path) {
            routeToBeFlown = path;
            try {
                // @TODO Prendere qui il primo lato ed inviarlo al server per la posizione futura

                // Ricavo le indicazioni
                setPoints();
                currentAction = translator.getDirectionForNextNode(prev, current, next);
                holder.setCurrentDirection(HumanDirection.createHumanDirection(getContext(), currentAction));

                if (routeToBeFlown.destinazioneRaggiunta()) {
                    // Destinazione raggiunta, disegno solo il pallino rosso
                    holder.setNavigationEnding();
                    holder.mapView.reset()
                            .setOrigin((Beacon) routeToBeFlown.getDestinazione());
                } else {
                    // Destinazione da raggiungere, disegno tutto il percorso
                    nextAction = translator.getDirectionForNextNode(current, next, nexter);
                    holder.setNextDirection(HumanDirection.createHumanDirection(getContext(), nextAction),
                            ((Beacon) next).getNome());
                    holder.mapView.drawRoute(routeToBeFlown.toMultiFloorPath());
                }
            } catch (OriginNotSettedException | DestinationNotSettedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onTaskError(Exception e) {
            Log.e(TAG, "Error", e);
        }

        @Override
        public void onTaskComplete() {
            holder.mapView.showSpinner(false);
        }

        @Override
        public void onTaskCancelled() {
            holder.mapView.showSpinner(false);
        }

        private void setPoints() {
            prev = routeTraveled.size() > 0 ? routeTraveled.lastElement() : null;
            current = routeToBeFlown.getOrigine();
            next = !routeToBeFlown.destinazioneRaggiunta() ? routeToBeFlown.get(1) : null;
            nexter = routeToBeFlown.size() > 2 ? routeToBeFlown.get(2) : null;
        }
    }

    public class ViewHolder {
        public final MapView mapView;
        public final ImageButton nextButton;
        public final ImageButton previousButton;
        public final TextView indicationTextView;
        public final TextView nextNodeTextView;
        public final ImageView indicationSymbol;
        public final ImageView nextNodeIcon;
        public final ViewGroup nextNodeContainer;

        public ViewHolder(View view) {
            mapView = (MapView) view.findViewById(R.id.navigation_map);
            nextButton = (ImageButton) view.findViewById(R.id.next_button);
            previousButton = (ImageButton) view.findViewById(R.id.previous_button);
            indicationTextView = (TextView) view.findViewById(R.id.indication_text);
            nextNodeTextView = (TextView) view.findViewById(R.id.next_node_text);
            indicationSymbol = (ImageView) view.findViewById(R.id.indication_icon);
            nextNodeIcon = (ImageView) view.findViewById(R.id.next_step_icon);
            nextNodeContainer = (ViewGroup) view.findViewById(R.id.next_step_container);
        }

        /**
         * Visualizza l'indicazione corrente
         *
         * @param direction Indicazione corrente
         * @return Istanza di corrente di {@link ViewHolder}
         */
        public ViewHolder setCurrentDirection(HumanDirection direction) {
            holder.indicationTextView.setText(direction.getDirection());
            holder.indicationSymbol.setImageResource(direction.getIconResource());
            return this;
        }

        /**
         * Visualizza l'indicazione successiva
         *
         * @param direction Indicazione successiva
         * @param nodeName  Nome del nodo successivo
         * @return Istanza corrente di {@link ViewHolder}
         */
        public ViewHolder setNextDirection(HumanDirection direction, String nodeName) {
            holder.nextNodeContainer.setVisibility(View.VISIBLE);
            String text = String.format(getString(R.string.toward_node), nodeName);
            holder.nextNodeTextView.setText(text);
            holder.nextNodeIcon.setImageResource(direction.getIconResource());
            return this;
        }

        /**
         * Visualizza il messaggio di fine navigazione
         *
         * @return Istanza corrente di {@link ViewHolder}
         */
        public ViewHolder setNavigationEnding() {
            holder.nextNodeContainer.setVisibility(View.GONE);
            holder.nextNodeTextView.setText(getString(R.string.congratulation));
            return this;
        }

        private void setupUI() {
            mapView.setOffline(offline);
            nextButton.setTag(ButtonType.NEXT);
            previousButton.setTag(ButtonType.PREVIOUS);
            nextButton.setOnClickListener(new IndicationButtonListener());
            previousButton.setOnClickListener(new IndicationButtonListener());
        }
    }
}
