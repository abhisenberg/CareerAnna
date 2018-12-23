package com.careeranna.careeranna.fragement.ui_fragments;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.user.SignUp;

/**
 * A simple {@link Fragment} subclass.
 */
public class signInFields extends Fragment implements View.OnClickListener {

//    RelativeLayout bt_backButton;
    TextInputLayout et_usermail, et_userpw;
    Button bt_signIn;
    FragmentManager fragmentManager;

    public signInFields() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in_fields, container, false);
        et_usermail = view.findViewById(R.id.useremailL);
        et_userpw = view.findViewById(R.id.userpasswordL);
        bt_signIn = view.findViewById(R.id.signInAccount_2);
//        bt_backButton = view.findViewById(R.id.signIn_bt_back_fragment);
//        bt_backButton.setOnClickListener(this);

        bt_signIn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signInAccount_2:
                String emailInput = et_usermail.getEditText().getText().toString().trim();
                String pwInput = et_userpw.getEditText().getText().toString().trim();
                if(validateUsernameAndPW(emailInput, pwInput)){
                    ((SignUp)getActivity()).loginWithEmailPw(emailInput, pwInput);
                }
                break;

//            case R.id.signIn_bt_back_fragment:
//                fragmentManager.popBackStack();
//                break;
        }
    }

    public boolean validateUsernameAndPW(String emailInput, String pwInput){
        //For username/email
        if(emailInput.isEmpty()) {
            et_usermail.setError("Required Field");
            return false;
        }

        //For password
        if(pwInput.isEmpty()) {
            et_userpw.setError("Required Field");
            return false;
        }

        return true;
    }
}
