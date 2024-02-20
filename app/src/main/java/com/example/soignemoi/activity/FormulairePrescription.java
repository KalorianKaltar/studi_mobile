package com.example.soignemoi.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FormulairePrescription extends AppCompatActivity {

    private final String URL = "http://192.168.1.101:8000/api/medecin/prescription";

    private EditText etMedicament, etPosologie, etDateFin;

    private String medicament;
    private String posologie;
    private String dateFin;
    private Date date;
    private EDT edt;

    DatePickerDialog picker;
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

        setContentView(R.layout.activity_formulaire_prescription);


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
        toolbar.setTitle("Prescription");

        etMedicament = findViewById(R.id.medicament);
        etPosologie = findViewById(R.id.posologie);
        etDateFin = findViewById(R.id.date_fin);

        etDateFin.setInputType(InputType.TYPE_NULL);
        etDateFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(FormulairePrescription.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                etDateFin.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                // Create a Calendar instance

                                Calendar mCalendar = Calendar.getInstance();

                                // Set static variables of Calendar instance

                                mCalendar.set(Calendar.YEAR,year);

                                mCalendar.set(Calendar.MONTH,month);

                                mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                                // Get the date in form of string

                                String selectedDate = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRENCH).format(mCalendar.getTime());

                                // Set the textview to the selectedDate String

                                etDateFin.setText(selectedDate);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

    }

    public void save(View view) {
        medicament = etMedicament.getText().toString().trim();
        posologie = etPosologie.getText().toString().trim();
        dateFin = etDateFin.getText().toString().trim();


        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            date = sdf.parse(dateFin);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }

        if (!medicament.equals("") && !posologie.equals("") && !dateFin.equals("")) {
            try {
                JSONObject jsonParams = new JSONObject();
                jsonParams.put("medicament", medicament);
                jsonParams.put("posologie", posologie);
                jsonParams.put("dateFin", date);
                jsonParams.put("idSejour", edt.idSejour.id);
                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL,
                        jsonParams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("res", response.toString());
                                Intent intent = new Intent(FormulairePrescription.this, Home.class);
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

                                    Intent intent = new Intent(FormulairePrescription.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(FormulairePrescription.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
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