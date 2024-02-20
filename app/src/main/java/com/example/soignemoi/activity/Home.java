package com.example.soignemoi.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.soignemoi.R;
import com.example.soignemoi.adapter.AdapterEDT;
import com.example.soignemoi.entity.EDT;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Home extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private final String URL = "http://192.168.1.101:8000/api/medecin/edts";

    private ListView listview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("res", response.toString());

                        Gson gson = new Gson();
                        ArrayList<EDT> stringList = gson.fromJson(response.toString(), new TypeToken<List<EDT>>() {}.getType());

                        AdapterEDT adapter = new AdapterEDT(Home.this, 0, stringList);

                        listview = (ListView) findViewById(R.id.listView);
                        listview.setAdapter(adapter);

                        listview.setOnItemClickListener(Home.this);
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

                            Intent intent = new Intent(Home.this, Login.class);
                            startActivity(intent);
//                            finish();
                        } else {
                            Toast.makeText(Home.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("Clicked !", "You clicked Item : " + id + " at position : " + position);
        Intent intent = new Intent();
        intent.setClass(this, NavBarActivity.class);
        intent.putExtra("edt", new Gson().toJson(listview.getItemAtPosition(position)));
        startActivity(intent);
    }
}