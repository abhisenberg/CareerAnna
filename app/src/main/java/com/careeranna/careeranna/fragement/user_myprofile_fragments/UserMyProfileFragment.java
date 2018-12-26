package com.careeranna.careeranna.fragement.user_myprofile_fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.careeranna.careeranna.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserMyProfileFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "UserMyProfileFrag";
//    Button bt_signout;

    public UserMyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
//        bt_signout = view.findViewById(R.id.bt_userp_profile_signout);
//        bt_signout.setOnClickListener(this);
        /*
            Fetch the height of the device
         */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels; //This is in pixels, we have to convert this into DPs

        int heightInDP = height / getContext().getResources().getDisplayMetrics().densityDpi;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        int left_margin = getMarginsInDP(15);
        int right_margin = getMarginsInDP(15);
        int bottom_margin = getMarginsInDP(5);
        int top_margin = getMarginsInDP(heightInDP+getMarginsInDP(45));
        params.setMargins(left_margin, top_margin, right_margin, bottom_margin);
//        bt_signout.setLayoutParams(params);

        Log.d(TAG, "height = "+heightInDP);
        Log.d(TAG, "marginTop = "+top_margin);

        return view;
    }

    @Override
    public void onClick(View view) {

    }

    private int getMarginsInDP(int val){
        Resources resources = getActivity().getResources();
        int valInDP = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                val,
                resources.getDisplayMetrics()
        );
        return valInDP;
    }
}
