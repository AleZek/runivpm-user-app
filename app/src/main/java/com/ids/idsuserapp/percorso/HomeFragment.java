package com.ids.idsuserapp.percorso;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ids.idsuserapp.HomeActivity;
import com.ids.idsuserapp.R;
import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;

import org.apache.commons.lang3.SerializationUtils;


public class HomeFragment extends Fragment {
    public static final String TAG = HomeFragment.class.getSimpleName();

    public static final String EMERGENCY = "emergenza";
    public static final String OFFLINE = "offline";
    public static final int ORIGIN_SELECTION_REQUEST_CODE = 200;
    public static final int DESTINATION_SELECTION_REQUEST_CODE = 201;

    private boolean visible = false;
    private boolean choosenOrigin=false;
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
                    choosenOrigin=true;
                    break;
                case DESTINATION_SELECTION_REQUEST_CODE:
                    destination = node;
                    choosenDestination=true;
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

        if(getArguments() != null) {
            offline = getArguments().getBoolean(OFFLINE);
        }

        /*if(!offline) {
            NodesDownloaderTask nodesDownloaderTask = new NodesDownloaderTask(
                    getContext(), new NodesDownloaderTaskListener());
            nodesDownloaderTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            EdgesDownloaderTask task = new EdgesDownloaderTask(
                    getContext(), new EdgesDownloaderTaskListener());
            task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }*/
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

        public final Button selezionaOrigineButton;
        public final Button selezionaDestinazioneButton;
        public BeaconViewModel mBeaconViewModel;

        public ViewHolder(View v) {
            toolbar = v.findViewById((R.id.navigation_toolbar));
            selezionaMappaOrigineButton = v.findViewById(R.id.scegli_da_mappa_origine_button);
            selezionaMappaDestinazioneButton = v.findViewById(R.id.scegli_da_mappa_destinazione_button);
            selezionaBeaconOrigineButton = v.findViewById(R.id.scegli_da_beacon_origine_button);
            selezionaBeaconDestinazioneButton = v.findViewById(R.id.scegli_da_beacon_destinazione_button);

            selezionaMappaOrigineLayout = v.findViewById(R.id.scegli_da_mappa_origine_layout);
            selezionaMappaDestinazioneLayout = v.findViewById(R.id.scegli_da_mappa_destinazione_layout);
            selezionaBeaconOrigineLayout = v.findViewById(R.id.scegli_da_beacon_origine_layout);
            selezionaBeaconDestinazioneLayout = v.findViewById(R.id.scegli_da_beacon_destinazione_layout);

         //   mappaContainer = v.findViewById(R.id.navigation_map_image);


            selezionaOrigineButton = v.findViewById(R.id.seleziona_origine);
            selezionaDestinazioneButton = v.findViewById(R.id.seleziona_destinazione);


            selezionaMappaOrigineLayout.setVisibility(View.GONE);
            selezionaMappaDestinazioneLayout.setVisibility(View.GONE);
            selezionaBeaconOrigineLayout.setVisibility(View.GONE);
            selezionaBeaconDestinazioneLayout.setVisibility(View.GONE);

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




}
