package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.graphics.Color;
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
        ImageView arrow = convertView.findViewById(R.id.arrow);

        TextView tvName = (TextView) convertView.findViewById(R.id.text);

        // Populate the data into the template view using the data object

        tvName.setGravity(this.user.get(position).getgravity());
        tvName.setText(user);
        Color.parseColor(this.user.get(position).getColor());

        arrow.setVisibility(this.user.get(position).getVisibility());
        convertView.setBackgroundColor(Color.parseColor(this.user.get(position).getColor()));
        tvName.setTextColor(Color.parseColor(this.user.get(position).getInsideColor()));
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);

        imageView.setImageDrawable(this.user.get(position).getDrawable());

        // Return the completed view to render on screen

        return convertView;


    }
}
