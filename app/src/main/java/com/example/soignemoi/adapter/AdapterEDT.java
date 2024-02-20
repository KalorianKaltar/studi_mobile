package com.example.soignemoi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.soignemoi.R;
import com.example.soignemoi.entity.EDT;

import java.util.ArrayList;

public class AdapterEDT extends ArrayAdapter<EDT> {
    private Activity activity;
    private ArrayList<EDT> lPerson;
    private static LayoutInflater inflater = null;

    public AdapterEDT(Activity activity, int textViewResourceId, ArrayList<EDT> _lPerson) {
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

    public EDT getItem(EDT position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        /*public TextView patient_firstname;
        public TextView patient_surname;*/

        public TextView sejour_motif;

        public TextView patient_name;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.patient, null);
                holder = new ViewHolder();

                holder.patient_name = (TextView) vi.findViewById(R.id.patient_name);
                /*holder.patient_firstname = (TextView) vi.findViewById(R.id.patient_firstname);
                holder.patient_surname = (TextView) vi.findViewById(R.id.patient_surname);*/
                holder.sejour_motif = (TextView) vi.findViewById(R.id.sejour_motif);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            String test = lPerson.get(position).idSejour.idPatient.prenom + " - " + lPerson.get(position).idSejour.idPatient.nom;
            holder.patient_name.setText(test);
            /*holder.patient_firstname.setText(lPerson.get(position).idSejour.idPatient.prenom);
            holder.patient_surname.setText(lPerson.get(position).idSejour.idPatient.nom);*/
            holder.sejour_motif.setText(lPerson.get(position).idSejour.motif);


        } catch (Exception e) {


        }
        return vi;
    }
}