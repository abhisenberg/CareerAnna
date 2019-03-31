package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Unit;

import java.util.ArrayList;

public class CourseContentAdapter extends RecyclerView.Adapter<CourseContentAdapter.ViewHolder>{

    private ArrayList<Unit> mUnits;
    private Context context;

    public CourseContentAdapter(ArrayList<Unit> mUnits, Context context) {
        this.mUnits = mUnits;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_unit_contents_parent, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.tv_unit_title.setText(mUnits.get(i).getName());

        CourseContentInside courseContentInside = new CourseContentInside(mUnits.get(i).topics, context);

        viewHolder.video_inside_heading_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        viewHolder.video_inside_heading_rv.setAdapter(courseContentInside);

    }

    @Override
    public int getItemCount() {
        return mUnits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_unit_title;

        RecyclerView video_inside_heading_rv;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_unit_title = itemView.findViewById(R.id.tv_unit_title);
            video_inside_heading_rv = itemView.findViewById(R.id.video_inside_heading_rv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(video_inside_heading_rv.getVisibility() == View.VISIBLE) {
                        video_inside_heading_rv.setVisibility(View.GONE);
                    } else {
                        video_inside_heading_rv.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

}
