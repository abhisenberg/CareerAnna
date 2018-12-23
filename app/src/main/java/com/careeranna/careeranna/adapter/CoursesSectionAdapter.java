package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.helper.RecyclerViewCoursesAdapter;

import java.util.ArrayList;

public class CoursesSectionAdapter extends RecyclerView.Adapter<CoursesSectionAdapter.ViewHolder>{


    private ArrayList<Course> mCourses;
    private Context mContext;

    private RecyclerViewCoursesAdapter.OnItemClickListener mListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_course, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(mContext)
                .load(mCourses.get(i).getImageUrl())
                .into(viewHolder.imageView);
        viewHolder.textView.setText(mCourses.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClicklistener(RecyclerViewCoursesAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public CoursesSectionAdapter(ArrayList<Course> mCourses, Context context) {
        this.mCourses = mCourses;
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.categoryImage);
            textView = itemView.findViewById(R.id.categoriesT);
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
