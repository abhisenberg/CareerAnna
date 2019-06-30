package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Topic;

import java.util.ArrayList;

import static android.view.View.GONE;

public class CourseContentInside extends RecyclerView.Adapter<CourseContentInside.ViewHolder>{

    private ArrayList<Topic> mTopics;
    private Context context;

    public CourseContentInside(ArrayList<Topic> mTopics, Context context) {
        this.mTopics = mTopics;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_unit_contents_child, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.tv_video_title.setText(mTopics.get(i).getName());

    }

    @Override
    public int getItemCount() {
        return mTopics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_video_title;
        ImageView iv_video_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_video_title = itemView.findViewById(R.id.tv_video_title);
            iv_video_image = itemView.findViewById(R.id.iv_video_image);

            iv_video_image.setVisibility(GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Please Enroll Now To See The Video Content", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
