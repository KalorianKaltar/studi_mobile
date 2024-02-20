package com.example.soignemoi.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.soignemoi.R;
import com.example.soignemoi.entity.EDT;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FormulaireAvis extends AppCompatActivity {

    private final String URL = "http://192.168.1.101:8000/api/medecin/avis";

    private EditText etLibelle, etDescription;

    private String libelle;
    private String description;
    private EDT edt;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String edtStr = extras.getString("edt");
            if(edtStr != null) {
                this.edt = new Gson().fromJson(edtStr, new TypeToken<EDT>() {}.getType());
            }
        }

        setContentView(R.layout.activity_formulaire_avis);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        this.setSupportActionBar(myToolbar);
        this.toolbar = getSupportActionBar();

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
        );
        toolbar.setCustomView(toolbar.getCustomView(), params);
        toolbar.setDisplayShowCustomEnabled(true);
        toolbar.setDisplayShowTitleEnabled(true);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Avis");

        etLibelle = findViewById(R.id.libelle);
        etDescription  = findViewById(R.id.description);
    }

    public void save(View view) {
        libelle = etLibelle.getText().toString().trim();
        description = etDescription.getText().toString().trim();

        if (!libelle.equals("") && !description.equals("")) {
            try {
                JSONObject jsonParams = new JSONObject();
                jsonParams.put("libelle", libelle);
                jsonParams.put("description", description);
                jsonParams.put("idSejour", edt.idSejour.id);
                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL,
                        jsonParams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("res", response.toString());
                                Intent intent = new Intent(FormulaireAvis.this, Home.class);
                                startActivity(intent);
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("err", error.toString());
                                if(error instanceof AuthFailureError) {
                                    SharedPreferences preferences = getSharedPreferences("StudiSoigneMoi",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.remove("token");
                                    editor.apply();

                                    Intent intent = new Intent(FormulaireAvis.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(FormulaireAvis.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        SharedPreferences preferences = getSharedPreferences("StudiSoigneMoi",Context.MODE_PRIVATE);
                        String token  = preferences.getString("token",null);//second parameter default value.

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json; charset=UTF-8");
                        params.put("Authorization", "Bearer " + token);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);


            } catch (JSONException ex) {
                // Catch if something went wrong with the params
            }
        } else {
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
        }

        Log.d("save", "prescription");

    }
}