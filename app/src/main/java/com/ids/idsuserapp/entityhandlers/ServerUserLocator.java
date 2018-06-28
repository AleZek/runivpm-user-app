package com.ids.idsuserapp.entityhandlers;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ids.idsuserapp.R;
import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerUserLocator {
    private Context context;
    private BeaconViewModel beaconViewModel;
    private com.android.volley.RequestQueue serverRequestQueue;

    public ServerUserLocator(Context context) {
        this.context = context;

        this.beaconViewModel = new BeaconViewModel( (Application) context);
        serverRequestQueue = Volley.newRequestQueue(context);
    }

    public void sendPosition(String positionDevice) {
        int positionId = getIdFromDevice(positionDevice);
        JsonObjectRequest positionRequest = prepareSendPositionRequest(positionId);
        serverRequestQueue.add(positionRequest);
    }

    public JsonObjectRequest prepareSendPositionRequest(int positionId) {
        String url = context.getString(R.string.api_user_locator);
        JSONObject position = createPositionJson(positionId);
        return new JsonObjectRequest(Request.Method.POST, url, position,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "RUNIVPM: errore di comunicazione con il server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private JSONObject createPositionJson(int positionId) {
            JSONObject position = new JSONObject();
            try {
                //TODO NOME UTENTE DA RECUPERARE DA APP
                position.put("email", "zek@zek.it");
                position.put("position", positionId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return position;
    }

    private int getIdFromDevice(String device){
        Beacon beacon = beaconViewModel.findByDevice(device);
        if(beacon != null)
            return beacon.getId();
        else return 0;
    }

}
