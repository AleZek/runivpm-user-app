package com.ids.idsuserapp.entityhandlers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ids.idsuserapp.R;
import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.viewmodel.ArcoViewModel;
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

    public void creaBeacon(String nome, String quota, double larghezza, int x, int y, /*int x_meter, int y_meter,*/ int mappa, String device) {
        //Beacon beacon = new Beacon(nome, quota,larghezza,x,y,x_meter,y_meter,mappa);
        Beacon beacon = new Beacon(nome, quota, larghezza,x,y,mappa, device);
        beaconViewModel.insert(beacon);
    }


    public void creaBeaconServer(String nome, String quota, double larghezza, int x, int y, /*int x_meter, int y_meter,*/ String mappa) {
        //JsonObjectRequest newBeaconJSONRequest = preparePostBeaconRequest(nome,quota,larghezza,x,y,x_meter,y_meter,mappa);
        JsonObjectRequest newBeaconJSONRequest = preparePostBeaconRequest(nome,quota,larghezza,x,y,mappa);
        serverRequestQueue.add(newBeaconJSONRequest);
    }

    public JsonObjectRequest prepareGetBeaconRequest() {
        String bcns_url = context.getString(R.string.api_beacons);
        return new JsonObjectRequest(Request.Method.GET, bcns_url, null,
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


    public JsonObjectRequest preparePostBeaconRequest(String nome, String quota, double larghezza, int x, int y, /*int x_meter, int y_meter,*/ String mappa) {
        String beacons_url = context.getString(R.string.api_beacons);
        // JSONObject newBeacon = createNewBeaconJson(nome,quota,larghezza,x,y,x_meter,y_meter,mappa);
        JSONObject newBeacon = createNewBeaconJson(nome,quota,larghezza,x,y,mappa);
        return new JsonObjectRequest(Request.Method.POST, beacons_url, newBeacon,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("VolleyError", error.toString());
                error.printStackTrace();
                Toast toast = Toast.makeText(context, "Errore durante la creazione dei dati Beacon!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private JsonObjectRequest prepareDeleteBeaconRequest(final int beacon_id) {
        String delete_beacons_url = context.getString(R.string.api_beacons) + "/"+Integer.toString(beacon_id);
        return new JsonObjectRequest(Request.Method.DELETE, delete_beacons_url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast toast = Toast.makeText(context, "Beacon eliminato con successo.", Toast.LENGTH_SHORT);
                        toast.show();
                        eliminaBeaconLocale(beacon_id);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if(networkResponse == null){
                    Toast toast = Toast.makeText(context, "Beacon eliminato con successo.", Toast.LENGTH_SHORT);
                    toast.show();
                    eliminaBeaconLocale(beacon_id);
                }else {
                    Toast toast = Toast.makeText(context, "Errore durante l'eliminazione del Beacon.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void eliminaBeaconLocale(int beacon_id) {
        beaconViewModel.deleteByMappa(beacon_id);
        Beacon beacon = beaconViewModel.trova(beacon_id);
        beaconViewModel.delete(beacon);
    }

    private JSONObject createNewBeaconJson(String nome, String quota, double larghezza, int x, int y,/* int x_meter, int y_meter,*/ String mappa) {
        JSONObject newBeacon = new JSONObject();
        try {
            newBeacon.put("name", nome);
            newBeacon.put("floor", quota);
            newBeacon.put("width", larghezza);
            newBeacon.put("x", x);
            newBeacon.put("y", y);
            //  newBeacon.put("x_meter", x_meter);
            // newBeacon.put("y_meter", y_meter);
            newBeacon.put("mappa", mappa);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newBeacon;
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

    public void deleteBeacon(int id){
        JsonObjectRequest deleteBeaconRequest = prepareDeleteBeaconRequest(id);
        serverRequestQueue.add(deleteBeaconRequest);

    }

    public void putBeacon(int  idBeacon, String nomeBeacon, String quota, double larghezza, int x, int y, /*int x_meter, int y_meter,*/ String mappa){
        JsonObjectRequest putBeaconRequest = preparePutBeaconRequest( idBeacon, nomeBeacon, quota,larghezza,x,y,mappa);
        //JsonObjectRequest putBeaconRequest = preparePutBeaconRequest( idBeacon, nomeBeacon, quota,larghezza,x,y,x_meter,y_meter,mappa);
        serverRequestQueue.add(putBeaconRequest);
    }

    public JsonObjectRequest preparePutBeaconRequest(final int idBeacon, String nomeBeacon, String quota, double larghezza, int x, int y, /*int x_meter, int y_meter,*/ String mappa) {
        String bcns_url = context.getString(R.string.api_beacons) + "/" + Integer.toString(idBeacon);
        JSONObject newBeacon = createNewBeaconJson(nomeBeacon,quota,larghezza,x,y,mappa);
        //JSONObject newBeacon = createNewBeaconJson(nomeBeacon,quota,larghezza,x,y,x_meter,y_meter,mappa);
        return new JsonObjectRequest(Request.Method.PUT, bcns_url, newBeacon,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        modificaBeaconLocale(parseBeaconToStringArray(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("VolleyError", error.toString());

                Toast toast = Toast.makeText(context, "Errore durante la modifica dei dati Mappa!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private ArrayList<String> parseBeaconToStringArray(JSONObject beacon){
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

    //metodo per modificare la mappa in locale
    public void modificaBeaconLocale(ArrayList<String> datiBeacon) {
        Beacon beacon = new Beacon(datiBeacon);
        int bcn_id = Integer.parseInt(datiBeacon.get(0));
        String bcn_name = datiBeacon.get(1);
        String bcn_quota = datiBeacon.get(4);
        Double bcn_larghezza = Double.parseDouble(datiBeacon.get(6));
        int bcn_x = Integer.parseInt(datiBeacon.get(2));
        int bcn_y = Integer.parseInt(datiBeacon.get(3));
        // int bcn_x_meter = Integer.parseInt(datiBeacon.get(6));
        // int bcn_y_meter = Integer.parseInt(datiBeacon.get(7));
        beaconViewModel.update(beacon);


    }


}
