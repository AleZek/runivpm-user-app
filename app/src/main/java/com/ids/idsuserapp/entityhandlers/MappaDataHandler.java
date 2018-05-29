package com.ids.idsuserapp.entityhandlers;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ids.idsuserapp.R;
import com.ids.idsuserapp.db.entity.Mappa;
import com.ids.idsuserapp.utils.FileHelper;
import com.ids.idsuserapp.viewmodel.BeaconViewModel;
import com.ids.idsuserapp.viewmodel.MappaViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MappaDataHandler {
    private Context context;
    private MappaViewModel mappaViewModel;
    private BeaconViewModel beaconViewModel;
    private com.android.volley.RequestQueue serverRequestQueue;
    private Uri nuovaMappaFilePath;


    public MappaDataHandler(Context context, MappaViewModel mappaViewModel, BeaconViewModel beaconViewModel) {
        this.context = context;
        this.mappaViewModel = mappaViewModel;
        this.beaconViewModel = beaconViewModel;
        serverRequestQueue = Volley.newRequestQueue(context);
    }

    public void retrieveMappeDataset() {
        JsonObjectRequest mappeRequest = prepareGetMappeRequest();
        serverRequestQueue.add(mappeRequest);
    }


    public void creaMappaLocale(ArrayList<String> datiMappa) {
        Mappa mappa = new Mappa(datiMappa);
        String img_name = datiMappa.get(2);
        String mappa_id = datiMappa.get(1);
        if (!img_name.equals("null")){
            retrieveMappaImageData(mappa_id,img_name);
        }
        mappaViewModel.insert(mappa);

    }

    public JsonObjectRequest prepareGetMappeRequest() {
        String maps_url = context.getString(R.string.api_mappas);
        return new JsonObjectRequest(Request.Method.GET, maps_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        try {
                            persistMappeCollectionLocally(response.getJSONArray("hydra:member"));
                            downloadMapImages(response.getJSONArray("hydra:member"));
                            //persistCollection(response.get("hydra:member"));
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

    public JsonObjectRequest prepareGetMappaImageRequest(String mappa_id, final String image_name) {
        String url = context.getString(R.string.api_mappa_image) + mappa_id ;
        return new JsonObjectRequest(Request.Method.GET, url, null,
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

    public JsonObjectRequest preparePostMappaRequest(String nomeMappa) {
        String maps_url = context.getString(R.string.api_mappas);
        JSONObject newMappa = createNewMapJson(nomeMappa);
        return new JsonObjectRequest(Request.Method.POST, maps_url, newMappa,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JsonObjectRequest imageRequest= null;
                        try {
                            if (nuovaMappaFilePath != null)
                                imageRequest = preparePostMappaImageRequest(response.getString("id"));
                            else
                                creaMappaLocale(parseMappaToStringArray(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (imageRequest != null) {
                            serverRequestQueue.add(imageRequest);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("VolleyError", error.toString());

                Toast toast = Toast.makeText(context, "Errore durante la creazione dei dati Mappa!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public JsonObjectRequest preparePostMappaImageRequest(String mappa_id) {
        String maps_url = context.getString(R.string.api_mappa_image) + mappa_id;
        JSONObject newMappaImage = createNewMapImageJson();
        return new JsonObjectRequest(Request.Method.POST, maps_url, newMappaImage,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast toast = Toast.makeText(context, "Mappa caricata con successo.", Toast.LENGTH_SHORT);
                        toast.show();
                        creaMappaLocale(parseMappaToStringArray(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast toast = Toast.makeText(context, "Errore durante la creazione dell' immagine Mappa.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public JsonObjectRequest prepareUpdateMappaImageRequest(final String  mappa_id) {
        String maps_url = context.getString(R.string.api_mappa_image) + mappa_id;
        JSONObject newMappaImage = createNewMapImageJson();
        return new JsonObjectRequest(Request.Method.PUT, maps_url, newMappaImage,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast toast = Toast.makeText(context, "Mappa aggiornata con successo.", Toast.LENGTH_SHORT);
                        toast.show();
                        deleteMappaImage(mappa_id);
                        modificaMappaLocale(parseMappaToStringArray(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast toast = Toast.makeText(context, "Errore durante la creazione dell' immagine Mappa.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private JsonObjectRequest prepareDeleteMappaRequest(final int mappa_id) {
        String delete_maps_url = context.getString(R.string.api_delete_mappa) + Integer.toString(mappa_id);
        JSONObject newMappaImage = createNewMapImageJson();
        return new JsonObjectRequest(Request.Method.DELETE, delete_maps_url, newMappaImage,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast toast = Toast.makeText(context, "Mappa eliminata con successo.", Toast.LENGTH_SHORT);
                        toast.show();
                        eliminaMappaLocale(mappa_id);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("VolleyError", error.toString());
                Toast toast = Toast.makeText(context, "Errore durante l'eliminazione della Mappa.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void eliminaMappaLocale(int mappa_id) {
        beaconViewModel.deleteByMappa(mappa_id);
        Mappa mappa = mappaViewModel.find(mappa_id);
        String image_name = null;
        if (mappa != null) {
            image_name = mappa.getImmagine();
        }
        deleteMappaImage(image_name);
        mappaViewModel.delete(mappa);
    }

    private void deleteMappaImage(String image_name) {
        FileHelper fileHelper = new FileHelper(context);
        fileHelper.deleteFile(Environment.getExternalStorageDirectory().toString() + context.getString(R.string.directory_mappe) + image_name);
    }

    public JSONObject createNewMapImageJson() {
        JSONObject imageJSON = null;
        String imgData = getNewMappaImageData();
        try {

            imageJSON = new JSONObject(getNewMappaImageData());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return imageJSON;
    }

    private String getNewMappaImageData() {
        FileHelper fileHelper = new FileHelper(context);
        if(nuovaMappaFilePath != null) {
            byte[] imageData = fileHelper.readFromFile(nuovaMappaFilePath);
            return "{\"image\":\"" + fileHelper.base64Encode(imageData) + "\"}";
        }

        return "{\"image\":\"\"}";
    }

    private JSONObject createNewMapJson(String nomeMappa) {
        JSONObject newMappa = new JSONObject();
        try {
            newMappa.put("name", nomeMappa);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newMappa;
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

    public void setNuovaMappaFilePath(Uri nuovaMappaFilePath) {
        this.nuovaMappaFilePath = nuovaMappaFilePath;
    }

    public void postMappa(String nomeMappa) {
        JsonObjectRequest postMappaRequest = preparePostMappaRequest(nomeMappa);
        serverRequestQueue.add(postMappaRequest);
    }

    public void putMappa(int mappa_id, String nomeMappa){
        JsonObjectRequest putMappaRequest = preparePutMappaRequest(mappa_id, nomeMappa);
        serverRequestQueue.add(putMappaRequest);
    }

    public void deleteMappa(int id){
        JsonObjectRequest deleteMappaRequest = prepareDeleteMappaRequest(id);
        serverRequestQueue.add(deleteMappaRequest);

    }


    public JsonObjectRequest preparePutMappaRequest(final int mappa_id, String nomeMappa) {
        String maps_url = context.getString(R.string.api_mappas) + "/" + Integer.toString(mappa_id);
        JSONObject newMappa = createNewMapJson(nomeMappa);
        return new JsonObjectRequest(Request.Method.PUT, maps_url, newMappa,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JsonObjectRequest imageRequest= null;
                        try {
                            if (nuovaMappaFilePath != null)
                                imageRequest = prepareUpdateMappaImageRequest(response.getString("id"));
                            else
                                modificaMappaLocale(parseMappaToStringArray(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (imageRequest != null) {
                            serverRequestQueue.add(imageRequest);
                        }
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

   /* public JsonObjectRequest preparePutMappaImageRequest(String mappa_id) {
        String maps_url = context.getString(R.string.api_mappa_image) + mappa_id;
        JSONObject newMappaImage = createNewMapImageJson();
        return new JsonObjectRequest(Request.Method.PUT, maps_url, newMappaImage,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast toast = Toast.makeText(context, "Mappa modificata con successo.", Toast.LENGTH_SHORT);
                        toast.show();
                        modificaMappaLocale(parseMappaToStringArray(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast toast = Toast.makeText(context, "Errore durante la modifica dell'immagine Mappa.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }*/


    //metodo per modificare la mappa in locale
    public void modificaMappaLocale(ArrayList<String> datiMappa) {
        Mappa mappa = new Mappa(datiMappa);
        String img_name = datiMappa.get(2);
        String mappa_id = datiMappa.get(1);
        if (!img_name.equals("null")){
            retrieveMappaImageData(mappa_id,img_name);
        }
        mappaViewModel.update(mappa);

    }




}
