package com.example.soignemoi.activity.ui.prescription;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.soignemoi.R;
import com.example.soignemoi.activity.Login;
import com.example.soignemoi.activity.NavBarActivity;
import com.example.soignemoi.adapter.AdapterAvis;
import com.example.soignemoi.adapter.AdapterPrescription;
import com.example.soignemoi.databinding.FragmentPrescriptionBinding;
import com.example.soignemoi.entity.Avis;
import com.example.soignemoi.entity.EDT;
import com.example.soignemoi.entity.Prescription;
import com.example.soignemoi.listeners.OnMyFragmentListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrescriptionFragment extends Fragment {

    private final String URL = "http://192.168.1.101:8000/api/medecin/prescription";
    private FragmentPrescriptionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        /*OnBackPressedCallback callback = new OnBackPressedCallback(true *//* enabled by default *//*) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().onb
                startActivity(new Intent(NavBarActivity.this, Home.class));
                requireActivity().finish();
            }
        };*/
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        PrescriptionViewModel prescriptionViewModel =
                new ViewModelProvider(this).get(PrescriptionViewModel.class);

        binding = FragmentPrescriptionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        (((NavBarActivity) getActivity()).getSupportActionBar()).setTitle("Prescription");

        getPrescriptioon();

//        final TextView textView = binding.labelListPrescription;
//        prescriptionViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    OnMyFragmentListener mListener;

    // Where is this method called??
    public void setOnMyFragmentListener(OnMyFragmentListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMyFragmentListener) {
            mListener = (OnMyFragmentListener) context;
            mListener.onChangeToolbarTitle("Prescription"); // Call this in `onResume()`
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume(){
        super.onResume();
        mListener.onChangeToolbarTitle("Prescription");
    }

    private void getPrescriptioon() {
        EDT edt = ((NavBarActivity) getActivity()).getEdt();

        Uri uri = Uri.parse(URL).buildUpon().appendPath(edt.idSejour.id).build();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HttpClient", "success! response: " + response.toString());

                        Gson gson = new Gson();
                        ArrayList<Prescription> stringList = gson.fromJson(response.toString(), new TypeToken<List<Prescription>>() {}.getType());

                        AdapterPrescription adapter = new AdapterPrescription(getActivity(), 0, stringList);

                        final ListView listview = (ListView) getActivity().findViewById(R.id.listViewPrescription);
                        listview.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("edt",edt.idSejour.id);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                SharedPreferences preferences = getActivity().getSharedPreferences("StudiSoigneMoi", Context.MODE_PRIVATE);
                String token  = preferences.getString("token",null);//second parameter default value.

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };

        /*JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("res", response.toString());

                        Gson gson = new Gson();
                        ArrayList<Prescription> stringList = gson.fromJson(response.toString(), new TypeToken<List<Prescription>>() {}.getType());

                        AdapterPrescription adapter = new AdapterPrescription(getActivity(), 0, stringList);

                        final ListView listview = (ListView) getActivity().findViewById(R.id.listViewPrescription);
                        listview.setAdapter(adapter);

//                        listview.setOnItemClickListener(AvisFragment.this);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("err", error.toString());
                        if(error instanceof AuthFailureError) {
                            SharedPreferences preferences = PrescriptionFragment.this.requireContext().getSharedPreferences("StudiSoigneMoi",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.remove("token");
                            editor.apply();

                            Intent intent = new Intent(PrescriptionFragment.this.requireContext(), Login.class);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences preferences = getActivity().getSharedPreferences("StudiSoigneMoi", Context.MODE_PRIVATE);
                String token  = preferences.getString("token",null);//second parameter default value.

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };*/
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }
}