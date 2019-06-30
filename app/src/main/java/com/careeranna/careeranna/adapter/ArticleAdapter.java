package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Article;
import com.careeranna.careeranna.helper.Utils;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_article, viewGroup, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder articleViewHolder, int i) {

        if(i == 0) {
            articleViewHolder.articleContent_1.setVisibility(View.GONE);
        }

        articleViewHolder.articleTitle.setText(mArticles.get(i).getName());
        articleViewHolder.articleTitle_1.setText(mArticles.get(i).getName());
        String contentShow;
        if(mArticles.get(i).getContent().length() > 150){
            contentShow = mArticles.get(i).getContent().substring(0 , 150)+"...";
        } else {
            contentShow = mArticles.get(i).getContent();
        }
        articleViewHolder.articleContent.setText(getFormattedArticleContent(contentShow));
        articleViewHolder.articleContent_1.setText(getFormattedArticleContent(contentShow));

        Glide.with(mContext)
                .load(mArticles.get(i).getImage_url())
                .into(articleViewHolder.articleImage);

        Glide.with(mContext)
                .load(mArticles.get(i).getImage_url())
                .into(articleViewHolder.articleImage_1);

        String authorName = mArticles.get(i).getAuthor();
        articleViewHolder.articleAuthor.setText(authorName.substring(0,8) + "...");
        articleViewHolder.articleCreated.setText("Posted "+ Utils.formatDate(mArticles.get(i).getCreated_at()));

        String authorName1 = mArticles.get(i).getAuthor();
        articleViewHolder.articleAuthor_1.setText(authorName1.substring(0,8) + "...");
        articleViewHolder.articleCreated_1.setText(mArticles.get(i).getCreated_at().substring(8, 10)+getMonth(Integer.valueOf(mArticles.get(i).getCreated_at().substring(5, 7)))+", "+mArticles.get(i).getCreated_at().substring(0, 4));

        int authorImagePath;
        if(authorName.startsWith("Sri")){
            authorImagePath = R.drawable.author_srinidhi;
        } else if(authorName.startsWith("Sai")){
            authorImagePath = R.drawable.author_saima;
        } else
            authorImagePath = R.drawable.ic_person_color;

        Glide.with(mContext)
                .load(authorImagePath)
                .into(articleViewHolder.authorImage);

        Glide.with(mContext)
                .load(authorImagePath)
                .into(articleViewHolder.authorImage_1);


    }

    public String getMonth(int month) {
        String monthString;
        switch (month) {
            case 1:  monthString = " Jan";       break;
            case 2:  monthString = " Feb";      break;
            case 3:  monthString = " March";         break;
            case 4:  monthString = " April";         break;
            case 5:  monthString = " May";           break;
            case 6:  monthString = " June";          break;
            case 7:  monthString = " July";          break;
            case 8:  monthString = " Aug";        break;
            case 9:  monthString = " Sept";     break;
            case 10: monthString = " Oct";       break;
            case 11: monthString = " Nov";      break;
            case 12: monthString = " Dec";      break;
            default: monthString = "Invalid month"; break;
        }
        return monthString;
    }


    public void addArticles(ArrayList<Article> articles) {
        mArticles.addAll(articles);
        notifyDataSetChanged();
    }

    private Spanned getFormattedArticleContent(String unformattedString){
        String formattedString = "<font color=#808080>"+unformattedString+"</font>"
                +"<font color=#00C0FF> Read More</font>";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return Html.fromHtml(formattedString, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(formattedString);
        }
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }


    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView articleTitle, articleAuthor, articleAuthor_1 ,articleTitle_1, articleCreated;
        RelativeTimeTextView articleCreated_1;
        TextViewEx articleContent, articleContent_1;
        ImageView articleImage, authorImage, articleImage_1, authorImage_1;
        LinearLayout linearLayout;
        Button arrow;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            articleImage = itemView.findViewById(R.id.article_image);
            articleImage_1 = itemView.findViewById(R.id.article_image_1);
            articleTitle = itemView.findViewById(R.id.article_name);
            articleTitle_1 = itemView.findViewById(R.id.article_name_1);
            articleAuthor = itemView.findViewById(R.id.article_author_name);
            articleCreated = itemView.findViewById(R.id.create_at);
            articleContent = itemView.findViewById(R.id.article_content);
            authorImage = itemView.findViewById(R.id.article_author_image);
            articleAuthor_1 = itemView.findViewById(R.id.article_author_name_1);
            articleCreated_1 = itemView.findViewById(R.id.article_created_date_1);
            articleContent_1 = itemView.findViewById(R.id.article_content_1);
            authorImage_1 = itemView.findViewById(R.id.article_author_image_1);
            arrow = itemView.findViewById(R.id.arrow);

            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(articleContent_1.getVisibility() == View.VISIBLE) {
                        Drawable[] drawables = arrow.getCompoundDrawables();
                        // get left drawable.
                        Drawable rightCompoundDrawable = drawables[2];
                        // get new drawable.
                        Drawable img = mContext.getResources().getDrawable(R.drawable.ic_arrow_drop_down_grey_24dp);
                        // set image size (don't change the size values)
                        img.setBounds(rightCompoundDrawable.getBounds());
                        // set new drawable
                        arrow.setCompoundDrawables(null, null, img, null);
                        articleContent_1.setVisibility(View.GONE);
                    } else {
                        Drawable[] drawables = arrow.getCompoundDrawables();
                        // get left drawable.
                        Drawable rightCompoundDrawable = drawables[2];
                        // get new drawable.
                        Drawable img = mContext.getResources().getDrawable(R.drawable.ic_arrow_drop_up_grey_24dp);
                        // set image size (don't change the size values)
                        img.setBounds(rightCompoundDrawable.getBounds());
                        // set new drawable
                        arrow.setCompoundDrawables(null, null, img, null);
                        articleContent_1.setVisibility(View.GONE);
                    }
                }
            });

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
