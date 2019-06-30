package com.careeranna.careeranna;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.activity.NotificationCourseActivity;
import com.careeranna.careeranna.data.Article;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationArticleActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;

    String article_id = "";
    String article_fetch_url = "https://www.careeranna.com/api/fetchNotificationArticleDetailApp.php?article_id=";

    Article article;

    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_article);

        relativeLayout = findViewById(R.id.progress_bar_layout);
        webview = findViewById(R.id.webview);

        webview.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("https://www.careeranna.com/articles/")) {
                    sendToAnotherArticle(url);
                } else if(url.contains("https://www.careeranna.com/")) {
                    sendToCoursePage(url);
                }
                return true;
            }
        });

        if(getIntent() != null) {
            if(getIntent().hasExtra("article_id")) {
                article_id = getIntent().getStringExtra("article_id");
                article_fetch_url += article_id;
            }
        }

        fetchArticleDetails();

    }

    private void fetchArticleDetails() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                article_fetch_url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            article = new Article(response.getString("ID"),
                                    response.getString("post_title"),
                                    "https://www.careeranna.com/articles/wp-content/uploads/" + response.getString("meta_value").replace("\\", ""),
                                    response.getString("display_name"),
                                    "CAT",
                                    response.getString("post_content"),
                                    response.getString("post_date"));
                        } catch (JSONException e) {
                            Toast.makeText(NotificationArticleActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                        }
                        String content = article.getContent();
                        content = content.replace("<img ", "<img class=\"img-thumbnail\" ");
                        content = content.replaceAll("<table .*>", "<table class=\"table table-responsive\"  >");
                        content = content.replaceAll("\\s+", " ");
                        String html = "<html>" +
                                "<head>" +
                                "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\n" +
                                "<link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet'>" +
                                "<link rel=\"stylesheet\" href=\"https://use.fontawesome.com/releases/v5.6.3/css/all.css\" integrity=\"sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/\" crossorigin=\"anonymous\">" +
                                "<style>" +
                                "body {\n" +
                                "    font-family: Roboto; " +
                                "}" +
                                "table tr:nth-child(odd) {" +
                                "   background: #d9e2f3;" +
                                "}" +
                                "table tr:nth-child(1) {" +
                                "  background: #11336f !important;" +
                                "}" +
                                "table tr:nth-child(1) td span {" +
                                "    color: #fff !important;" +
                                "}" +
                                "</style>" +
                                "</head>" +
                                "<body>" +
                                "<div class=\"container\">" +
                                "<h2 style=\"margin-top:20px;\">" + article.getName() + "</h2><hr/>";
                        if(article.getAuthor().startsWith("Sai")) {
                            html += "<div class=\"row\"><span class=\"col col-sm-6 col-md-6\"><img src=\"https://secure.gravatar.com/avatar/8a4cbbd46914885872a3bf5109761a97?s=35&d=mm&r=g\" class=\"avatar avatar-35 photo img-circle\" style=\"border-radius: 50%;\"height=\"30\" width=\"30\">" + article.getAuthor() + "</span><span class=\"col col-sm-6 col-md-6\">" +
                                    "<img src=\"https://www.careeranna.com/upload/calendar.svg\" class=\"avatar avatar-35 photo img-circle\" style=\"border-radius: 50%;\"height=\"30\" width=\"30\">" + article.getCreated_at().substring(0, 10) + "</span></div>" +
                                    "<img style=\"margin-top:20px;\" class=\"img-thumbnail\" src=" + article.getImage_url() + "><br/>" + content +
                                    "</div>" +
                                    "</html>";
                        } else if(article.getAuthor().startsWith("Sri")) {
                            html += "<div class=\"row\"><span class=\"col col-sm-6 col-md-6\"><img src=\"https://secure.gravatar.com/avatar/a6f47b79dadb76f0331bcaf66921f657?s=35&d=mm&r=g\" class=\"avatar avatar-35 photo img-circle\" style=\"border-radius: 50%;\"height=\"30\" width=\"30\">" + article.getAuthor() + "</span><span class=\"col col-sm-6 col-md-6\">" +
                                    "<img src=\"https://www.careeranna.com/upload/calendar.svg\" class=\"avatar avatar-35 photo img-circle\" style=\"border-radius: 50%;\"height=\"30\" width=\"30\">" + article.getCreated_at().substring(0, 10) + "</span></div>" +
                                    "<img style=\"margin-top:20px;\" class=\"img-thumbnail\" src=" + article.getImage_url() + "><br/>" + content +
                                    "</div>" +
                                    "</html>";
                        } else {
                            html += "<div class=\"row\"><span class=\"col col-sm-6 col-md-6\"><img src=\"https://www.careeranna.com/icons/favicon-32x32.png\">" + article.getAuthor() + "</span><span class=\"col col-sm-6 col-md-6\">" +
                                    "<img src=\"https://www.careeranna.com/upload/calendar.svg\" class=\"avatar avatar-35 photo img-circle\" style=\"border-radius: 50%;\"height=\"30\" width=\"30\">" + article.getCreated_at().substring(0, 10) + "</span></div>" +
                                    "<img style=\"margin-top:20px;\" class=\"img-thumbnail\" src=" + article.getImage_url() + "><br/>" + content +
                                    "</div>" +
                                    "</html>";
                        }
                        webview.loadData(html, "text/html", null);
                        relativeLayout.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        relativeLayout.setVisibility(View.GONE);
                        Toast.makeText(NotificationArticleActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void sendToCoursePage(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "https://careeranna.com/api/getCourseIdFromSlug.php?url=" + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("No Result")) {

                        } else {
                            Intent intent;
                            intent =  new Intent(getApplicationContext(), NotificationCourseActivity.class);
                            intent.putExtra("course_id", response);
                            intent.putExtra("type", "premium_course");
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        requestQueue.add(stringRequest);
    }

    private void sendToAnotherArticle(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "https://careeranna.com/api/getNotificationId.php?url=" + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("No Result")) {

                        } else {
                            Intent intent;
                            intent =  new Intent(getApplicationContext(), NotificationArticleActivity.class);
                            intent.putExtra("article_id", response);
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        requestQueue.add(stringRequest);
    }
}

