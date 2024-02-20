package com.example.soignemoi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.soignemoi.R;
import com.example.soignemoi.entity.Avis;
import com.example.soignemoi.entity.Prescription;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdapterPrescription extends ArrayAdapter<Prescription> {
    private Activity activity;
    private ArrayList<Prescription> lPerson;
    private static LayoutInflater inflater = null;

    public AdapterPrescription(Activity activity, int textViewResourceId, ArrayList<Prescription> _lPerson) {
        super(activity, textViewResourceId, _lPerson);
        try {
            this.activity = activity;
            this.lPerson = _lPerson;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return lPerson.size();
    }

    public Avis getItem(Avis position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        /*public TextView patient_firstname;
        public TextView patient_surname;*/

        public TextView prescription_date_debut;

        public TextView prescription_date_fin;

        public TextView prescription_medicament;

        public TextView prescription_posologie;

        public TextView prescription_medecin;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.prescription, null);
                holder = new ViewHolder();

                holder.prescription_date_debut  = (TextView) vi.findViewById(R.id.dateDebutPrescription);
                holder.prescription_date_fin = (TextView) vi.findViewById(R.id.dateFinPrescription);
                holder.prescription_medicament = (TextView) vi.findViewById(R.id.medicamentPrescription);
                holder.prescription_posologie = (TextView) vi.findViewById(R.id.posologiePrescription);
                holder.prescription_medecin = (TextView) vi.findViewById(R.id.medecinPrescription);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            setDate(holder.prescription_date_debut, lPerson.get(position).dateDebut);
            setDate(holder.prescription_date_fin, lPerson.get(position).dateFin);

            holder.prescription_medicament.setText(lPerson.get(position).medicament);
            holder.prescription_posologie.setText(lPerson.get(position).posologie);

            String test = "Dr. " + lPerson.get(position).idMedecin.prenom + " " + lPerson.get(position).idMedecin.nom;
            holder.prescription_medecin.setText(test);;


        } catch (Exception e) {


        }
        return vi;
    }

    public void setDate (TextView view, Date date){

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = formatter.format(date);
        view.setText(dateStr);
    }
}