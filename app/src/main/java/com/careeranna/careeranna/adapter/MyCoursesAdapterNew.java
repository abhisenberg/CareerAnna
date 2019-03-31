package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.CourseWithLessData;
import com.careeranna.careeranna.helper.RecyclerViewCoursesAdapter;

import java.util.ArrayList;
import java.util.Random;

public class MyCoursesAdapterNew extends RecyclerView.Adapter<MyCoursesAdapterNew.ViewHolder>{

    private Context mContext;
    private ArrayList<CourseWithLessData> course = new ArrayList<>();
    private ArrayList<CourseWithLessData> courseCopy = new ArrayList<>();

    public MyCoursesAdapterNew(Context mContext, ArrayList<CourseWithLessData> course) {
        this.mContext = mContext;
        this.course = course;
    }

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_course_new_2, viewGroup, false);
        return new ViewHolder(view);
    }

    int delayAnimate = 300; //global variable

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //set view to INVISIBLE before animate
        Random rand = new Random();
        int random  = rand.nextInt(100);
        if(course.get(position).getCourse_imageURL() != null && !course.get(position).getCourse_imageURL().isEmpty()) {
            if(!course.get(position).getCourse_imageURL().contains("https")) {
                Glide.with(mContext)
                        .load("https://www.careeranna.com/"+course.get(position).getCourse_imageURL())
                        .into(viewHolder.imageView);
            } else {
                Glide.with(mContext)
                        .load(course.get(position).getCourse_imageURL())
                        .into(viewHolder.imageView);
            }
        }viewHolder.textView.setText(course.get(position).getCourse_name());
        viewHolder.progressBar.setProgress(random);
        viewHolder.tv.setText(random+"%");

    }

    private void setAnimation(final View view) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
                if(view!=null){
                    view.startAnimation(animation);
                    view.setVisibility(View.VISIBLE);
                }
            }
        }, delayAnimate);
        delayAnimate+=300;
    }

    @Override
    public int getItemCount() {
        return course.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView, tv;

        Button go_inside;

        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.name);
            progressBar = itemView.findViewById(R.id.progress);
            tv = itemView.findViewById(R.id.tv);

            go_inside = itemView.findViewById(R.id.continu);

            go_inside.setOnClickListener(new View.OnClickListener() {
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

