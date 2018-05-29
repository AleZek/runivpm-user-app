package com.ids.idsuserapp.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ids.idsuserapp.R;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;

/**
 * Created by zek on 06/04/18.
 */

public class BeaconAddFragment extends Fragment {

    private BeaconViewModel mBeaconViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beacon_recycler, container, false);
//        setupRecycler(view);
//        setupSearch(view);

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
}
