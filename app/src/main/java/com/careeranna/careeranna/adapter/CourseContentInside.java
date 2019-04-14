package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Topic;

import java.util.ArrayList;

public class CourseContentInside extends RecyclerView.Adapter<CourseContentInside.ViewHolder>{

    private ArrayList<Topic> mTopics;
    private Context context;
    private int watched_index = -1;

    public CourseContentInside(ArrayList<Topic> mTopics, Context context) {
        this.mTopics = mTopics;
        this.context = context;
    }

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick1(String  url, String heading);
    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_course_content_inside, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.tv_video_title.setText(mTopics.get(i).getName());

        if(watched_index == i) {
            viewHolder.tv_video_title.setTypeface(null, Typeface.BOLD);
            viewHolder.tv_video_title.setTextColor(Color.parseColor("#1989cd"));
            viewHolder.duration.setTextColor(Color.parseColor("#1989cd"));
            viewHolder.s_no.setTextColor(Color.parseColor("#1989cd"));
        } else {
            if(mTopics.get(i).getWatched()!= null) {
                if(mTopics.get(i).getWatched()) {
                    viewHolder.tv_video_title.setTextColor(Color.parseColor("#1989cd"));
                    viewHolder.duration.setTextColor(Color.parseColor("#1989cd"));
                    viewHolder.s_no.setTextColor(Color.parseColor("#1989cd"));
                }
            }
            viewHolder.tv_video_title.setTypeface(null, Typeface.NORMAL);
        }
        viewHolder.duration.setText("Video - " + mTopics.get(i).getDuration() + " mins");
        viewHolder.s_no.setText(i+1+"");

    }


    @Override
    public int getItemCount() {
        return mTopics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_video_title, duration, s_no;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_video_title = itemView.findViewById(R.id.title);
            duration = itemView.findViewById(R.id.duration);
            s_no = itemView.findViewById(R.id.s_no);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            watched_index = position;
                            mListener.onItemClick1(mTopics.get(position).getVideos(), mTopics.get(position).getName());
                            tv_video_title.setTextColor(Color.parseColor("#1989cd"));
                            duration.setTextColor(Color.parseColor("#1989cd"));
                            s_no.setTextColor(Color.parseColor("#1989cd"));
                            notifyDataSetChanged();
                        }
                    } else {
                        mListener.onItemClick1("Checkout", "");
                    }
                }
            });
        }
    }

}
