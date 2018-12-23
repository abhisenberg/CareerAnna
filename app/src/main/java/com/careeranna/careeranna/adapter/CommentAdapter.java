package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Comment> comments;
    private Context context;

    public CommentAdapter(ArrayList<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment_parent, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        CommentChildAdapter commentAdapter = new CommentChildAdapter(comments.get(i).getComments(), context);

        viewHolder.comments_tv.setText(comments.get(i).getComments().size()+" Comments");

        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.recyclerView.setHasFixedSize(true);

        viewHolder.name.setText(comments.get(i).getName());

        viewHolder.body.setText(comments.get(i).getComment_body());

        viewHolder.recyclerView.setAdapter(commentAdapter);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        Button reply;

        TextView comments_tv, name, body;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.child_rv);

            reply = itemView.findViewById(R.id.reply);

            name =  itemView.findViewById(R.id.name);

            body = itemView.findViewById(R.id.comment);

            comments_tv = itemView.findViewById(R.id.no_of_comments);

            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(recyclerView.getVisibility() == View.GONE) {

                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            });

        }
    }
}
