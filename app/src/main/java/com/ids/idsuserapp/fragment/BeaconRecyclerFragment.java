package com.ids.idsuserapp.fragment;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ids.idsuserapp.R;
import com.ids.idsuserapp.adapters.BeaconRecyclerAdapter;
import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;

import java.util.List;


public class BeaconRecyclerFragment extends Fragment{
    private RecyclerView recyclerBeaconPartenza; //recyclerview nel xml della partenza
    private RecyclerView recyclerBeaconDestinazione; // recyclerview della destinazione da collegare nel xml
    private SearchView searchViewPartenza; //searchview della partenza, presente nel layout del beaconfragment
    private SearchView searchViewDestinazione; //searchview della destinazione

    private BeaconRecyclerAdapter beaconRecyclerAdapterPartenza; // recycleradapter della partenza
    private BeaconRecyclerAdapter beaconRecyclerAdapterDestinazione; // recyclerAdapter della destinazione
    private BeaconViewModel mBeaconViewModel;
    private int mappa;


    public BeaconRecyclerFragment() {
    }


    public static BeaconRecyclerFragment newInstance() {
        BeaconRecyclerFragment fragment = new BeaconRecyclerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);


        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*try{
            Bundle bundle = getActivity().getIntent().getExtras();
            mappa = ((int) bundle.get("idMappa"));
        }catch(NullPointerException e ){
            e.printStackTrace();
        }*/
        mBeaconViewModel = ViewModelProviders.of(this).get(BeaconViewModel.class);
        beaconRecyclerAdapterPartenza = new BeaconRecyclerAdapter(getContext()); // i recycleradapter sono istanziati ma devono ancora essere popolati
        beaconRecyclerAdapterDestinazione = new BeaconRecyclerAdapter(getContext());
        //qui rispetto al codice admin app ho eliminato la dipendenza dalla mappa in modo che venisse restituita
        // una lista di tutti i beacon
        mBeaconViewModel.getAllBeacons().observe((LifecycleOwner) this, new Observer<List<Beacon>>() {
            @Override
            public void onChanged(@Nullable final List<Beacon> beacon) {
                // Update the cached copy of the words in the adapter.
                beaconRecyclerAdapterPartenza.setmBeacon(beacon); // i recycleradapter sono popolati
                beaconRecyclerAdapterDestinazione.setmBeacon(beacon);
            }
        });

        //mBeaconViewModel.getAllBeacons();



    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beacon_recycler, container, false);
        setupRecycler(view);
        setupSearch(view);

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        // mListener = null;
    }



    private void setupSearch(View view) {
        searchViewPartenza = view.findViewById(R.id.searchViewBeaconPart);
        searchViewPartenza.setQueryHint("Seleziona il punto di partenza");
        searchViewPartenza.setIconified(false);
        searchViewPartenza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.searchViewBeaconPart:
                        searchViewPartenza.onActionViewExpanded();
                        break;
                }
            }
        });

        searchViewPartenza.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                beaconRecyclerAdapterPartenza.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                beaconRecyclerAdapterPartenza.getFilter().filter(newText);
                //getActivity().
                return false;
            }

        });

        searchViewDestinazione = view.findViewById(R.id.searchViewBeaconDest);
        searchViewDestinazione.setQueryHint("Seleziona la destinazione");
        searchViewDestinazione.setIconified(false);
        searchViewDestinazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.searchViewBeaconDest:
                        searchViewDestinazione.onActionViewExpanded();
                        break;
                }
            }
        });

        searchViewDestinazione.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                beaconRecyclerAdapterDestinazione.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                beaconRecyclerAdapterDestinazione.getFilter().filter(newText);
                //getActivity().
                return false;
            }

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setupRecycler(View view) {
        recyclerBeaconPartenza = view.findViewById(R.id.recyclerViewBeaconPart);
        recyclerBeaconPartenza.setAdapter(beaconRecyclerAdapterPartenza);
        recyclerBeaconPartenza.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerBeaconPartenza.setHasFixedSize(false);

        recyclerBeaconDestinazione = view.findViewById(R.id.recyclerViewBeaconDest);
        recyclerBeaconDestinazione.setAdapter(beaconRecyclerAdapterDestinazione);
        recyclerBeaconDestinazione.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerBeaconDestinazione.setHasFixedSize(false);
    }



}