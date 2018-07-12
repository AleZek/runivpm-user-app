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
import com.ids.idsuserapp.authentication.LoginActivity;
import com.ids.idsuserapp.utils.AuthenticatedJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRequestHandler {


    private Context context;
    private ProgressInterface progressInterface;
    private com.android.volley.RequestQueue serverRequestQueue;


    public UserRequestHandler(Context context) {
        this.context = context;
        if(context instanceof ProgressInterface)
            progressInterface = (ProgressInterface) context;
        serverRequestQueue = Volley.newRequestQueue(context);
    }

    public void creaUserServer(String email, String password) {
        progressInterface.showProgressBar("Registrazione","Registrazione in corso...");
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
                        progressInterface.hideProgressBar();
                        Toast.makeText(context, "Registrazione completata", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressInterface.hideProgressBar();
                Log.v("VolleyError", error.toString());
                int statusCode = error.networkResponse.statusCode;
                switch (statusCode) {
                    case 400:
                        Toast.makeText(context, "Errore nella richiesta", Toast.LENGTH_SHORT).show();
                        break;
                    case 409:
                        Toast.makeText(context, "Email già presente nel nostro sistema", Toast.LENGTH_SHORT).show();
                        break;
                }
                error.printStackTrace();
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
        progressInterface.showProgressBar("Login", "Login in corso...");
        JsonObjectRequest newLoginJSONRequest = preparePostLoginRequest(email, password);
        serverRequestQueue.add(newLoginJSONRequest);
    }

    public void logoutUserServer() {
        JsonObjectRequest logoutJSONRequest = preparePostLogoutRequest();
        serverRequestQueue.add(logoutJSONRequest);
    }

    private AuthenticatedJsonObjectRequest preparePostLogoutRequest() {
        String logout_url = context.getString(R.string.api_logout);

        return new AuthenticatedJsonObjectRequest(context, Request.Method.POST, logout_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SharedPreferences sharedPref = context.getSharedPreferences(
                                context.getString(R.string.auth_preference), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("token", "");
                        editor.apply();
                        sharedPref = context.getSharedPreferences(
                                context.getString(R.string.local_position), Context.MODE_PRIVATE);
                        editor = sharedPref.edit();
                        editor.putString("position", "");
                        editor.apply();
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
                        break;
                }
                error.printStackTrace();
            }
        });

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
                            progressInterface.hideProgressBar();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressInterface.hideProgressBar();
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
                        progressInterface.hideProgressBar();
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


    public interface ProgressInterface {
        void showProgressBar(String title, String message);
        void hideProgressBar();
    }




}
