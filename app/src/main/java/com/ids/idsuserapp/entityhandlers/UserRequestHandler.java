package com.ids.idsuserapp.entityhandlers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ids.idsuserapp.HomeActivity;
import com.ids.idsuserapp.R;
import com.ids.idsuserapp.autentication.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRequestHandler {


    private Context context;
    private com.android.volley.RequestQueue serverRequestQueue;


    public UserRequestHandler(Context context) {
        this.context = context;
        serverRequestQueue = Volley.newRequestQueue(context);
    }

    public void creaUserServer(String email, String password) {
        JsonObjectRequest newUserJSONRequest = preparePostUserRequest(email, password);
        serverRequestQueue.add(newUserJSONRequest);
    }

    public JsonObjectRequest preparePostUserRequest(String email, String password) {
        String registration_url = context.getString(R.string.api_registrazione);
        JSONObject newUser = createNewUserJson(email, password);
        return new JsonObjectRequest(Request.Method.POST, registration_url, newUser,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);

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

    public void loginUserServer(String email, String password) {
        JsonObjectRequest newLoginJSONRequest = preparePostLoginRequest(email, password);
        serverRequestQueue.add(newLoginJSONRequest);
    }

    public JsonObjectRequest preparePostLoginRequest(String email, String password) {
        String login_url = context.getString(R.string.api_login);
        JSONObject newLoginUser = createNewLoginUserJson(email, password);
        return new JsonObjectRequest(Request.Method.POST, login_url, newLoginUser,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SharedPreferences sharedPref = context.getSharedPreferences(
                                                     context.getString(R.string.auth_preference), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        try {
                            String token = response.get("token").toString();
                            editor.putString("token", token);
                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Errore nel login. Riprovare", Toast.LENGTH_SHORT).show();
                        }
                        Log.v("TOKEN", sharedPref.getString("token", ""));
                        Intent intent = new Intent(context, HomeActivity.class);
                        context.startActivity(intent);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("VolleyError", error.toString());
                int statusCode = error.networkResponse.statusCode;
                switch (statusCode) {
                    case 401:
                        Toast.makeText(context,"Credenziali errate.Riprovare", Toast.LENGTH_LONG).show();
                        break;
                }
                error.printStackTrace();
            }
        });
    }


    private JSONObject createNewLoginUserJson(String email, String password) {
        JSONObject loginUser = new JSONObject();
        try {
            loginUser.put("email", email);
            loginUser.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return loginUser;
    }


}
