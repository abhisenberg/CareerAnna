package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Course;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import static android.view.View.GONE;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {

    private ArrayList<Course> freeVideos;
    private Context mContext;

    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick1(String type, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public CoursesAdapter(ArrayList<Course> freeVideos, Context mContext) {
        this.freeVideos = freeVideos;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_course_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.name.setText(freeVideos.get(i).getName());

        String[] thumbnail_URLs = {
                "https://www.careeranna.com/uploads/thumbnail_images/CAT01.jpg",
                "https://www.careeranna.com/uploads/thumbnail_images/XAT_02.jpg",
                "https://www.careeranna.com/uploads/thumbnail_images/TISSNET.jpg",
                "https://www.careeranna.com/uploads/thumbnail_images/SNAP.jpg",
                "https://www.careeranna.com/uploads/thumbnail_images/MICAT.jpg",
                "https://www.careeranna.com/uploads/thumbnail_images/GK.jpg"
        };

        String courseName = freeVideos.get(i).getName();
        String courseThumbnailURL = freeVideos.get(i).getImageUrl();

        if(freeVideos.get(i).getPrice()!= null && freeVideos.get(i).getPrice().equals("0")) {
            viewHolder.discounted_price.setVisibility(GONE);
            viewHolder.original_price.setText("Free");
        } else {
            viewHolder.discounted_price.setText("₹ "+freeVideos.get(i).getPrice());
            viewHolder.original_price.setPaintFlags(viewHolder.original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.original_price.setText("₹ "+freeVideos.get(i).getPrice_before_discount());
        }
        if(courseName.contains("CAT")){
            courseThumbnailURL = thumbnail_URLs[0];
        } else if (courseName.contains("XAT")){
            courseThumbnailURL = thumbnail_URLs[1];
        } else if (courseName.contains("TISSNET")){
            courseThumbnailURL = thumbnail_URLs[2];
        } else if (courseName.contains("SNAP")){
            courseThumbnailURL = thumbnail_URLs[3];
        } else if (courseName.contains("MICAT")){
            courseThumbnailURL = thumbnail_URLs[4];
        } else if (courseName.contains("GK")){
            courseThumbnailURL = thumbnail_URLs[5];
        }

        Glide
                .with(mContext)
                .load(Uri.parse(courseThumbnailURL))
                .into(viewHolder.imageView);

        viewHolder.ratingBar.setRating(Float.valueOf(freeVideos.get(i).getRatings()));
        if(freeVideos.get(i).getRatings() != null) {
            viewHolder.ratings.setText(freeVideos.get(i).getRatings()+" ( "+ NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(freeVideos.get(i).getTotal_ratings()))+" Ratings)");
        }
    }

    @Override
    public int getItemCount() {
        return freeVideos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        TextView name, original_price, discounted_price, ratings;

        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            ratingBar = itemView.findViewById(R.id.ratingBar_freeCourse);
            original_price = itemView.findViewById(R.id.original_price);
            discounted_price = itemView.findViewById(R.id.discounted_price);
            ratings = itemView.findViewById(R.id.ratings);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick1(freeVideos.get(position).getType(), position);
                        }
                    }
                }
            });
        }
    }
}

