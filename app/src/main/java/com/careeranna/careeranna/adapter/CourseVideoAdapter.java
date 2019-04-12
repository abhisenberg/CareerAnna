package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Topic;
import com.careeranna.careeranna.data.Unit;
import com.careeranna.careeranna.fragement.profile_fragements.TutorialFragment;

import java.util.ArrayList;

public class CourseVideoAdapter extends RecyclerView.Adapter<CourseVideoAdapter.VideoViewHolder> implements VideoInsideHeadingAdpater.OnVideoItemClickListener{

    private ArrayList<Unit> units;
    private Context context;
    private TutorialFragment tutorialFragment;

    public CourseVideoAdapter(ArrayList<Unit> units, Context context, TutorialFragment tutorialFragment) {
        this.units = units;
        this.context = context;
        this.tutorialFragment = tutorialFragment;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_unit_contents_parent, viewGroup, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder videoViewHolder, int i) {
        videoViewHolder.heading.setText(units.get(i).Name);

        videoViewHolder.videos_rv.setLayoutManager(new LinearLayoutManager(context));
        videoViewHolder.videos_rv.setHasFixedSize(true);

        VideoInsideHeadingAdpater videoInsideHeadingAdpater = new VideoInsideHeadingAdpater(units.get(i).topics, context);
        videoViewHolder.videos_rv.setAdapter(videoInsideHeadingAdpater);
        videoInsideHeadingAdpater.setOnItemClicklistener(this);
    }

    @Override
    public int getItemCount() {
        if(units != null) {
            return units.size();
        } else {
            return 0;
        }
    }

    @Override
    public void onVideoItemClick(Topic topic) {
        tutorialFragment.playVideo(topic.getVideos(), "");
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        TextView heading;
        ImageView toogleShowVideos;

        RecyclerView videos_rv;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            heading = itemView.findViewById(R.id.tv_unit_title);

            videos_rv = itemView.findViewById(R.id.video_inside_heading_rv);
            toogleShowVideos =  itemView.findViewById(R.id.iv_unit_imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(videos_rv.getVisibility() == View.GONE) {
                        toogleShowVideos.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
                        videos_rv.setVisibility(View.VISIBLE);
                    } else {
                        toogleShowVideos.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_drop_down_blue_24dp));
                        videos_rv.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
