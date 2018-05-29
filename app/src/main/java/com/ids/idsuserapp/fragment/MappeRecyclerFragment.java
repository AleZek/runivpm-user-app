package com.ids.idsuserapp.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ids.idsuserapp.R;
import com.ids.idsuserapp.adapters.MappaRecyclerAdapter;
import com.ids.idsuserapp.db.entity.Mappa;
import com.ids.idsuserapp.entityhandlers.MappaDataHandler;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;
import com.ids.idsuserapp.viewmodel.MappaViewModel;

import java.util.List;


public class MappeRecyclerFragment extends Fragment{

    private RecyclerView recyclerMappe;
    private SearchView searchView;

    private MappaRecyclerAdapter mappaRecyclerAdapter;
    private MappaViewModel mMappaViewModel;
    private BeaconViewModel beaconViewModel;
    private MappaDataHandler mappaDataHandler;


    public MappeRecyclerFragment() {
        // Required empty public constructor
    }

    public static MappeRecyclerFragment newInstance() {
        MappeRecyclerFragment fragment = new MappeRecyclerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMappaViewModel = ViewModelProviders.of(this).get(MappaViewModel.class);
        mappaRecyclerAdapter = new MappaRecyclerAdapter(getContext());
        mMappaViewModel.getAll().observe(this, new Observer<List<Mappa>>() {
            @Override
            public void onChanged(@Nullable final List<Mappa> mappe) {
                // Update the cached copy of the words in the adapter.
                mappaRecyclerAdapter.setmMappe(mappe);
            }
        });

        beaconViewModel = ViewModelProviders.of(this).get(BeaconViewModel.class);
        mappaDataHandler = new MappaDataHandler(getContext(),mMappaViewModel,beaconViewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mappe_recycler, container, false);
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
        searchView.setQueryHint("Cerca una mappa...");
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
                mappaRecyclerAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mappaRecyclerAdapter.getFilter().filter(newText);
                //getActivity().
                return false;
            }

        });

    }

    private void setupRecycler(View view) {
        recyclerMappe =view.findViewById(R.id.recyclerViewMappe);
        recyclerMappe.setAdapter(mappaRecyclerAdapter);
        recyclerMappe.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerMappe.setHasFixedSize(false);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
/*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
*/
}
