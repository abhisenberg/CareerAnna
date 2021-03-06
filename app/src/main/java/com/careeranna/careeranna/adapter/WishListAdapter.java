package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.OrderedCourse;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    private ArrayList<OrderedCourse> mOrdered;
    private Context mContext;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick1(int position, String type);
    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        mListener = listener;
    }


    public WishListAdapter(ArrayList<OrderedCourse> mOrdered, Context mContext) {
        this.mOrdered = mOrdered;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wish_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.itemView.setTag(i);

        final int position = i;

        viewHolder.name.setText(mOrdered.get(i).getName());
        viewHolder.price.setText(mOrdered.get(i).getPrice());
        Glide.with(mContext).load(mOrdered.get(i).getImage()).into(viewHolder.imageView);


        if(mOrdered.get(i).getAverage_rating() != null) {
            viewHolder.course_rating_bar.setRating(Float.valueOf(mOrdered.get(i).getAverage_rating()));
            if(mOrdered.get(i).getTotal_rating() != null) {
                viewHolder.total_rating.setText(mOrdered.get(i).getAverage_rating()+" ( "+ NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(mOrdered.get(i).getTotal_rating()))+" Ratings)");
            }
        }


        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    if(position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick1(position, "remove");
                    }
                }
            }
        });

        viewHolder.whishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    if(position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick1(position, "wish");
                    }
                }
            }
        });
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

        TextView name, price, total_rating;
        RatingBar course_rating_bar;
        ImageView imageView;
        Button remove, whishlist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.course_name);
            price = itemView.findViewById(R.id.course_price);

            remove = itemView.findViewById(R.id.remove);

            imageView = itemView.findViewById(R.id.course_image);
            whishlist = itemView.findViewById(R.id.whishlist);

            course_rating_bar = itemView.findViewById(R.id.course_rating_bar);
            total_rating = itemView.findViewById(R.id.total_rating);
        }
    }
}
