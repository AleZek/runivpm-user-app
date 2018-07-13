package com.ids.idsuserapp.percorso;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ids.idsuserapp.HomeActivity;
import com.ids.idsuserapp.PercorsoActivity;
import com.ids.idsuserapp.R;
import com.ids.idsuserapp.SearchModel;
import com.ids.idsuserapp.adapters.BeaconRecyclerAdapter;
import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.fragment.SelezionaMappaFragment;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;

import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;


public class HomeFragment extends BaseFragment {
    public static final String TAG = HomeFragment.class.getSimpleName();

    public static final String EMERGENCY = "emergenza";
    public static final String OFFLINE = "offline";
    public static final int ORIGIN_SELECTION_REQUEST_CODE = 200;
    public static final int DESTINATION_SELECTION_REQUEST_CODE = 201;
    public static final String EMERGENCY_ACTION = "emergency_action";

    private boolean visible = false;
    private boolean choosenOrigin = false;
    private boolean choosenDestination = false;


    private static Beacon origin = null, destination = null;
    public static final String INTENT_KEY_POSITION = "position";
    private int indexOfPathSelected;
    private ViewHolder holder;
    private boolean offline = false;


    public static HomeFragment newInstance(boolean emergency, boolean offline) {
        Log.d(TAG, String.valueOf(emergency));
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putBoolean(EMERGENCY, emergency);
        args.putBoolean(OFFLINE, offline);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        holder = new ViewHolder(view);
        toggleConfirmButtonState();


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Beacon node;
        byte[] serializedData;
        try {
            serializedData = data.getByteArrayExtra(INTENT_KEY_POSITION);
            if (serializedData == null) {
                throw new NullPointerException("Null array data");
            }
            node = (Beacon) SerializationUtils.deserialize(serializedData);

            switch (requestCode) {
                case ORIGIN_SELECTION_REQUEST_CODE:
                    origin = node;
                    choosenOrigin = true;
                    break;
                case DESTINATION_SELECTION_REQUEST_CODE:
                    destination = node;
                    choosenDestination = true;
                    break;
            }

            indexOfPathSelected = 0;
        } catch (NullPointerException ee) {
            Log.e(TAG, "NullPointer", ee);
        }
    }

    private void openSelezionaMappaFragment(View v) {
        SelezionaMappaFragment selectionFragment;
        Beacon alreadySelectedNode = null;

        int code = 1;
        switch (v.getId()) {
            case R.id.scegli_da_mappa_origine_button:
                code = ORIGIN_SELECTION_REQUEST_CODE;
                alreadySelectedNode = destination;
                break;
            case R.id.scegli_da_mappa_destinazione_button:
                code = DESTINATION_SELECTION_REQUEST_CODE;
                alreadySelectedNode = origin;
                break;
        }

        selectionFragment = SelezionaMappaFragment.newInstance(code, alreadySelectedNode, offline);
        selectionFragment.setTargetFragment(this, code);
        ((HomeActivity) getActivity()).changeFragment(selectionFragment);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            offline = getArguments().getBoolean(OFFLINE);
        }
        setText();
    }

    public void setText() {
        holder.infoTextView.setText("\n Seleziona origine e destinazione per visualizzare il percorso");
        if (choosenOrigin && !choosenDestination) {
            holder.infoTextView.setText("\nOrigine selezionata: " +
                    "\n Piano:" + origin.getFloor().toString() +
                    "\n Beacon: " + origin.getNome().toString() +
                    "\n\n Seleziona anche la destinazione se vuoi visualizzare il percorso.");

        } else if (choosenOrigin && choosenDestination) {
            if (origin.getId() == destination.getId()) {
                holder.infoTextView.setText("\n Devi selezionare due Beacon diversi \nper origine e destinazione");
            } else {
                holder.infoTextView.setText(
                        "\nOrigine selezionata: " +
                                "\n Piano:" + origin.getFloor().toString() +
                                "\n Beacon scelto: " + origin.getNome().toString() +
                                "\n\n Destinazione selezionata: " +
                                "\n Piano: " + destination.getFloor().toString() +
                                "\n Beacon scelto: " + destination.getNome().toString());
            }
        } else if (choosenDestination && !choosenOrigin) {
            holder.infoTextView.setText("\nDestinazione selezionata: " +
                    "\n Piano:" + destination.getFloor().toString() +
                    "\n Beacon: " + destination.getNome().toString() +
                    "\n\n Seleziona origine per poter visualizzare il percorso.");
        } else if (choosenOrigin && choosenDestination) {
            holder.infoTextView.setText(
                    "\n Origine selezionata: " +
                            "\n Piano:" + origin.getFloor().toString() +
                            "\n Beacon scelto: " + origin.getNome().toString() +
                            "\n\n Destinazione selezionata: " +
                            "\n Piano: " + destination.getFloor().toString() +
                            "\n Beacon scelto: " + destination.getNome().toString());
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set the Toolbar as the ActionBar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(holder.toolbar);
        assert activity.getSupportActionBar() != null;
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
    }

    /**
     * Classe wrapper degli elementi della vista
     */
    public class ViewHolder extends BaseFragment.ViewHolder {

        public final Toolbar toolbar;
        public final Button selezionaMappaOrigineButton;
        public final Button selezionaMappaDestinazioneButton;
        public final Button selezionaBeaconOrigineButton;
        public final Button selezionaBeaconDestinazioneButton;

        // public final View mappaContainer;

        public final View selezionaMappaOrigineLayout;
        public final View selezionaBeaconOrigineLayout;
        public final View selezionaMappaDestinazioneLayout;
        public final View selezionaBeaconDestinazioneLayout;

        public final Button visualizzaPercorsoButton;
        public final Button visualizzaUscitaVicinaButton;
        public TextView infoTextView;

        public final Button selezionaOrigineButton;
        public final Button selezionaDestinazioneButton;
        public BeaconViewModel mBeaconViewModel;
        public BeaconRecyclerAdapter beaconRecyclerAdapter;

        public ViewHolder(View v) {
            toolbar = find(v, (R.id.navigation_toolbar));
            selezionaMappaOrigineButton = find(v, R.id.scegli_da_mappa_origine_button);
            selezionaMappaDestinazioneButton = find(v, R.id.scegli_da_mappa_destinazione_button);
            selezionaBeaconOrigineButton = find(v, R.id.scegli_da_beacon_origine_button);
            selezionaBeaconDestinazioneButton = find(v, R.id.scegli_da_beacon_destinazione_button);

            selezionaMappaOrigineLayout = find(v, R.id.scegli_da_mappa_origine_layout);
            selezionaMappaDestinazioneLayout = find(v, R.id.scegli_da_mappa_destinazione_layout);
            selezionaBeaconOrigineLayout = find(v, R.id.scegli_da_beacon_origine_layout);
            selezionaBeaconDestinazioneLayout = find(v, R.id.scegli_da_beacon_destinazione_layout);

            infoTextView = find(v, R.id.info_text_view);
            visualizzaPercorsoButton = find(v, R.id.visualizza_percorso_button);
            visualizzaUscitaVicinaButton = find(v, R.id.visualizza_uscita_vicina_button);
            //   mappaContainer = v.findViewById(R.id.navigation_map_image);


            selezionaOrigineButton = find(v, R.id.seleziona_origine);
            selezionaDestinazioneButton = find(v, R.id.seleziona_destinazione);


            selezionaMappaOrigineLayout.setVisibility(View.GONE);
            selezionaMappaDestinazioneLayout.setVisibility(View.GONE);
            selezionaBeaconOrigineLayout.setVisibility(View.GONE);
            selezionaBeaconDestinazioneLayout.setVisibility(View.GONE);
            visualizzaUscitaVicinaButton.setVisibility(View.GONE);
            visualizzaPercorsoButton.setVisibility(View.GONE);


            //   mappaContainer.setVisibility(View.GONE);


            selezionaOrigineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleOrigine();
                }
            });
            selezionaDestinazioneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleDestinazione();
                }
            });

            selezionaMappaOrigineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openSelezionaMappaFragment(v);
                }
            });

            selezionaMappaDestinazioneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openSelezionaMappaFragment(v);
                }
            });

            selezionaBeaconOrigineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SimpleSearchDialogCompat(getActivity(), "Cerca", "Seleziona beacon", null, initData(), new SearchResultListener<Searchable>() {
                        @Override
                        public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                            BeaconViewModel beacon = new BeaconViewModel(getActivity().getApplication());
                            origin = beacon.findByName(searchable.getTitle());
                            choosenOrigin = true;
                            toggleConfirmButtonState();
                            setText();
                            toggleOrigine();
                            baseSearchDialogCompat.dismiss();
                        }
                    }).show();
                }
            });

            selezionaBeaconDestinazioneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SimpleSearchDialogCompat(getActivity(), "Cerca", "Seleziona beacon", null, initData(), new SearchResultListener<Searchable>() {
                        @Override
                        public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                            BeaconViewModel beacon = new BeaconViewModel(getActivity().getApplication());
                            destination = beacon.findByName(searchable.getTitle());
                            choosenDestination = true;
                            toggleConfirmButtonState();
                            setText();
                            toggleDestinazione();
                            baseSearchDialogCompat.dismiss();
                        }
                    }).show();


                }


            });

        }

        private ArrayList<SearchModel> initData() {
            ArrayList<SearchModel> items = new ArrayList<>();
            mBeaconViewModel = ViewModelProviders.of(getActivity()).get(BeaconViewModel.class);
            beaconRecyclerAdapter = new BeaconRecyclerAdapter(getContext());
            List<Beacon> beacons = mBeaconViewModel.getAllSynchronous();
            for (int i = 0; i < beacons.size(); i++) {
                items.add(new SearchModel(beacons.get(i).getNome()));
            }
            return items;
        }










        public void toggleOrigine() {
            {
                if (visible) {
                    selezionaMappaOrigineLayout.setVisibility(View.GONE);
                    selezionaBeaconOrigineLayout.setVisibility(View.GONE);
                    visible = false;
                } else {
                    selezionaMappaOrigineLayout.setVisibility(View.VISIBLE);
                    selezionaBeaconOrigineLayout.setVisibility(View.VISIBLE);
                    visible = true;
                }
            }
        }

        public void toggleDestinazione() {
            {
                if (visible) {
                    selezionaMappaDestinazioneLayout.setVisibility(View.GONE);
                    selezionaBeaconDestinazioneLayout.setVisibility(View.GONE);
                    visible = false;
                } else {
                    selezionaMappaDestinazioneLayout.setVisibility(View.VISIBLE);
                    selezionaBeaconDestinazioneLayout.setVisibility(View.VISIBLE);
                    visible = true;
                }
            }
        }


    }

    private void disableVisualizzaPercorsoButtonState() {
        holder.visualizzaPercorsoButton.setVisibility(View.GONE);
    }

    private void disableVisualizzaUscitaButtonState() {
        holder.visualizzaUscitaVicinaButton.setVisibility(View.GONE);
    }

    private void toggleConfirmButtonState() {
        if (!choosenOrigin) {
            disableVisualizzaPercorsoButtonState();
            disableVisualizzaUscitaButtonState();
        } else if (!choosenDestination) {
            disableVisualizzaPercorsoButtonState();
            holder.visualizzaUscitaVicinaButton.setVisibility(View.VISIBLE);
            holder.visualizzaUscitaVicinaButton.setTextColor(color(R.color.linkText));
            holder.visualizzaUscitaVicinaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PercorsoActivity.class);
                    intent.putExtra("beaconOrigine", SerializationUtils.serialize(origin));
                    startActivity(intent);
                }
            });
        } else if (origin == destination) {
            disableVisualizzaPercorsoButtonState();
            disableVisualizzaUscitaButtonState();
        }else{
            disableVisualizzaUscitaButtonState();
            holder.visualizzaPercorsoButton.setVisibility(View.VISIBLE);
            holder.visualizzaPercorsoButton.setTextColor(color(R.color.linkText));
            holder.visualizzaPercorsoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PercorsoActivity.class);
                    intent.putExtra("beaconOrigine", SerializationUtils.serialize(origin));
                    intent.putExtra("beaconDestinazione", SerializationUtils.serialize(destination));
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        origin = null;
        destination = null;
        choosenDestination = false;
        choosenOrigin = false;
    }


}
