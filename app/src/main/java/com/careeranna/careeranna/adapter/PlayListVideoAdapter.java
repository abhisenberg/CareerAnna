package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.activity.VideoWithComment;
import com.careeranna.careeranna.data.FreeVideos;
import com.careeranna.careeranna.data.PlayListItem;

import java.util.ArrayList;
import java.util.Collections;

public class PlayListVideoAdapter extends RecyclerView.Adapter<PlayListVideoAdapter.ViewHolder>{

    private ArrayList<FreeVideos> freeVideos;
    private Context context;
    private String videoId;

    private PlaylistAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick1(int position);
    }

    public void setOnItemClicklistener(PlaylistAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    public PlayListVideoAdapter(ArrayList<FreeVideos> freeVideos, Context context) {
        this.freeVideos = freeVideos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.play_list_video, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.video_heading.setText(freeVideos.get(i).getTitle());

        Glide.with(context)
                .load(freeVideos.get(i).getThumbnail())
                .into(viewHolder.video_image);

    }

    @Override
    public int getItemCount() {
        return freeVideos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView video_image;
        TextView video_heading;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            video_image = itemView.findViewById(R.id.video_image);

            video_heading = itemView.findViewById(R.id.video_heading);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(context!= null) {
                        int position = getAdapterPosition();
                        context.startActivity(new Intent(context, VideoWithComment.class).putExtra("videos", freeVideos.get(position)));
                    }
                }
            });
        }
    }
    }