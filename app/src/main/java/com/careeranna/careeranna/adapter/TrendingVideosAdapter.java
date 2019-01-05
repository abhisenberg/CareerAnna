package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.FreeVideos;

import java.util.ArrayList;

public class TrendingVideosAdapter extends RecyclerView.Adapter<TrendingVideosAdapter.ViewHolder> {

    private ArrayList<FreeVideos> freeVideos;
    private Context mContext;

    private OnItemClickListener mListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_videos_new_2, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Glide.with(mContext).load(freeVideos.get(i).getThumbnail()).into(viewHolder.imageView);
        viewHolder.title.setText(freeVideos.get(i).getTitle());
        viewHolder.views.setText(freeVideos.get(i).getTotal_view()+" views");
        Log.i("length", freeVideos.get(i).getDuration().length()+"");
        if(freeVideos.get(i).getDuration().length() == 4) {
            viewHolder.duration.setText(freeVideos.get(i).getDuration()+"0");
        } else if(freeVideos.get(i).getDuration().length() == 3) {
            viewHolder.duration.setText(freeVideos.get(i).getDuration()+"00");
        } else if(freeVideos.get(i).getDuration().length() == 2) {
            viewHolder.duration.setText(freeVideos.get(i).getDuration()+".00");
        } else  if(freeVideos.get(i).getDuration().length() == 1) {
            viewHolder.duration.setText("0"+freeVideos.get(i).getDuration()+".00");
        } else{
            viewHolder.duration.setText(freeVideos.get(i).getDuration());
        }
        if(freeVideos.get(i).getTags() != null) {
            if (!freeVideos.get(i).getTags().equals("null")) {
                viewHolder.tags.setText(freeVideos.get(i).getTags());
            } else {
                viewHolder.tags.setText("Cat");
            }
        }
    }

    @Override
    public int getItemCount() {
        return freeVideos.size();
    }

    public interface OnItemClickListener {
        void onItemClick1(int position, String type);
    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        mListener = listener;
    }


    public TrendingVideosAdapter(ArrayList<FreeVideos> freeVideos, Context mContext) {
        this.freeVideos = freeVideos;
        this.mContext = mContext;
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title, rating, views, duration, tags;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            duration = itemView.findViewById(R.id.duration);
            imageView = itemView.findViewById(R.id.image);

            title = itemView.findViewById(R.id.title);
            rating = itemView.findViewById(R.id.ratings);
            views = itemView.findViewById(R.id.views);
            tags = itemView.findViewById(R.id.tags);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick1(position, freeVideos.get(position).getType());
                        }
                    }
                }
            });
        }
    }
}
