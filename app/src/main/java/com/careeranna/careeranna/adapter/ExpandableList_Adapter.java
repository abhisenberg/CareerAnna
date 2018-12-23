package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Topic;
import com.careeranna.careeranna.data.Unit;
import com.careeranna.careeranna.helper.RecyclerViewAdapter;

import java.util.ArrayList;

public class ExpandableList_Adapter extends BaseExpandableListAdapter {

    private Context mContext;

    private ArrayList<Unit> unit;

    private LayoutInflater layoutInflater;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick1(int position, int position2);
    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        mListener = listener;
    }


    public ExpandableList_Adapter(Context mContext, ArrayList<Unit> unit) {
        this.mContext = mContext;
        this.unit = unit;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return unit.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return unit.get(i).topics.size();
    }

    @Override
    public Unit getGroup(int i) {
        return unit.get(i);
    }

    @Override
    public Topic getChild(int i, int i1) {
        return unit.get(i).topics.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        String headerTitle = getGroup(i).Name;

        Drawable icon = getGroup(i).getIcon();

        if(view == null) {

            LayoutInflater inflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_unit_group, null);
            view = inflater.inflate(R.layout.item_videos_inside_course, null);
        }

        TextView listHeader = (TextView) view.findViewById(R.id.header);
        ImageView imageView = view.findViewById(R.id.imageView);

        listHeader.setTypeface(null, Typeface.BOLD);
        listHeader.setText(headerTitle);

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        if(view == null) {

            view = layoutInflater.inflate(R.layout.list_group_item, null);
        }

        final int parent = i;
        final int child = i1;
        final String childText = getChild(i, i1).getName();

        Drawable icon = getChild(i,i1).getIcon();
        ImageView listChildImage = view.findViewById(R.id.imageView);
        TextView listChild = (TextView) view.findViewById(R.id.headeritem);
        listChild.setText(childText);
        listChildImage.setImageDrawable(icon);

        // listChildImage.setImageDrawable(icon);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null) {
                    mListener.onItemClick1(parent, child);
                }
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}

