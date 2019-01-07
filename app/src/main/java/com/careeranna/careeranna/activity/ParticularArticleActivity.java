package com.careeranna.careeranna.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
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
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Article;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_article);

        if (getSupportActionBar().isShowing()) {
            getSupportActionBar().hide();
        }

        fab = findViewById(R.id.fab_menu);
        fab_google = findViewById(R.id.fab_google);
        fab_facebook = findViewById(R.id.fab_facebook);
        fab_linkedin = findViewById(R.id.fab_twitter);

        relativeLayout = findViewById(R.id.relative);

        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);

        articleName = findViewById(R.id.particle_name);
        articleAuthor = findViewById(R.id.particle_author);
        articleCreated = findViewById(R.id.particle_created);
        articleCategory = findViewById(R.id.particle_category);
        articleImage = findViewById(R.id.particle_image);
        webview = findViewById(R.id.webview);

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
                        Log.i("url_response", response.toString());
                        response = response.replace("<img ", "<img class=\"img-thumbnail\" ");
                        response = response.replaceAll("<table .*>", "<table class=\"table table-responsive\"  >");
                        response = response.replaceAll("\\s+"," ");
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
                                "<h2 style=\"margin-top:20px;\">" + article.getName() + "</h2><hr/>" +
                                "<div class=\"row\"><span class=\"col col-sm-6 col-md-6\"><i class=\"fas fa-user-circle\"></i> " + article.getAuthor() + "</span><span class=\"col col-sm-6 col-md-6\">" +
                                "<i class=\"fas fa-calendar-alt\"></i> " + article.getCreated_at().substring(0, 10) + "</span></div>" +
                                "<img style=\"margin-top:20px;\" class=\"img-thumbnail\" src=" + article.getImage_url() + "><br/>" + response +
                                "</div>" +
                                "</html>";
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

        requestQueue.add(stringRequest);


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
