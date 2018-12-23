package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InstructorsListAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private ArrayList<String> inst_names, inst_des, inst_images;
    private Context context;

    public InstructorsListAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_instructor_details, viewGroup, false);

        return new InstructorHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Glide.with(context)
                .load(inst_images.get(i))
                .into(((InstructorHolder)viewHolder).instructor_image);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_instructor_follow:
                break;
        }
    }

    public class InstructorHolder extends RecyclerView.ViewHolder {
        CircleImageView instructor_image;
        TextView instrctor_name, instructor_desc;

        public InstructorHolder(@NonNull View itemView) {
            super(itemView);
            instrctor_name = itemView.findViewById(R.id.tv_instructor_name);
            instructor_desc = itemView.findViewById(R.id.tv_instructor_desc);
            instructor_image = itemView.findViewById(R.id.iv_instructor_image);
        }
    }
}
