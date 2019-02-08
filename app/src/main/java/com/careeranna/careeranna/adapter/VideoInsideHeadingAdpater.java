package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Topic;

import java.util.ArrayList;

public class VideoInsideHeadingAdpater extends RecyclerView.Adapter<VideoInsideHeadingAdpater.VideoViewHolder>{

    private ArrayList<Topic> topics;
    private Context context;

    private OnVideoItemClickListener mListener;

    public interface OnVideoItemClickListener {
        void onVideoItemClick(Topic topic);
    }

    public void setOnItemClicklistener(OnVideoItemClickListener listener) {
        mListener = listener;
    }


    public VideoInsideHeadingAdpater(ArrayList<Topic> topics, Context context) {
        this.topics = topics;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_unit_contents_child, viewGroup, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder videoViewHolder, int i) {
        videoViewHolder.heading.setText(topics.get(i).getName());
        if(!topics.get(i).isWatched()) {
            videoViewHolder.isWatched.setVisibility(View.INVISIBLE);
            videoViewHolder.heading.setTextColor(context.getResources().getColor(R.color.dark_gray));
        }
    }

    @Override
    public int getItemCount() {
        if(topics != null) {
            return topics.size();
        } else {
            return 0;
        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        TextView heading;

        ImageView isWatched;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            heading = itemView.findViewById(R.id.tv_video_title);
            isWatched = itemView.findViewById(R.id.iv_video_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            heading.setTextColor(context.getResources().getColor(R.color.watched));
                            mListener.onVideoItemClick(topics.get(position));
                        }
                    }

                }
            });
        }
    }
}
