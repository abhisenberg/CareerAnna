package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Comment;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommentChildAdapter  extends RecyclerView.Adapter<CommentChildAdapter.ViewHolder> {

    private ArrayList<Comment> comments;
    private Context context;

    public CommentChildAdapter(ArrayList<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentChildAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment_child, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.name.setText(comments.get(i).getName());
        viewHolder.comment.setText(comments.get(i).getComment_body());
        String name = "G";

        String myDate = comments.get(i).getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();

        viewHolder.relativeTimeTextView.setReferenceTime(millis);

        if(comments.get(i).getName() != null){
            name = comments.get(i).getName().substring(0,1).toUpperCase();
        }

        viewHolder.image.setText(name);

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, comment, image;

        RelativeTimeTextView relativeTimeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);

            relativeTimeTextView = itemView.findViewById(R.id.timestamp);

            comment = itemView.findViewById(R.id.comment);

            image  = itemView.findViewById(R.id.image);
        }
    }
}
