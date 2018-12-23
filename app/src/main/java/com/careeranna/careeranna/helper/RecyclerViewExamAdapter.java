package com.careeranna.careeranna.helper;

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
import com.careeranna.careeranna.data.ExamPrep;

import java.util.ArrayList;

public class RecyclerViewExamAdapter extends RecyclerView.Adapter<RecyclerViewExamAdapter.ViewHolder>{

    private ArrayList<ExamPrep> mExamPreps;
    private Context mContext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClickExamp(int position);
    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        mListener = listener;
    }

    public RecyclerViewExamAdapter(ArrayList<ExamPrep> mExamPreps, Context mContext) {
        this.mExamPreps = mExamPreps;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category, viewGroup, false);
        return new RecyclerViewExamAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(mContext)
                .load(mExamPreps.get(i).getImageUrl())
                .into(viewHolder.imageView);
        viewHolder.textView.setText(mExamPreps.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return mExamPreps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.categoryImage);
            textView = itemView.findViewById(R.id.categoriesT);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClickExamp(position);
                        }
                    }
                }
            });
        }
    }
}