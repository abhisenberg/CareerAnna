package com.careeranna.careeranna.fragement.dashboard_fragements;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.activity.ParticularArticleActivity;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.ArticleAdapter;
import com.careeranna.careeranna.data.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ArticlesFragment extends Fragment implements ArticleAdapter.OnItemClickListener {

    ArrayList<Article> mArticles;

    RecyclerView recyclerView;
    ArticleAdapter articleAdapter;

    Boolean isScrolling = false;

    ProgressBar progressBar;

    int currentItems, TotalItems, scrolledOut;

    LinearLayoutManager linearLayoutManager;

    int page = 1;

    Boolean isLoading = false;

    ArrayList<Article> articles;
    ;

    public ArticlesFragment() {
        // Required empty public constructor
    }

    public void setmArticles(ArrayList<Article> articles) {

        mArticles.addAll(articles);
        populateArticles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_articles, container, false);

        progressBar = view.findViewById(R.id.progress_bar_rv);

        recyclerView = view.findViewById(R.id.article_rv);

        mArticles = new ArrayList<>();

        return view;
    }

    public void populateArticles() {

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        articleAdapter = new ArticleAdapter(mArticles, getApplicationContext());
        recyclerView.setAdapter(articleAdapter);

        articleAdapter.setOnItemClicklistener(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = linearLayoutManager.getChildCount();
                TotalItems = linearLayoutManager.getItemCount();
                scrolledOut = linearLayoutManager.findFirstVisibleItemPosition();
                if(!isLoading && isScrolling && (currentItems + scrolledOut == TotalItems )) {

                    isLoading = true;
                    progressBar.setVisibility(View.VISIBLE);
                    Snackbar.make(getActivity().findViewById(R.id.main_content), getString(R.string.loading_more_articles), Snackbar.LENGTH_SHORT).show();
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    String url = "https://careeranna.com/api/articlewithimage.php?pageno="+page;
                    page += 1;
                    articles = new ArrayList<>();
                    StringRequest stringRequest  = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.i("url_response", response.toString());
                                        JSONArray ArticlesArray = new JSONArray(response.toString());
                                        for(int i=0;i<ArticlesArray.length();i++) {
                                            JSONObject Articles = ArticlesArray.getJSONObject(i);
                                            articles.add(new Article(Articles.getString("ID"),
                                                    Articles.getString("post_title"),
                                                    "https://www.careeranna.com/articles/wp-content/uploads/"+Articles.getString("meta_value").replace("\\",""),
                                                    Articles.getString("display_name"),
                                                    "CAT",
                                                    "",
                                                    Articles.getString("post_date")));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    isLoading = false;
                                    articleAdapter.addArticles(articles);
                                    articleAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressBar.setVisibility(View.GONE);
                                    isLoading = false;
                                }
                            }
                    );

                    requestQueue.add(stringRequest);

                }
            }
        });
    }

    @Override
    public void onItemClick1(int position) {
        Intent intent = new Intent(getApplicationContext(), ParticularArticleActivity.class);
        intent.putExtra("article", mArticles.get(position));
        getContext().startActivity(intent);
    }
}
