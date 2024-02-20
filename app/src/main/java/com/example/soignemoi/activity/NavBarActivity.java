package com.example.soignemoi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.example.soignemoi.R;
import com.example.soignemoi.activity.ui.patient.PatientViewModel;
import com.example.soignemoi.activity.ui.prescription.PrescriptionViewModel;
import com.example.soignemoi.activity.ui.avis.AvisViewModel;
import com.example.soignemoi.entity.EDT;
import com.example.soignemoi.listeners.OnMyFragmentListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.soignemoi.databinding.ActivityNavBarBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class NavBarActivity extends AppCompatActivity implements OnMyFragmentListener {

    private PatientViewModel patientViewModel;
    private PrescriptionViewModel prescriptionViewModel;
    private AvisViewModel avisViewModel;


    private ActivityNavBarBinding binding;

    private EDT edt;

    private ActionBar toolbar;

    @Override
    public void onSaveInstanceState(Bundle state) {
        state.putString("edt", new Gson().toJson(edt));

        super.onSaveInstanceState(state);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        edt = new Gson().fromJson(savedInstanceState.getString("edt"), new TypeToken<EDT>() {}.getType());
    }

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

        binding = ActivityNavBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        this.setSupportActionBar(myToolbar);
        this.toolbar = getSupportActionBar();

        toolbar.setDisplayHomeAsUpEnabled(true);

        this.setSupportActionBar(new BottomAppBar(this));






        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_patient, R.id.navigation_prescription, R.id.navigation_avis)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_nav_bar);


        AppBarConfiguration toolBarConfiguration = new AppBarConfiguration.Builder(
                navController.getGraph()).build();
        NavigationUI.setupWithNavController(
                myToolbar, navController, toolBarConfiguration);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);



        getOnBackPressedDispatcher()
                .addCallback(this,
                            new OnBackPressedCallback(true) {

                                @Override
                                public void handleOnBackPressed() {
                               Log.d("test", "test");
                                }
                            });

    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.id.my_toolbar, menu);
        return true;
    }*/
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }



//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Ward/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//        }
//        return false;
//    }

    public EDT getEdt() {
        return edt;
    }

    @Override
    public void onChangeToolbarTitle(String title) {
        if(toolbar != null) {
            toolbar.setTitle(title);
            toolbar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void createPrescription(View view) {
        Log.d("create", "Prescription");
        Intent intent = new Intent();
        intent.setClass(this, FormulairePrescription.class);
        intent.putExtra("edt", new Gson().toJson(edt));
        startActivity(intent);
    }

    public void createAvis(View view) {
        Log.d("create", "Avis");
        Intent intent = new Intent();
        intent.setClass(this, FormulaireAvis.class);
        intent.putExtra("edt", new Gson().toJson(edt));
        startActivity(intent);
    }
}