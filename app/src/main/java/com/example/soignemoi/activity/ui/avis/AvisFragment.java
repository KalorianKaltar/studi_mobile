package com.example.soignemoi.activity.ui.avis;

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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.soignemoi.databinding.FragmentAvisBinding;
import com.example.soignemoi.entity.Avis;
import com.example.soignemoi.entity.EDT;
import com.example.soignemoi.listeners.OnMyFragmentListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvisFragment extends Fragment {

    private final String URL = "http://192.168.1.101:8000/api/medecin/avis";

    private FragmentAvisBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AvisViewModel avisViewModel =
                new ViewModelProvider(this).get(AvisViewModel.class);

        binding = FragmentAvisBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        getAvis();

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
            mListener.onChangeToolbarTitle("Avis"); // Call this in `onResume()`
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
        mListener.onChangeToolbarTitle("Avis");
    }

    private void getAvis() {
        EDT edt = ((NavBarActivity) getActivity()).getEdt();

        Uri uri = Uri.parse(URL).buildUpon().appendPath(edt.idSejour.id).build();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HttpClient", "success! response: " + response.toString());

                        Gson gson = new Gson();
                        ArrayList<Avis> stringList = gson.fromJson(response.toString(), new TypeToken<List<Avis>>() {}.getType());

                        AdapterAvis adapter = new AdapterAvis(getActivity(), 0, stringList);

                        final ListView listview = (ListView) getActivity().findViewById(R.id.listViewAvis);
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
                        ArrayList<Avis> stringList = gson.fromJson(response.toString(), new TypeToken<List<Avis>>() {}.getType());

                        AdapterAvis adapter = new AdapterAvis(getActivity(), 0, stringList);

                        final ListView listview = (ListView) getActivity().findViewById(R.id.listViewAvis);
                        listview.setAdapter(adapter);

//                        listview.setOnItemClickListener(AvisFragment.this);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("err", error.toString());
                        if(error instanceof AuthFailureError) {
                            SharedPreferences preferences = AvisFragment.this.requireContext().getSharedPreferences("StudiSoigneMoi",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.remove("token");
                            editor.apply();

                            Intent intent = new Intent(AvisFragment.this.requireContext(), Login.class);
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