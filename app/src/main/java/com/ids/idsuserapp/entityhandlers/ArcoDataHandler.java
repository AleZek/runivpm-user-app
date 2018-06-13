package com.ids.idsuserapp.entityhandlers;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ids.idsuserapp.R;
import com.ids.idsuserapp.db.entity.Arco;
import com.ids.idsuserapp.viewmodel.ArcoViewModel;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;
import com.ids.idsuserapp.viewmodel.MappaViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ArcoDataHandler {
    private Context context;
    private ArcoViewModel arcoViewModel;
    private com.android.volley.RequestQueue serverRequestQueue;

    public ArcoDataHandler(Context context, ArcoViewModel arcoViewModel, BeaconViewModel beaconViewModel) {
        this.context = context;

        this.arcoViewModel = arcoViewModel;
        serverRequestQueue = Volley.newRequestQueue(context);
    }

    public void retrieveArchiDataset() {
        JsonObjectRequest archiRequest = prepareGetArchiRequest();
        serverRequestQueue.add(archiRequest);
    }

    public JsonObjectRequest prepareGetArchiRequest() {
        String archi_url = context.getString(R.string.api_archi);
        return new JsonObjectRequest(Request.Method.GET, archi_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            persistArchiCollection(response.getJSONArray("hydra:member"));
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

    private void persistArchiCollection(JSONArray beacon) {
        for (int i = 0; i < beacon.length(); i++) {
            HashMap<String, String> nuovoArcoDataStrings = null;
            try {
                nuovoArcoDataStrings = getNuovoArcoDataStrings(beacon.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arcoViewModel.insert(new Arco(nuovoArcoDataStrings));
        }
    }

    private HashMap<String, String> getNuovoArcoDataStrings(JSONObject arco) {
        HashMap<String, String> datiNuovoArco = new HashMap<>();
        for (String campo : ArcoViewModel.CAMPI) {
            try {
                datiNuovoArco.put(campo, arco.getString(campo));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return datiNuovoArco;
    }


}
