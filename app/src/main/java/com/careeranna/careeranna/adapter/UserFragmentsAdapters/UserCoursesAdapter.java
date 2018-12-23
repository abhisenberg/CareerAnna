package com.careeranna.careeranna.adapter.UserFragmentsAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.CourseWithLessData;

import java.util.ArrayList;
import java.util.Random;

public class UserCoursesAdapter extends RecyclerView.Adapter<UserCoursesAdapter.UserCourses> {

    public static final String TAG = "UserCoursesFrag";

    private Context context;
    ArrayList<CourseWithLessData> courseList;

    /*
    TODO: UserCoursesFragment -- All this list is dummy data, change it to show actual progress
     */

    public UserCoursesAdapter(Context context){
        this.context = context;
        courseList = new ArrayList<>();
    }

    public void updateData(ArrayList<CourseWithLessData> courseList){
        this.courseList = courseList;
        Log.d(TAG, "updateData: courses = "+courseList.size());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserCourses onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_user_courses_list, viewGroup, false);

        return new UserCourses(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCourses userCourses, int i) {
        //*******************SHOWING RANDOM PROGRESS IN COURSES************
        Random random = new Random();
        int randomProgress = random.nextInt(100);
        userCourses.progressBar.setProgress(randomProgress);
        userCourses.tv_progress.setText(randomProgress+"%");
        if(randomProgress <= 20){
            userCourses.progressBar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.circular_red));
        } else if(randomProgress <= 50){
            userCourses.progressBar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.circular_yellow));
        } else if(randomProgress <= 70){
            userCourses.progressBar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.circular_dark_green));
        } else
        {
            userCourses.progressBar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.circular));
        }
        //******************************************************************/

        userCourses.tv_units_completed.setText("0 out 0 units completed");
        userCourses.tv_courseName.setText(courseList.get(i).getCourse_name());
        Glide
                .with(context)
                .load(courseList.get(i).getCourse_imageURL())
                .into(userCourses.iv_courseImage);

        Log.d(TAG, "Image url is "+courseList.get(i).getCourse_imageURL());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class UserCourses extends RecyclerView.ViewHolder {

        ImageView iv_courseImage;
        ProgressBar progressBar;
        TextView tv_progress, tv_courseName, tv_units_completed;

        public UserCourses(@NonNull View itemView) {
            super(itemView);

            iv_courseImage = itemView.findViewById(R.id.item_user_course_image);
            progressBar = itemView.findViewById(R.id.item_user_course_progress_bar);
            tv_progress = itemView.findViewById(R.id.item_user_course_progress_percentage);
            tv_courseName = itemView.findViewById(R.id.item_user_course_name);
            tv_units_completed = itemView.findViewById(R.id.item_user_course_units_completed);
        }
    }

}