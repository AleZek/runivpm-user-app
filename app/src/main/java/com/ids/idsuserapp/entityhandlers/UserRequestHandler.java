package com.ids.idsuserapp.entityhandlers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ids.idsuserapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRequestHandler {


    private Context context;
    private DataRetriever dataRetriever;
    private com.android.volley.RequestQueue serverRequestQueue;


    public UserRequestHandler(Context context) {
        this.context = context;
        dataRetriever = (DataRetriever) context;
        serverRequestQueue = Volley.newRequestQueue(context);
    }

   /* public void retrieveUserDataset() {
        JsonObjectRequest userRequest = prepareGetUserRequest();
        serverRequestQueue.add(userRequest);
    }

    */



    public JsonObjectRequest preparePostUserRequest(String email, String password) {
        String registration_url = context.getString(R.string.api_registrazione);
        JSONObject newUser = createNewUserJson(email,password);
        return new JsonObjectRequest(Request.Method.POST, registration_url, newUser,
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
                Toast toast = Toast.makeText(context, "Errore!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


    private JSONObject createNewUserJson(String email, String password) {
        JSONObject newUser = new JSONObject();
        try {
            newUser.put("email", email);
            newUser.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newUser;
    }

}
