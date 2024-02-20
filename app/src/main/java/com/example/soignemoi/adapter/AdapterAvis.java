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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterAvis extends ArrayAdapter<Avis> {
    private Activity activity;
    private ArrayList<Avis> lPerson;
    private static LayoutInflater inflater = null;

    public AdapterAvis(Activity activity, int textViewResourceId, ArrayList<Avis> _lPerson) {
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

        public TextView avis_date;

        public TextView avis_libelle;

        public TextView avis_description;

        public TextView avis_medecin;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.avis, null);
                holder = new ViewHolder();

                holder.avis_date = (TextView) vi.findViewById(R.id.dateAvis);
                holder.avis_libelle = (TextView) vi.findViewById(R.id.libelleAvis);
                holder.avis_description = (TextView) vi.findViewById(R.id.descriptionAvis);
                holder.avis_medecin = (TextView) vi.findViewById(R.id.medecinAvis);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            setDate(holder.avis_date, lPerson.get(position).date);
            holder.avis_libelle.setText(lPerson.get(position).libelle);
            holder.avis_description.setText(lPerson.get(position).description);

            String test = "Dr. " + lPerson.get(position).idMedecin.prenom + " " + lPerson.get(position).idMedecin.nom;
            holder.avis_medecin.setText(test);;


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