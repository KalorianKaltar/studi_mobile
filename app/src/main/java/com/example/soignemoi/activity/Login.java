package com.example.soignemoi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.soignemoi.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private String email, password;
    private final String URL = "http://192.168.1.101:8000/api/login_check";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = password = "";
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);

        SharedPreferences preferences = getSharedPreferences("StudiSoigneMoi",Context.MODE_PRIVATE);
        String token  = preferences.getString("token",null);//second parameter default value.

        if(token != null && !token.equals("")) {
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
            finish();
        }
    }

    public void login(View view) {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (!email.equals("") && !password.equals("")) {
            try {
                JSONObject jsonParams = new JSONObject();
                jsonParams.put("username", email);
                jsonParams.put("password", password);
                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL,
                        jsonParams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String token = response.getString("token");
                                    if (!token.equals("")) {

                                        SharedPreferences.Editor edit;
                                        SharedPreferences prefs = getSharedPreferences("StudiSoigneMoi", Context.MODE_PRIVATE);
                                        edit = prefs.edit();

                                        edit.putString("token", token);
                                        Log.i("Login", token);
                                        edit.commit();


                                        Log.d("res", response.toString());
                                        Intent intent = new Intent(Login.this, Home.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(Login.this, "Invalid Login Id/Password", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("err", error.toString());
                                Toast.makeText(Login.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                            }
                        });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);


            } catch (JSONException ex) {
                // Catch if something went wrong with the params
            }
        } else {
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
        }
    }
}