package com.careeranna.careeranna.user;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.R;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = "RegisterActivity";

    TextInputLayout emailLayout,
            passwordLayout,
            ciytLayout,
            phoneLayout,
            userNameLayout;

    Spinner spinner_how_know;
//    Spinner spinner_state_selector;
    String email, username, countryCode, phoneNumber, state ,city, password, howtoKnow;
    ProgressDialog progressDialog;
    Button signUp;
    RelativeLayout relativeLayout;
    Snackbar snackbar;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if(getSupportActionBar().isShowing()){
            getSupportActionBar().hide();
        }

        phoneLayout = findViewById(R.id.userphoneTv1);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        relativeLayout = findViewById(R.id.snackbar_rv);
        emailLayout = findViewById(R.id.useremailTv1);
        passwordLayout = findViewById(R.id.userpasswordTv1);
        ciytLayout= findViewById(R.id.usercityTv1);
        userNameLayout = findViewById(R.id.usernameTv1);
        spinner_how_know = findViewById(R.id.how_spinner);
//        spinner_state_selector = findViewById(R.id.state_selector_spinner);
        signUp = findViewById(R.id.signUpBtn);

        spinner_how_know.setOnItemSelectedListener(this);
//        spinner_state_selector.setOnItemSelectedListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.signUpBtn:
                if( !validateEmailTv() |
                        !validateUserCityTv() |
                        !validateUserNameTv() |
                        !validateUserNmberTv() |
                        !validateUserPasswordTv()
//                        !validateState()
                        ) {
                    return;
                } else {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Creating USer Please Wait!!");
                    progressDialog.show();

                    Log.d(TAG, "Country code selected is "+countryCode);

                    countryCode = countryCodePicker.getFullNumber();
                    email = emailLayout.getEditText().getText().toString();
                    username = userNameLayout.getEditText().getText().toString();
                    city = ciytLayout.getEditText().getText().toString();
                    password = passwordLayout.getEditText().getText().toString();
                    howtoKnow = spinner_how_know.getSelectedItem().toString();
                    phoneNumber = countryCode+phoneLayout.getEditText().getText().toString();
//                    state = spinner_state_selector.getSelectedItem().toString();

                    /*
                    TODO: Send user_state to the database
                     */

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            "https://careeranna.com/api/signUp.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("url_response", "Register Response: " + response);
                            snackbar = Snackbar.make(relativeLayout, response, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            snackbar = Snackbar.make(relativeLayout, error.getMessage(), Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            progressDialog.dismiss();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            // Posting params to login url
                            Map<String, String> params = new HashMap<>();
                            params.put("email", email);
                            params.put("password", password);
                            params.put("username", username);
                            params.put("phoneNumber", countryCode+phoneNumber);
                            params.put("city", city);
                            params.put("howtoknow", howtoKnow);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    requestQueue.add(stringRequest);
                }
                break;
        }

    }

    private boolean validateEmailTv() {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailInput = emailLayout.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()) {

            emailLayout.setError("UserEmail can't be emplty ");
            return false;

        } else if(!emailInput.matches(emailPattern)) {

            emailLayout.setError("UserEmail is not valid");
            return false;

        } else {

            emailLayout.setError(null);
            return true;

        }
    }

    private boolean validateUserNameTv() {

        String emailInput = userNameLayout.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()) {

            userNameLayout.setError("UserName can't be emplty ");
            return false;

        }else if(emailInput.length() > 30) {

            userNameLayout.setError("Username can't be greater than 30");
            return false;

        } else {

            userNameLayout.setError(null);
            return true;

        }
    }

    private boolean validateUserPasswordTv() {

        String emailInput = passwordLayout.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()) {

            passwordLayout.setError("Password can't be emplty ");
            return false;

        } else {

            passwordLayout.setError(null);
            return true;

        }
    }

    private boolean validateUserCityTv() {

        String emailInput = ciytLayout.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()) {

            ciytLayout.setError("City can't be emplty ");
            return false;

        } else {

            ciytLayout.setError(null);
            return true;

        }
    }

    private boolean validateUserNmberTv() {
        String MobilePattern = "[0-9]{10}";
        String emailInput = phoneLayout.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()) {

            phoneLayout.setError("Phone Number can't be emplty ");
            return false;

        } else if(!emailInput.matches(MobilePattern)) {

            phoneLayout.setError("Enter a Valid Phone Number ");
            return false;

        }else {

            phoneLayout.setError(null);
            return true;
        }
    }
    
//    private boolean validateState(){
//        int pos = spinner_state_selector.getSelectedItemPosition();
//        if(pos == 0){
//            Toast.makeText(this, "Please select your state from the menu!", Toast.LENGTH_SHORT).show();
//            return false;
//        } else
//            return true;
//    }

    /*
    This function is necessary to set the color of the options in the spinner_how_know of
    "How did you come to know about us?"
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ((TextView)adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.register_screen_fields_color));
        ((TextView)adapterView.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Leave this empty for now
    }
}
