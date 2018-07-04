package com.ids.idsuserapp.entityhandlers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ids.idsuserapp.R;
import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.utils.AuthenticatedJsonObjectRequest;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by giuli on 07/05/2018.
 */

public class BeaconDataHandler {
    private Context context;
    private BeaconViewModel beaconViewModel;
    private DataRetriever dataRetriever;
    private com.android.volley.RequestQueue serverRequestQueue;


    public BeaconDataHandler(Context context, BeaconViewModel beaconViewModel) {
        this.context = context;
        this.beaconViewModel = beaconViewModel;
        dataRetriever = (DataRetriever) context;
        serverRequestQueue = Volley.newRequestQueue(context);
    }

    public void retrieveBeaconDataset() {
        JsonObjectRequest beaconRequest = prepareGetBeaconRequest();
        serverRequestQueue.add(beaconRequest);
    }

    public AuthenticatedJsonObjectRequest prepareGetBeaconRequest() {
        String bcns_url = context.getString(R.string.api_beacons);
        return new AuthenticatedJsonObjectRequest(context, Request.Method.GET, bcns_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            persistBeaconCollection(response.getJSONArray("hydra:member"));
                            dataRetriever.retrieveArchi();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }


    //locale
    private void persistBeaconCollection(JSONArray beacon) {
        for (int i = 0; i<beacon.length();i++) {
            ArrayList<String> nuovoBeaconDataStrings = null;
            try {
                nuovoBeaconDataStrings = getNuovoBeaconDataStrings(beacon.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            beaconViewModel.insert(new Beacon(nuovoBeaconDataStrings));
        }
    }

    private ArrayList<String> getNuovoBeaconDataStrings(JSONObject beacon){
        ArrayList<String> datiNuovoBeacon = new ArrayList<>();
        for( String campo : BeaconViewModel.CAMPI){
            try {
                datiNuovoBeacon.add(beacon.getString(campo));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return datiNuovoBeacon;
    }
}
