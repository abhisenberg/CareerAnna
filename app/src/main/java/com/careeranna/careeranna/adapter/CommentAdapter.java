package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        CommentChildAdapter commentAdapter = new CommentChildAdapter(comments.get(i).getComments(), context);

        viewHolder.comments_tv.setText(comments.get(i).getComments().size()+" Comments");

        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.recyclerView.setHasFixedSize(true);

        viewHolder.name.setText(comments.get(i).getName());

        viewHolder.body.setText(comments.get(i).getComment_body());

        String name = "C";

        if(comments.get(i).getName() != null){
            name = comments.get(i).getName().substring(0,1).toUpperCase();
        }

        viewHolder.image.setText(name);

        viewHolder.replyCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.editText.setText("");
            }
        });

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

        Button reply, reply_child, replyCancel;

        TextView image;

        EditText editText;

        TextView comments_tv, name, body;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.child_rv);

            reply_child = itemView.findViewById(R.id.reply_parent);

            reply = itemView.findViewById(R.id.reply);

            editText = itemView.findViewById(R.id.child_tv);
            replyCancel = itemView.findViewById(R.id.reply_cancel1);

            name =  itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);

            body = itemView.findViewById(R.id.comment);

            comments_tv = itemView.findViewById(R.id.no_of_comments);

            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(recyclerView.getVisibility() == View.GONE) {
                        editText.setVisibility(View.VISIBLE);
                        reply_child.setVisibility(View.VISIBLE);
                        replyCancel.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        editText.setVisibility(View.GONE);
                        reply_child.setVisibility(View.GONE);
                        replyCancel.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            });

        }
    }
}
