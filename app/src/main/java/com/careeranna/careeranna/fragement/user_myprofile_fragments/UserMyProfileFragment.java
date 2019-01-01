package com.careeranna.careeranna.fragement.user_myprofile_fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.careeranna.careeranna.activity.MyCourses;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserMyProfileFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "UserMyProfileFrag";
//    Button bt_signout;

    TextView tv_changeNotiPref, tv_referralCode, tv_support, tv_tnc;
    CardView openMyCourses;

    public UserMyProfileFragment() {
        // Required empty public constructors
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        tv_changeNotiPref = view.findViewById(R.id.tv_userProf_changeNotiPref);
        tv_referralCode = view.findViewById(R.id.tv_userProf_appReferralCode);
        tv_support = view.findViewById(R.id.tv_userProf_Support);
        tv_tnc = view.findViewById(R.id.tv_userProf_tnc);
        openMyCourses = view.findViewById(R.id.bt_userProf_myCourses);

        openMyCourses.setOnClickListener(this);
        tv_changeNotiPref.setOnClickListener(this);
        tv_referralCode.setOnClickListener(this);
        tv_support.setOnClickListener(this);
        tv_tnc.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        String url;
        switch (view.getId()){
            case R.id.tv_userProf_appReferralCode:
                Toast.makeText(getContext(), "Referral codes coming soon!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.tv_userProf_changeNotiPref:
                Toast.makeText(getContext(), "Notification preferences coming soon!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.tv_userProf_Support:
                url = "https://www.careeranna.com/contact";
                openLink(url);
                break;

            case R.id.tv_userProf_tnc:
                url = "https://www.careeranna.com/terms-of-service";
                openLink(url);
                break;

            case R.id.bt_userProf_myCourses:
                if(getActivity() != null)
                    getActivity().finish();
                 Intent openMyCourses = new Intent(getContext(), MyCourses.class);
                 openMyCourses.putExtra(Constants.OPEN_MY_COURSES_INTENT, true);
                 startActivity(openMyCourses);
                break;
        }
    }

    private void openLink(String url){
        Intent openLink = new Intent(Intent.ACTION_VIEW);
        openLink.setData(Uri.parse(url));
        startActivity(openLink);
    }
}
