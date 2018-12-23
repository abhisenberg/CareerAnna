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
import com.careeranna.careeranna.data.OrderedCourse;

import java.util.ArrayList;

public class OrderCourseAdapter extends RecyclerView.Adapter<OrderCourseAdapter.ViewHolder> {

    private ArrayList<OrderedCourse> mOrdered;
    private Context mContext;

    public OrderCourseAdapter(ArrayList<OrderedCourse> mOrdered, Context mContext) {
        this.mOrdered = mOrdered;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_ordered_course, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.itemView.setTag(i);

        viewHolder.name.setText(mOrdered.get(i).getName());
        viewHolder.price.setText(mOrdered.get(i).getPrice());
        Glide.with(mContext).load(mOrdered.get(i).getImage()).into(viewHolder.imageView);
    }

    public void remove(int pos) {
        mOrdered.remove(pos);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mOrdered.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, price;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.course_name);
            price = itemView.findViewById(R.id.course_price);

            imageView = itemView.findViewById(R.id.course_image);
        }
    }
}
