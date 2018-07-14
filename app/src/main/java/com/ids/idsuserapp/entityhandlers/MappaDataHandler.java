package com.ids.idsuserapp.entityhandlers;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ids.idsuserapp.R;
import com.ids.idsuserapp.db.entity.Mappa;
import com.ids.idsuserapp.utils.AuthenticatedJsonObjectRequest;
import com.ids.idsuserapp.utils.FileHelper;
import com.ids.idsuserapp.viewmodel.MappaViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MappaDataHandler {
    private Context context;
    private MappaViewModel mappaViewModel;
    private DataRetriever dataRetriever;
    private com.android.volley.RequestQueue serverRequestQueue;

    public MappaDataHandler(Context context, MappaViewModel mappaViewModel) {
        this.context = context;
        dataRetriever = (DataRetriever) context;
        this.mappaViewModel = mappaViewModel;
        serverRequestQueue = Volley.newRequestQueue(context);
    }

    public void retrieveMappeDataset() {
        JsonObjectRequest mappeRequest = prepareGetMappeRequest();
        serverRequestQueue.add(mappeRequest);
    }

    public AuthenticatedJsonObjectRequest prepareGetMappeRequest() {
        String maps_url = context.getString(R.string.api_mappas);
        return new AuthenticatedJsonObjectRequest(context, Request.Method.GET, maps_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        try {
                            persistMappeCollectionLocally(response.getJSONArray("hydra:member"));
                            downloadMapImages(response.getJSONArray("hydra:member"));
                            dataRetriever.retrieveBeacons();
                            //persistCollection(response.get("hydra:member"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
        };
    }

    private void downloadMapImages(JSONArray mappe) {

        for (int i = 0; i<mappe.length();i++) {
            ArrayList<String> nuovaMappaDataStrings = null;
            String mappa_id = "";
            String image_name = "";
            try {
                JSONObject mappa = mappe.getJSONObject(i);
                mappa_id = mappa.getString("id");
                image_name = mappa.getString("image");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            FileHelper fileHelper = new FileHelper(context);
            String directory_mappe = context.getResources().getString(R.string.directory_mappe);
            if (!mappa_id.equals("") && !fileHelper.fileExists(directory_mappe + image_name)) {
                retrieveMappaImageData(mappa_id, image_name);
            }
        }
    }

    private void storeMappaImage(String img_data, String img_name) {
        FileHelper fileHelper = new FileHelper(context);
        String directory_mappe = context.getResources().getString(R.string.directory_mappe);
        byte[] img_bytes = fileHelper.base64Decode(img_data);
        fileHelper.saveFile(img_name, directory_mappe, img_bytes);
    }

    private void retrieveMappaImageData(String mappa_id, String image_name) {
        JsonObjectRequest get_mappa_image_request = prepareGetMappaImageRequest(mappa_id, image_name);
        serverRequestQueue.add(get_mappa_image_request);
    }

    public AuthenticatedJsonObjectRequest prepareGetMappaImageRequest(String mappa_id, final String image_name) {
        String url = context.getString(R.string.api_mappa_image) + mappa_id ;
        return new AuthenticatedJsonObjectRequest(context, Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        try {
                            if (!image_name.equals("null"))
                                storeMappaImage(response.getString("img"), image_name);
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

    private void persistMappeCollectionLocally(JSONArray mappe) {
        for (int i = 0; i<mappe.length();i++) {
            ArrayList<String> nuovaMappaDataStrings = null;
            try {
                nuovaMappaDataStrings = parseMappaToStringArray(mappe.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mappaViewModel.insert(new Mappa(nuovaMappaDataStrings));

        }
    }

    private ArrayList<String> parseMappaToStringArray(JSONObject mappa){
        ArrayList<String> datiNuovaMappa = new ArrayList<>();
        for( String campo : MappaViewModel.CAMPI){
            try {
                datiNuovaMappa.add(mappa.getString(campo));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return datiNuovaMappa;
    }




}
