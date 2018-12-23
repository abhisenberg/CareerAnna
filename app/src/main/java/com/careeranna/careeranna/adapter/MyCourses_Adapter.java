package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.dummy_data.ProfileCourses;

import java.util.List;

public class MyCourses_Adapter extends RecyclerView.Adapter<MyCourses_Adapter.ViewHolder> {

    public static final String TAG = "adapter_mycourses";

    private List<ProfileCourses> myCoursesList;
    private Context context;

    public MyCourses_Adapter(Context context, List<ProfileCourses> myCoursesList){
        this.context = context;
        this.myCoursesList = myCoursesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_mycourses, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        ProfileCourses myCourse = myCoursesList.get(i);
        holder.tv_courseName.setText(myCourse.getName());
        holder.tv_courseCompletePerc.setText(String.valueOf(myCourse.getProgress()+"%"));
        holder.pb_completion.setProgress(myCourse.getProgress());
        //Here set the image of the course from the image link
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+myCoursesList.size());
        return myCoursesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_courseName, tv_courseCompletePerc;
        private ImageView iv_courseImage;
        private ProgressBar pb_completion;

        public ViewHolder(@NonNull View view) {
            super(view);

            tv_courseName = view.findViewById(R.id.tv_courseName);
            tv_courseCompletePerc = view.findViewById(R.id.tv_completedPercentage);
            iv_courseImage  = view.findViewById(R.id.iv_courseImage);
            pb_completion = view.findViewById(R.id.pb_courseProgress);
        }
    }

}