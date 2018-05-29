package com.ids.idsuserapp.entityhandlers;

import android.content.Context;
import android.net.Uri;

import com.ids.idsuserapp.viewmodel.ArcoViewModel;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;
import com.ids.idsuserapp.viewmodel.MappaViewModel;

public class ArcoDataHandler {
    private Context context;
    private MappaViewModel mappaViewModel;
    private BeaconViewModel beaconViewModel;
    private ArcoViewModel arcoViewModel;
    private com.android.volley.RequestQueue serverRequestQueue;
    private Uri nuovaMappaFilePath;

}
