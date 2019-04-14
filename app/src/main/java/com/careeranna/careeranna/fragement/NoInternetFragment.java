package com.careeranna.careeranna.fragement;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.careeranna.careeranna.R;

public class NoInternetFragment extends Fragment {

    public NoInternetFragment() {
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
                if (amIConnect()) {
                    if (mListener != null) {
                        mListener.onItemClick1();
                    }
                } else {
                    return;
                }
            }
        });

        return view;
    }


    private boolean amIConnect() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

}
