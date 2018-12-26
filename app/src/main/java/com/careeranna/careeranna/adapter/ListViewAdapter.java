package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.MenuList;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<MenuList> {

    ArrayList<MenuList> user;

    public ListViewAdapter(Context context, ArrayList<MenuList> user) {
        super(context, 0, user);
        this.user = user;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String user = this.user.get(position).getName();

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_icon_with_divider,parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.text);

        // Populate the data into the template view using the data object

        tvName.setText(user);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);

        imageView.setImageDrawable(this.user.get(position).getDrawable());

        // Return the completed view to render on screen

        return convertView;


    }
}
