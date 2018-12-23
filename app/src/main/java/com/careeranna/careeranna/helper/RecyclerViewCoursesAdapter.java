package com.careeranna.careeranna.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;

import java.util.ArrayList;

public class RecyclerViewCoursesAdapter extends RecyclerView.Adapter<RecyclerViewCoursesAdapter.ViewHolder>{

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();
    private Context mContext;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        mListener = listener;
    }

    public RecyclerViewCoursesAdapter(ArrayList<String> names, ArrayList<String> urls, Context mContext) {
        this.names = names;
        this.urls = urls;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_course, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {
        Glide.with(mContext)
                .load(urls.get(i))
                .into(viewHolder.imageView);
        viewHolder.textView.setText(names.get(i));
    }

    @Override
    public int getItemCount() {
        return urls.size();
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
