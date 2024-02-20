package com.example.soignemoi.activity.ui.patient;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.soignemoi.activity.NavBarActivity;
import com.example.soignemoi.databinding.FragmentPatientBinding;
import com.example.soignemoi.entity.EDT;
import com.example.soignemoi.listeners.OnMyFragmentListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PatientFragment extends Fragment /*implements AdapterView.OnItemClickListener*/ {

//    private final String URL = "http://192.168.1.101:8000/api/medecin/edts";

    OnMyFragmentListener mListener;

    private FragmentPatientBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PatientViewModel patientViewModel =
                new ViewModelProvider(this).get(PatientViewModel.class);

        binding = FragmentPatientBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.labelListPatient;
//        patientViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        TextView motif = binding.sejourMotif;
        TextView id = binding.patientId;
        TextView name = binding.patientName;
        TextView dateDebut = binding.sejourDateDebut;
        TextView dateFin = binding.sejourDateFin;

        EDT edt = ((NavBarActivity) getActivity()).getEdt();
        if(edt != null) {
            motif.setText(edt.idSejour.motif);
            setDate(dateDebut, edt.idSejour.dateDebut);
            setDate(dateFin, edt.idSejour.dateFin);
            id.setText(edt.id);
            String nameStr = edt.idSejour.idPatient.prenom + " " + edt.idSejour.idPatient.nom;
            name.setText(nameStr);
        }

        return root;
    }

    public void setOnMyFragmentListener(OnMyFragmentListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMyFragmentListener) {
            mListener = (OnMyFragmentListener) context;
            mListener.onChangeToolbarTitle("Patient du Jour"); // Call this in `onResume()`
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
        mListener.onChangeToolbarTitle("Patient du Jour");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setDate (TextView view, Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = formatter.format(date);
        view.setText(dateStr);
    }

}