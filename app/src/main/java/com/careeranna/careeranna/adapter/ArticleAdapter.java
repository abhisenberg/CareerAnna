package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Article;
import com.careeranna.careeranna.misc.TextViewEx;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    public static final String TAG = "ArticleAdapter";

    private ArrayList<Article> mArticles;
    private Context mContext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick1(int position);
    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ArticleAdapter(ArrayList<Article> mArticles, Context mContext) {
        this.mArticles = mArticles;
        this.mContext = mContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_article_2, viewGroup, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder articleViewHolder, int i) {

//        if(mArticles.get(i).getName().length() > 50) {
//            articleViewHolder.articleTitle.setText(mArticles.get(i).getName().substring(0,50) + "...");
//        } else {
//            articleViewHolder.articleTitle.setText(mArticles.get(i).getName());
//        }
        articleViewHolder.articleTitle.setText(mArticles.get(i).getName());
        if(mArticles.get(i).getContent().length() > 250){
            String contentShow = mArticles.get(i).getContent().substring(0 , 250)+"...";
            articleViewHolder.articleContent.setText(contentShow, true);
        } else {
            articleViewHolder.articleContent.setText(mArticles.get(i).getContent(), true);
        }
        articleViewHolder.articleAuthor.setText(mArticles.get(i).getAuthor());
        articleViewHolder.articleCreated.setText(mArticles.get(i).getCreated_at().substring(0, 10));
        articleViewHolder.articleAuthor.setText(mArticles.get(i).getAuthor());
        Glide.with(mContext).load(mArticles.get(i).getImage_url()).into(articleViewHolder.articleImage);

        Log.d(TAG, "Image url is "+mArticles.get(i).getImage_url());
    }

    public void addArticles(ArrayList<Article> articles) {
        mArticles.addAll(articles);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }


    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView articleTitle, articleAuthor;
        RelativeTimeTextView articleCreated;
        TextViewEx articleContent;
        ImageView articleImage;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            articleImage = itemView.findViewById(R.id.article_image);
            articleTitle = itemView.findViewById(R.id.article_name);
            articleAuthor = itemView.findViewById(R.id.article_author_name);
            articleCreated = itemView.findViewById(R.id.article_created_date);
            articleContent = itemView.findViewById(R.id.article_content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
