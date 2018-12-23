package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.SubCategory;
import com.careeranna.careeranna.helper.RecyclerViewCoursesAdapter;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    private ArrayList<SubCategory> courses;
    private Context mContext;

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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sub_category, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.name.setText(courses.get(i).getEXAM_NAME());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }


    public SubCategoryAdapter(ArrayList<SubCategory> courses, Context mContext) {
        this.courses = courses;
        this.mContext = mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.sub);

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
