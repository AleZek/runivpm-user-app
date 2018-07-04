package com.ids.idsuserapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ids.idsuserapp.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthenticatedJsonObjectRequest extends JsonObjectRequest{
    Context context;

    public AuthenticatedJsonObjectRequest(Context context, int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        this.context = context;
        }

    /**
     * Header con il token per l'autenticazione
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        SharedPreferences authPref = context.getSharedPreferences(context.getString(R.string.auth_preference), Context.MODE_PRIVATE);
        String token = authPref.getString("token", "");
        headers.put("Authorization", "Bearer " + token);
        return headers;
    }
}
