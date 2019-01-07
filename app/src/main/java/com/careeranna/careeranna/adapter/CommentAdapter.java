package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Comment;
import com.careeranna.careeranna.data.User;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Comment> comments;
    private User user;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick1(int position, String comment);
    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        mListener = listener;
    }


    public CommentAdapter(ArrayList<Comment> comments, Context context, User user) {
        this.comments = comments;
        this.context = context;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment_parent, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        final CommentChildAdapter commentAdapter = new CommentChildAdapter(comments.get(i).getComments(), context);

        viewHolder.comments_tv.setText(comments.get(i).getComments().size()+" Comments");

        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.recyclerView.setHasFixedSize(true);

        viewHolder.name.setText(comments.get(i).getName());

        viewHolder.body.setText(comments.get(i).getComment_body());

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

        viewHolder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().isEmpty()) {
                    viewHolder.replyCancel.setVisibility(View.VISIBLE);
                    viewHolder.reply_child.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.replyCancel.setVisibility(View.INVISIBLE);
                    viewHolder.reply_child.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        viewHolder.replyCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.editText.setText("");
            }
        });

        viewHolder.reply_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!viewHolder.editText.getText().toString().equals("")) {
                    if(mListener != null) {

                        Comment comment1 = new Comment();
                        comment1.setName(user.getUser_username());
                        comment1.setComment_body(viewHolder.editText.getText().toString());
                        comments.get(i).getComments().add(comment1);
                        final CommentChildAdapter commentAdapter = new CommentChildAdapter(comments.get(i).getComments(), context);
                        viewHolder.recyclerView.setAdapter(commentAdapter);

                        mListener.onItemClick1(i, viewHolder.editText.getText().toString());
                    }
                }
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

        RelativeTimeTextView relativeTimeTextView;

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

            relativeTimeTextView = itemView.findViewById(R.id.timestamp);

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
