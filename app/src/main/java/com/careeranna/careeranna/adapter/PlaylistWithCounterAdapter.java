package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.PlayListItem;
import com.careeranna.careeranna.data.PlayListItemWithVideo;

import java.util.ArrayList;
import java.util.Collections;

public class PlaylistWithCounterAdapter extends RecyclerView.Adapter<PlaylistWithCounterAdapter.ViewHolder>{

    private ArrayList<PlayListItemWithVideo> playListItems;
    private Context context;
    private String videoId;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick1(int position);
    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        mListener = listener;
    }


    public PlaylistWithCounterAdapter(ArrayList<PlayListItemWithVideo> playListItems, Context context) {
        this.playListItems = playListItems;
        this.context = context;
    }

    public void addPlayListItem(PlayListItemWithVideo playListItem) {
        this.playListItems.add( playListItem);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.play_list_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.playlist_name.setText(playListItems.get(i).getName());
        String[] strings = playListItems.get(i).getVideoId().split(",");
        ArrayList<String> playLists = new ArrayList<>();
        Collections.addAll(playLists, strings);
        int count = 0;
        if(!playLists.contains("0")) {
            count = playLists.size()-2;
        } else {
            count = playLists.size()-1;
        }
        viewHolder.playlist_counter.setText(count+"");

        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.recyclerView.setHasFixedSize(true);

        PlayListVideoAdapter playListVideoAdapter = new PlayListVideoAdapter(playListItems.get(i).getVideos(), context);
        viewHolder.recyclerView.setAdapter(playListVideoAdapter);

    }

    @Override
    public int getItemCount() {
        return playListItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView playlist_name, playlist_counter;

        RecyclerView recyclerView;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            playlist_name = itemView.findViewById(R.id.play_list_name);

            recyclerView = itemView.findViewById(R.id.videos_rv);

            playlist_counter = itemView.findViewById(R.id.items_count);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerView.getVisibility() == View.GONE) {
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                    }
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick1(position);
                        }
                    }
                }
            });
        }
    }
}
