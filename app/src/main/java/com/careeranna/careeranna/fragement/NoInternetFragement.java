package com.careeranna.careeranna.fragement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.ArticleAdapter;

public class NoInternetFragement extends Fragment {

    public NoInternetFragement() {
        // Required empty public constructor
    }

    Button retry;

    private OnFragemntClickListener mListener;

    public interface OnFragemntClickListener {
        void onItemClick1();
    }

    public void setOnFragementClicklistener(OnFragemntClickListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_no_internet_fragement, container, false);

        retry = view.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                        mListener.onItemClick1();
                }
            }
        });

        return view;
    }

}
