package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.FreeVideos;

import java.util.ArrayList;

public class FreeCourseAdapter extends RecyclerView.Adapter<FreeCourseAdapter.ViewHolder> {

    private ArrayList<Course> freeVideos;
    private Context mContext;

    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick1(String type, int position);
    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        mListener = listener;
    }


    public FreeCourseAdapter(ArrayList<Course> freeVideos, Context mContext) {
        this.freeVideos = freeVideos;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_free_course_new, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Glide.with(mContext).load(freeVideos.get(i).getImageUrl()).into(viewHolder.imageView);
        viewHolder.name.setText(freeVideos.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return freeVideos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        TextView name;

        Button go_inside;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            Log.i("type", freeVideos.get(position).getType());
                            mListener.onItemClick1(freeVideos.get(position).getType(), position);
                        }
                    }
                }
            });
        }
    }
}

