package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.SubCategory;

import java.util.ArrayList;

public class ExamArrayAdapter extends RecyclerView.Adapter<ExamArrayAdapter.ViewHolder>{

    public static final String TAG = "SubCategoryArrayAdapter";

    private ArrayList<SubCategory> subCategories;
    private Context mContext;
    int row_index = -1;

    public OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onCategorySelected(int position);
    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        mListener = listener;
    }


    public ExamArrayAdapter(ArrayList<SubCategory> subCategories, Context mContext) {
        this.subCategories = subCategories;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_exam_name, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.exam_name.setText(subCategories.get(i).getEXAM_NAME());
        if(row_index==i){
            viewHolder.exam_name.setTextColor(Color.parseColor("#1989cd"));
            viewHolder.exam_name.setBackground(ContextCompat.getDrawable(mContext, R.drawable.border_bottom_active_category));
        } else {
            viewHolder.exam_name.setTextColor(Color.parseColor("#000000"));
            viewHolder.exam_name.setBackground(ContextCompat.getDrawable(mContext, R.drawable.border_bottom_non_active_category));
        }
    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView exam_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            exam_name = itemView.findViewById(R.id.exam_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            mListener.onCategorySelected(position);
                            row_index = position;
                            notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    }
}
