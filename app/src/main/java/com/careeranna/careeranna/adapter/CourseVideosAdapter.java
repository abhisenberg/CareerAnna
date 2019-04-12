package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.FreeVideos;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CourseVideosAdapter extends RecyclerView.Adapter<CourseVideosAdapter.ViewHolder> {

    private ArrayList<FreeVideos> freeVideos;
    private Context mContext;


    public OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CourseVideosAdapter(ArrayList<FreeVideos> freeVideos, Context mContext) {
        this.freeVideos = freeVideos;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_of_courses, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        Glide.with(mContext)
                .load(freeVideos.get(i).getThumbnail())
                .into(viewHolder.video_image);

        viewHolder.duration.setText(freeVideos.get(i).getDuration());
        if(freeVideos.get(i).getDuration().length() <= 2) {
            viewHolder.duration.setText(freeVideos.get(i).getDuration()+":00");
        }
        String views = "0";
        if(freeVideos.get(i).getTotal_view() != "null") {
            views = NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(freeVideos.get(i).getTotal_view()));
        }
        viewHolder.views.setText(views + " Views");
        viewHolder.title.setText(freeVideos.get(i).getTitle());
        String likes = "0";
        if(freeVideos.get(i).getLikes() != "null") {
            likes = NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(freeVideos.get(i).getLikes()));
        }
        viewHolder.likes.setText(likes+" likes");
        viewHolder.count_comment.setText("view all " + freeVideos.get(i).getCount_comments()+" comments");

        viewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.careeranna.com/english/free/videos/mba/"+freeVideos.get(i).getTitle().replaceAll(" ", "-"));
                sendIntent.setType("text/plain");
                mContext.startActivity(sendIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return freeVideos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView video_image;

        TextView duration, views, title, likes, count_comment, share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            video_image = itemView.findViewById(R.id.course_video_image);

            duration = itemView.findViewById(R.id.course_video_duration);
            views = itemView.findViewById(R.id.course_video_views);
            title = itemView.findViewById(R.id.course_video_title);
            likes = itemView.findViewById(R.id.course_videos_likes);
            share = itemView.findViewById(R.id.share);
            count_comment = itemView.findViewById(R.id.total_comment);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
