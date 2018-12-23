package com.careeranna.careeranna.fragement.dashboard_fragements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.careeranna.careeranna.MyExamPrepActivity;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.ExamPrep;
import com.careeranna.careeranna.helper.RecyclerViewCoursesAdapter;
import com.careeranna.careeranna.helper.RecyclerViewExamAdapter;
import com.careeranna.careeranna.helper.RecyclerViewMyExamAdapter;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ExamPrepFragment extends Fragment implements RecyclerViewMyExamAdapter.OnItemClickListener{

    private ArrayList<String> names;
    private ArrayList<String> urls;

    RecyclerView recyclerViewExamp;

    RecyclerViewMyExamAdapter recyclerViewMyExamAdapter;

    private String[] imageUrls = new String[] {
            "https://4.bp.blogspot.com/-qf3t5bKLvUE/WfwT-s2IHmI/AAAAAAAABJE/RTy60uoIDCoVYzaRd4GtxCeXrj1zAwVAQCLcBGAs/s1600/Machine-Learning.png",
            "https://cdn-images-1.medium.com/max/2000/1*SSutxOFoBUaUmgeNWAPeBA.jpeg",
            "https://www.digitalvidya.com/wp-content/uploads/2016/02/Master_Digital_marketng-1170x630.jpg"
    };

    public ExamPrepFragment() {
        // Required empty public constructor
    }


    public void add(ArrayList<String> names, ArrayList<String> urls) {
        this.names = names;
        this.urls = urls;

        recyclerViewMyExamAdapter = new RecyclerViewMyExamAdapter(names, urls, getApplicationContext());
        recyclerViewExamp.setAdapter(recyclerViewMyExamAdapter);

        recyclerViewMyExamAdapter.setOnItemClicklistener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_exam_prep, container, false);

        recyclerViewExamp = view.findViewById(R.id.examprep_courses);

        initializeExamprep();

        return view;
    }


    private void initializeExamprep() {

        names = new ArrayList<>();
        urls = new ArrayList<>();

        urls.add(imageUrls[1]);
        names.add("Python");
        urls.add(imageUrls[2]);
        names.add("Marketing");
        urls.add(imageUrls[0]);
        names.add("Machine Learning");
        urls.add(imageUrls[1]);
        names.add("Python");
        urls.add(imageUrls[2]);
        names.add("Marketing");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewExamp.setLayoutManager(linearLayoutManager);

        recyclerViewMyExamAdapter = new RecyclerViewMyExamAdapter(names, urls, getApplicationContext());
        recyclerViewExamp.setAdapter(recyclerViewMyExamAdapter);

        recyclerViewMyExamAdapter.setOnItemClicklistener(this);
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(getApplicationContext(), MyExamPrepActivity.class);
        intent.putExtra("Examp", names.get(position));
        getContext().startActivity(intent);
    }
}
