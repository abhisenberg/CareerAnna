package com.careeranna.careeranna.fragement.dashboard_fragements;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.CourseVideosAdapter;
import com.careeranna.careeranna.data.FreeVideos;
import com.careeranna.careeranna.activity.activity_with_comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

import static com.facebook.FacebookSdk.getApplicationContext;

public class VideosFragment extends Fragment implements CourseVideosAdapter.OnItemClickListener {

    ArrayList<FreeVideos> courseVideosArrayList;

    RecyclerView videos_rv;

    CourseVideosAdapter courseVideosAdapter;

    ProgressBar progressBar;

    String id = "5010";

    private Context context;

    public VideosFragment() {
    }

    public void addId(String id) {
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_videos_fragement, container, false);

        videos_rv = view.findViewById(R.id.course_videos_rv);

        context = inflater.getContext();

        progressBar = view.findViewById(R.id.progress_bar_inside_video_fragment);

        courseVideosArrayList = new ArrayList<>();

        RequestQueue requestQueue1 = Volley.newRequestQueue(inflater.getContext());
        String url = "https://www.careeranna.com/api/getParticularCourseVideoDetails.php?id="+id;

        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray VideosArray = new JSONArray(response);
                            for (int i = 0; i < VideosArray.length(); i++) {
                                JSONObject videos = VideosArray.getJSONObject(i);
                                courseVideosArrayList.add(new FreeVideos(
                                        videos.getString("id"),
                                        videos.getString("video_url").replace("\\", ""),
                                        "https://www.careeranna.com/thumbnail/" + videos.getString("thumbnail"),
                                        videos.getString("totalViews"),
                                        "",
                                        videos.getString("heading"),
                                        "Trending",
                                        videos.getString("duration"),
                                        videos.getString("likes"),
                                        videos.getString("dislikes")
                                ));
                                courseVideosArrayList.get(i).setType("Latest");
                                courseVideosArrayList.get(i).setCount_comments( videos.getString("count_comment"));
                            }
                            courseVideosAdapter = new CourseVideosAdapter(courseVideosArrayList, getApplicationContext());
                            videos_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            videos_rv.setAdapter(courseVideosAdapter);
                            courseVideosAdapter.setOnItemClicklistener(VideosFragment.this);
                            progressBar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            Log.e("VideosFragment", "onResponse: ", e.fillInStackTrace());
                            e.printStackTrace();
                            courseVideosAdapter = new CourseVideosAdapter(courseVideosArrayList, getApplicationContext());
                            videos_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            videos_rv.setAdapter(courseVideosAdapter);
                            courseVideosAdapter.setOnItemClicklistener(VideosFragment.this);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBar.setVisibility(View.GONE);

                    }
                });

        requestQueue1.add(stringRequest1);

        return view;
    }

    @Override
    public void onItemClick(int position) {
        startActivity(new Intent(context, activity_with_comment.class).putExtra("videos", courseVideosArrayList.get(position)));
    }
}
