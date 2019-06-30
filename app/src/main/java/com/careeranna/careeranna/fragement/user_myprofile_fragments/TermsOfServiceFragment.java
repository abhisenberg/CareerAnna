package com.careeranna.careeranna.fragement.user_myprofile_fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.careeranna.careeranna.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermsOfServiceFragment extends Fragment {


    WebView webView;

    public TermsOfServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_terms_of_service, container, false);

        webView = view.findViewById(R.id.webview);
        webView.loadUrl("https://www.careeranna.com/terms-of-service");

        return  view;
    }

}
