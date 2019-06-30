package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.activity.NotificationCourseActivity;
import com.careeranna.careeranna.activity.PurchaseCourseActivity;
import com.careeranna.careeranna.data.Unit;
import com.careeranna.careeranna.fragement.profile_fragements.TutorialFragment;

import java.util.ArrayList;

public class CourseContentAdapter extends RecyclerView.Adapter<CourseContentAdapter.ViewHolder> implements CourseContentInside.OnItemClickListener{

    private ArrayList<Unit> mUnits;
    private Context context;
    private String url;
    private TutorialFragment tutorialFragment;
    private PurchaseCourseActivity purchaseCourseDetail;
    private NotificationCourseActivity notificationCourseActivity;

    public CourseContentAdapter(ArrayList<Unit> mUnits, Context context) {
        this.mUnits = mUnits;
        this.context = context;
    }

    public void addFragment(TutorialFragment tutorialFragment) {
        this.tutorialFragment = tutorialFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_course_content, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.tv_unit_title.setText(mUnits.get(i).getName());

        CourseContentInside courseContentInside = new CourseContentInside(mUnits.get(i).topics, context);

        viewHolder.video_inside_heading_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        viewHolder.video_inside_heading_rv.setAdapter(courseContentInside);

        if(i == 0) {
            viewHolder.video_inside_heading_rv.setVisibility(View.VISIBLE);
            viewHolder.dropdown_arrow.setImageResource(R.drawable.ic_remove_blue_24dp);
        }
        if(this.tutorialFragment != null) {
            courseContentInside.setOnItemClicklistener(this);
        }
        if(this.purchaseCourseDetail != null) {
            courseContentInside.setOnItemClicklistener(this);
        }
        if(this.notificationCourseActivity != null) {
            courseContentInside.setOnItemClicklistener(this);
        }

    }

    @Override
    public int getItemCount() {
        return mUnits.size();
    }

    public void addPurchaseCourse(PurchaseCourseActivity purchaseCourseDetail) {
        this.purchaseCourseDetail = purchaseCourseDetail;
    }

    public void addNotificationCourse(NotificationCourseActivity notificationCourseActivity) {
        this.notificationCourseActivity = notificationCourseActivity;
    }

    @Override
    public void onItemClick1(String video_url, String heading) {
        if(tutorialFragment != null) {
            url = video_url;
            tutorialFragment.playVideo(url, heading);
        }
        if(purchaseCourseDetail != null) {
            url = video_url;
            if(url.equals("Checkout")) {
                purchaseCourseDetail.freeCourseCheckout();
            } else {
                purchaseCourseDetail.playVideo(url);
            }
        }

        if(notificationCourseActivity != null) {

            url = video_url;

            if(url.equals("Checkout")) {
                notificationCourseActivity.freeCourseCheckout();
            } else {
                notificationCourseActivity.playVideo(url);
            }

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_unit_title;

        RecyclerView video_inside_heading_rv;

        ImageView dropdown_arrow;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_unit_title = itemView.findViewById(R.id.course_heading);
            video_inside_heading_rv = itemView.findViewById(R.id.course_content_inside);
            dropdown_arrow = itemView.findViewById(R.id.dropdown_arrow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(video_inside_heading_rv.getVisibility() == View.VISIBLE) {
                        video_inside_heading_rv.setVisibility(View.GONE);
                        dropdown_arrow.setImageResource(R.drawable.ic_plus_blue_24dp);
                    } else {
                        video_inside_heading_rv.setVisibility(View.VISIBLE);
                        dropdown_arrow.setImageResource(R.drawable.ic_remove_blue_24dp);
                    }
                }
            });
        }
    }

}
