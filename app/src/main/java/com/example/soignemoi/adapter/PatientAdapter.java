package com.example.soignemoi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PatientAdapter extends ArrayAdapter {
    public PatientAdapter(@NonNull Context context, int resource, ArrayList patientList) {
        super(context, resource, patientList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        /*View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        //get the current user
        User currentUser = getItem(position);
        //Find the views from list_item.xml
        TextView userName = listView.findViewById(R.id.tv_user_name);
        TextView userId = listView.findViewById(R.id.tv_user_id);
        //Set these views with the data stored in the currentUser object
        userName.setText(currentUser.getUserName());
        userId.setText(currentUser.getUserId());

        return listView;*/
        return super.getView(position, convertView, parent);
    }
}
