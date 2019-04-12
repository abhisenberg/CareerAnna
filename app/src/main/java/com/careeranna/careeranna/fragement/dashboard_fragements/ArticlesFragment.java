package com.careeranna.careeranna.fragement.dashboard_fragements;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.activity.MyCourses;
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

    int page = 2;

    Boolean isLoading = false;

    Boolean isEnded = false;

    ArrayList<Article> articles;

    private Context context;

    public ArticlesFragment() {
        // Required empty public constructor
    }

    public void setmArticles(ArrayList<Article> articles) {
        if(mArticles == null) {
            mArticles = new ArrayList<>();
            Log.d("article", "inside_null_1");
        }
        mArticles.addAll(articles);
        populateArticles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_articles, container, false);

        progressBar = view.findViewById(R.id.progress_bar_rv);

        recyclerView = view.findViewById(R.id.article_rv);

        mArticles = new ArrayList<>();

        context = inflater.getContext();

        return view;
    }

    public void populateArticles() {

        if(mArticles != null) {
            if(!mArticles.isEmpty()) {

                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);

                articleAdapter = new ArticleAdapter(mArticles, getApplicationContext());
                recyclerView.setAdapter(articleAdapter);

                articleAdapter.setOnItemClicklistener(this);

                recylerViewPopulationAgain();

            } else {
                page = 1;
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://careeranna.com/api/articlewithimage.php?pageno=" + page;
                page += 1;
                articles = new ArrayList<>();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.i("url_response", response.toString());
                                    JSONArray ArticlesArray = new JSONArray(response.toString());
                                    if(ArticlesArray.length() > 0) {
                                        for (int i = 0; i < ArticlesArray.length(); i++) {
                                            JSONObject Articles = ArticlesArray.getJSONObject(i);
                                            articles.add(new Article(Articles.getString("ID"),
                                                    Articles.getString("post_title"),
                                                    "https://www.careeranna.com/articles/wp-content/uploads/" + Articles.getString("meta_value").replace("\\", ""),
                                                    Articles.getString("display_name"),
                                                    "CAT",
                                                    "",
                                                    Articles.getString("post_date")));
                                        }
                                    }  else {
                                        Log.d("Results", "results Ended");
                                        isEnded = true;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                isLoading = false;
                                mArticles.addAll(articles);
                                progressBar.setVisibility(View.GONE);
                                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(linearLayoutManager);

                                articleAdapter = new ArticleAdapter(mArticles, getApplicationContext());
                                recyclerView.setAdapter(articleAdapter);

                                articleAdapter.setOnItemClicklistener(ArticlesFragment.this);
                                recylerViewPopulationAgain();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressBar.setVisibility(View.GONE);
                                isLoading = false;
                                recylerViewPopulationAgain();
                            }
                        }
                );

                requestQueue.add(stringRequest);
            }
        } else {
            Log.d("article", "inside_null_2");
        }


    }

    private void recylerViewPopulationAgain() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = linearLayoutManager.getChildCount();
                TotalItems = linearLayoutManager.getItemCount();
                scrolledOut = linearLayoutManager.findFirstVisibleItemPosition();
                if (!isEnded) {
                    if (!isLoading && isScrolling && (currentItems + scrolledOut == TotalItems)) {

                        isLoading = true;
                        progressBar.setVisibility(View.VISIBLE);
                        if (getActivity() != null) {
                            Snackbar.make(getActivity().findViewById(R.id.main_content), getString(R.string.loading_more_articles), Snackbar.LENGTH_SHORT).show();
                        }
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        String url = "https://careeranna.com/api/articlewithimage.php?pageno=" + page;
                        page += 1;
                        articles = new ArrayList<>();
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            Log.i("url_response", response.toString());
                                            JSONArray ArticlesArray = new JSONArray(response.toString());
                                            if(ArticlesArray.length() > 0) {
                                                for (int i = 0; i < ArticlesArray.length(); i++) {
                                                    JSONObject Articles = ArticlesArray.getJSONObject(i);
                                                    articles.add(new Article(Articles.getString("ID"),
                                                            Articles.getString("post_title"),
                                                            "https://www.careeranna.com/articles/wp-content/uploads/" + Articles.getString("meta_value").replace("\\", ""),
                                                            Articles.getString("display_name"),
                                                            "CAT",
                                                            "",
                                                            Articles.getString("post_date")));
                                                }
                                            }  else {
                                                Log.d("Results", "results Ended");
                                                isEnded = true;
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
                } else {
                    if (getActivity() != null) {
                        Snackbar.make(getActivity().findViewById(R.id.main_content), "No More Articles Left!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void onItemClick1(int position) {
        if (amIConnect()) {
            if (getContext() != null) {
                Intent intent = new Intent(getApplicationContext(), ParticularArticleActivity.class);
                intent.putExtra("article", mArticles.get(position));
                getContext().startActivity(intent);
            }
        } else {
            if (getActivity() != null) {
                ((MyCourses) getActivity()).changeInternet();
            }
        }
    }


    private boolean amIConnect() {

        if (getActivity() != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}
