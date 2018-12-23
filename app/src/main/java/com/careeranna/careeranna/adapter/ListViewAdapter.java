package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.careeranna.careeranna.R;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<String> {

    public ListViewAdapter(Context context, ArrayList<String> user) {
        super(context, 0, user);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String user = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_icon_with_divider,parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.text);

        // Populate the data into the template view using the data object

        tvName.setText(user);


        // Return the completed view to render on screen

        return convertView;


    }
}
