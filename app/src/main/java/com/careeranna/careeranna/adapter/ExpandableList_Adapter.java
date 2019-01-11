package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Topic;
import com.careeranna.careeranna.data.Unit;

import java.util.ArrayList;

public class ExpandableList_Adapter extends BaseExpandableListAdapter{

    public static final String TAG = "ExpandableListAdapter";

    private Context mContext;

    private ArrayList<Unit> unitsList;

    private LayoutInflater layoutInflater;

    private OnItemClickListener mListener;

    private ExpandableListView expandableListView;

    private TextView lastSelectedChild;

    public interface OnItemClickListener {
        void onItemClick1(int position, int position2);
    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ExpandableList_Adapter(Context mContext, ArrayList<Unit> unit, final ExpandableListView expandableListView) {
        this.mContext = mContext;
        this.unitsList = unit;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.expandableListView = expandableListView;

        this.expandableListView.setGroupIndicator(null);
    }

    @Override
    public int getGroupCount() {
        return unitsList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return unitsList.get(i).topics.size();
    }

    @Override
    public Unit getGroup(int i) {
        return unitsList.get(i);
    }

    @Override
    public Topic getChild(int i, int i1) {
        return unitsList.get(i).topics.get(i1);
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
    public View getGroupView(int i, boolean isExpanded, View view, ViewGroup viewGroup) {

        String headerTitle = getGroup(i).Name;

        Drawable icon = getGroup(i).getIcon();

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_unit_contents_parent, null);
        }

        if(isExpanded){
            expandableListView.setFooterDividersEnabled(true);
        }

        expandableListView.setChildDivider(mContext.getResources().getDrawable(R.drawable.line));

        TextView listHeader = (TextView) view.findViewById(R.id.tv_unit_title);
        ImageView imageView = view.findViewById(R.id.iv_unit_imageView);

        listHeader.setTypeface(null, Typeface.BOLD);
        listHeader.setText(headerTitle);

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = layoutInflater.inflate(R.layout.list_item_unit_contents_child, null);
        }

        final int parent = groupPosition;
        final int child = childPosition;
        final String childText = getChild(groupPosition, childPosition).getName();

        Drawable icon = getChild(groupPosition,childPosition).getIcon();

        ImageView listChildImage = view.findViewById(R.id.iv_video_image);
        final TextView listChild = view.findViewById(R.id.tv_video_title);
        View lineDivider = view.findViewById(R.id.line_divider_under_child);

          /*
        The first video of the course plays by default when the course if opened, hence the
        video title is highlighted if parent = 0 and child = 0. But if another video is clicked,
        the color of that video will be changed and the first video will be un-highlighted.
         */
        Log.d(TAG, "getChildView: "+groupPosition+", "+childPosition+", "+listChild.getText());
        if(lastSelectedChild == null && (groupPosition == 0 && childPosition == 1)){
            Log.d(TAG, "getChildView: Setting last child as: "+listChild.getText());
            lastSelectedChild = listChild;
            listChild.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }

        if(!isLastChild){
            lineDivider.setVisibility(View.INVISIBLE);
        } else
            lineDivider.setVisibility(View.VISIBLE);

        listChild.setText(childText);
        listChildImage.setImageDrawable(icon);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null) {
                    mListener.onItemClick1(parent, child);
                    /*
                        Highlight the current video and un-highlight the first video
                     */
                    ((TextView)view.findViewById(R.id.tv_video_title)).setTextColor(
                            mContext.getResources().getColor(R.color.colorPrimary)
                    );

                    if(lastSelectedChild != null){
                        lastSelectedChild.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
                    }

                    lastSelectedChild = view.findViewById(R.id.tv_video_title);
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

