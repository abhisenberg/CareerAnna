package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.PlayListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder>{

    private ArrayList<PlayListItem> playListItems;
    private Context context;
    private String videoId;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick1(int position);
    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        mListener = listener;
    }


    public PlaylistAdapter(ArrayList<PlayListItem> playListItems, Context context, String id) {
        this.playListItems = playListItems;
        this.context = context;
        this.videoId = id;
    }

    public void addPlayListItem(PlayListItem playListItem) {
        this.playListItems.add( playListItem);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_playlist, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.checkBoxItem.setText(playListItems.get(i).getName());
        String[] strings = playListItems.get(i).getVideoId().split(",");
        ArrayList<String> playLists = new ArrayList<>();
        Collections.addAll(playLists, strings);
        if(!playLists.contains(videoId)) {
            viewHolder.checkBoxItem.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return playListItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBoxItem;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            checkBoxItem = itemView.findViewById(R.id.check_item_playlist);

            checkBoxItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        if(mListener != null) {
                            mListener.onItemClick1(getAdapterPosition());
                        }
                    }
                }
            });
        }
    }
}
