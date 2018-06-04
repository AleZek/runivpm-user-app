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
    private RecyclerView recyclerBeacon;
    private SearchView searchView;

    private BeaconRecyclerAdapter beaconRecyclerAdapter;
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
        beaconRecyclerAdapter = new BeaconRecyclerAdapter(getContext());
        mBeaconViewModel.getBeaconByIdMappa(mappa).observe((LifecycleOwner) this, new Observer<List<Beacon>>() {
            @Override
            public void onChanged(@Nullable final List<Beacon> beacon) {
                // Update the cached copy of the words in the adapter.
                beaconRecyclerAdapter.setmBeacon(beacon);
            }
        });

        mBeaconViewModel.getAll();


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
        searchView = view.findViewById(R.id.searchView);
        searchView.setQueryHint("Cerca un beacon...");
        searchView.setIconified(false);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.searchView:
                        searchView.onActionViewExpanded();
                        break;
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                beaconRecyclerAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                beaconRecyclerAdapter.getFilter().filter(newText);
                //getActivity().
                return false;
            }

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setupRecycler(View view) {
        recyclerBeacon = view.findViewById(R.id.recyclerViewBeacon);
        recyclerBeacon.setAdapter(beaconRecyclerAdapter);
        recyclerBeacon.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerBeacon.setHasFixedSize(false);
    }



}