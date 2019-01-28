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

/**
 * A simple {@link Fragment} subclass.
 */
public class ErrorOccurredFragment extends Fragment {

    Button refresh;

    private OnErrorFragementClickListener mListener;

    public interface OnErrorFragementClickListener {
        void onRefreshClick();
    }

    public void setOnFragementClicklistener(OnErrorFragementClickListener listener) {
        mListener = listener;
    }

    public ErrorOccurredFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_error_occured, container, false);

        refresh = view.findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amIConnect()) {
                    if (mListener != null) {
                        mListener.onRefreshClick();
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
