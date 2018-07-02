package com.ids.idsuserapp.entityhandlers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ids.idsuserapp.HomeActivity;
import com.ids.idsuserapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class UserLoginHandler {

    private Context context;
    private com.android.volley.RequestQueue serverRequestQueue;


    public UserLoginHandler(Context context) {
        this.context = context;
        serverRequestQueue = Volley.newRequestQueue(context);
    }



    public void loginUserServer(String email, String password){
        JsonObjectRequest newLoginJSONRequest = preparePostLoginRequest(email,password);
        serverRequestQueue.add(newLoginJSONRequest);
    }

    public JsonObjectRequest preparePostLoginRequest(String email, String password) {
        String login_url = context.getString(R.string.api_login);
        JSONObject newLoginUser = createNewLoginUserJson(email,password);
        return new JsonObjectRequest(Request.Method.POST, login_url, newLoginUser,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context,HomeActivity.class);
                        context.startActivity(intent);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("VolleyError", error.toString());
                error.printStackTrace();
                Toast toast = Toast.makeText(context, "Errore, Email o Password errati!", Toast.LENGTH_SHORT);
                toast.show();
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
