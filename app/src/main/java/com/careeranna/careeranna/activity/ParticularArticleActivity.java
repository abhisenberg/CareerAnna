package com.careeranna.careeranna.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.careeranna.careeranna.NotificationArticleActivity;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Article;
import com.careeranna.careeranna.data.Constants;

import io.paperdb.Paper;


public class ParticularArticleActivity extends AppCompatActivity {

    Article article;
    TextView articleName, articleAuthor, articleContent, articleCategory;
    ImageView articleImage;

    ProgressBar progressBar;

    TextView articleCreated;

    WebView webview;

    WebSettings webSettings;

    boolean showandhiden;

    FloatingActionButton fab, fab_google, fab_facebook, fab_linkedin;

    RelativeLayout relativeLayout;

    int language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_article);

        Paper.init(this);

        if(getSupportActionBar() != null) {
            if (getSupportActionBar().isShowing()) {
                getSupportActionBar().hide();
            }
        }

        fab = findViewById(R.id.fab_menu);
        fab_google = findViewById(R.id.fab_google);
        fab_facebook = findViewById(R.id.fab_facebook);
        fab_linkedin = findViewById(R.id.fab_twitter);

        relativeLayout = findViewById(R.id.relative);

        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

        try {
            language = Paper.book().read(Constants.LANGUAGE);
            Log.d("in_try", language + " ");
        } catch (NullPointerException e) {
            language = 1;
            Log.d("in_catch", language + " ");
            Paper.book().write(Constants.LANGUAGE, language);
        }


        articleName = findViewById(R.id.particle_name);
        articleAuthor = findViewById(R.id.particle_author);
        articleCreated = findViewById(R.id.particle_created);
        articleCategory = findViewById(R.id.particle_category);
        articleImage = findViewById(R.id.particle_image);
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

        webSettings = webview.getSettings();

        webSettings.setJavaScriptEnabled(true);

        Snackbar.make(relativeLayout, "Loading Content", Snackbar.LENGTH_SHORT).show();

        article = (Article) getIntent().getSerializableExtra("article");

        Glide.with(this).load(article.getImage_url()).into(articleImage);
        articleName.setText(article.getName());
        articleAuthor.setText(article.getAuthor());
        articleCreated.setText(article.getCreated_at().substring(0, 10));
        articleCategory.setText("Category : " + article.getCategory());

        if (article.getAuthor().length() > 10) {
            article.setAuthor(article.getAuthor().substring(0, 10) + "...");
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "https://careeranna.com/api/particularArticle.php?id=" + article.getId();
        Log.d("ParticularArticle", "post"+url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            response = response.replace("<img ", "<img class=\"img-thumbnail\" ");
                            response = response.replaceAll("<table .*>", "<table class=\"table table-responsive\"  >");
                            response = response.replaceAll("\\s+", " ");
                            Log.d("url_response_with_table", response);
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
                                                "<img style=\"margin-top:20px;\" class=\"img-thumbnail\" src=" + article.getImage_url() + "><br/>" + response +
                                                "</div>" +
                                                "</html>";
                                    } else if(article.getAuthor().startsWith("Sri")) {
                                        html += "<div class=\"row\"><span class=\"col col-sm-6 col-md-6\"><img src=\"https://secure.gravatar.com/avatar/a6f47b79dadb76f0331bcaf66921f657?s=35&d=mm&r=g\" class=\"avatar avatar-35 photo img-circle\" style=\"border-radius: 50%;\"height=\"30\" width=\"30\">" + article.getAuthor() + "</span><span class=\"col col-sm-6 col-md-6\">" +
                                                "<img src=\"https://www.careeranna.com/upload/calendar.svg\" class=\"avatar avatar-35 photo img-circle\" style=\"border-radius: 50%;\"height=\"30\" width=\"30\">" + article.getCreated_at().substring(0, 10) + "</span></div>" +
                                                "<img style=\"margin-top:20px;\" class=\"img-thumbnail\" src=" + article.getImage_url() + "><br/>" + response +
                                                "</div>" +
                                                "</html>";
                                    } else {
                                        html += "<div class=\"row\"><span class=\"col col-sm-6 col-md-6\"><img src=\"https://www.careeranna.com/icons/favicon-32x32.png\">" + article.getAuthor() + "</span><span class=\"col col-sm-6 col-md-6\">" +
                                                "<img src=\"https://www.careeranna.com/upload/calendar.svg\" class=\"avatar avatar-35 photo img-circle\" style=\"border-radius: 50%;\"height=\"30\" width=\"30\">" + article.getCreated_at().substring(0, 10) + "</span></div>" +
                                                "<img style=\"margin-top:20px;\" class=\"img-thumbnail\" src=" + article.getImage_url() + "><br/>" + response +
                                                "</div>" +
                                                "</html>";
                                    }
                            webview.loadData(html, "text/html", null);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
        );
        if(language == 2) {
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
                    "<h2 style=\"margin-top:20px;\">" + article.getName() + "</h2><hr/>" +
                    "<div class=\"row\"><span class=\"col col-sm-6 col-md-6\"><img src=\"https://secure.gravatar.com/avatar/8a4cbbd46914885872a3bf5109761a97?s=35&d=mm&r=g\" class=\"avatar avatar-35 photo img-circle\" style=\"border-radius: 50%;\"height=\"30\" width=\"30\">" + article.getAuthor() + "</span><span class=\"col col-sm-6 col-md-6\">" +
                    "<img src=\"https://www.careeranna.com/upload/calendar.svg\" class=\"avatar avatar-35 photo img-circle\" style=\"border-radius: 50%;\"height=\"30\" width=\"30\">" + article.getCreated_at().substring(0, 10) + "</span></div>" +
                    "<img style=\"margin-top:20px;\" class=\"img-thumbnail\" src=" + article.getImage_url() + "><br/>" + article.getContent() +
                    "</div>" +
                    "</html>";
            webview.loadData(html, "text/html", null);
            progressBar.setVisibility(View.INVISIBLE);

        } else {
            requestQueue.add(stringRequest);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showandhiden) {
                    hidden();
                    showandhiden = false;
                } else {
                    shown();
                    showandhiden = true;
                }
            }
        });
    }

    private void sendToCoursePage(String url) {

        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "https://careeranna.com/api/getCourseIdFromSlug.php?url=" + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("No Result")) {
                            Log.d("response", response);
                        } else {
                            Intent intent;
                            intent =  new Intent(getApplicationContext(), NotificationCourseActivity.class);
                            intent.putExtra("course_id", response);
                            intent.putExtra("type", "premium_course");
                            startActivity(intent);
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
        );
        requestQueue.add(stringRequest);
    }

    private void sendToAnotherArticle(String url) {

        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "https://careeranna.com/api/getNotificationId.php?url=" + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("No Result")) {

                        } else {
                            Intent intent;
                            intent =  new Intent(getApplicationContext(), NotificationArticleActivity.class);
                            intent.putExtra("article_id", response);
                            startActivity(intent);
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
        );
        requestQueue.add(stringRequest);
    }

    public void hidden() {

        fab_facebook.show();
        fab_google.show();
        fab_linkedin.show();
    }

    public void shown() {

        fab_facebook.hide();
        fab_google.hide();
        fab_linkedin.hide();
    }
}
