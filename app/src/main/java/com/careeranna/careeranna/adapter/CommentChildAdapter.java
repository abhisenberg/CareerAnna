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

import java.util.ArrayList;

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
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.comment);

        }
    }
}
