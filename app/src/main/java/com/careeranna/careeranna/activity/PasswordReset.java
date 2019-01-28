package com.careeranna.careeranna.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.user.SignUp;

import java.util.HashMap;
import java.util.Map;

public class PasswordReset extends AppCompatActivity {

    EditText verificationCode;

    TextInputLayout newPassword, confirmPassword;

    Button sendNewPassword, verifyCode;

    String code, email;

    Snackbar snackbar;

    RelativeLayout relativeLayout;

    String pass, confirmPass;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        relativeLayout = findViewById(R.id.layout);

        progressBar = findViewById(R.id.progress);

        code = getIntent().getStringExtra("code");

        email = getIntent().getStringExtra("email");

        verifyCode = findViewById(R.id.verifyCode);

        verificationCode = findViewById(R.id.verify_code);

        newPassword = findViewById(R.id.new_password);

        confirmPassword = findViewById(R.id.confirm_new_password);

        sendNewPassword = findViewById(R.id.changePassowrd);

        newPassword.setEnabled(false);

        confirmPassword.setEnabled(false);

        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code.equals(verificationCode.getText().toString())) {
                    Snackbar.make(relativeLayout, getString(R.string.security_code_match), Snackbar.LENGTH_SHORT).show();
                    newPassword.setEnabled(true);
                    sendNewPassword.setEnabled(true);
                    confirmPassword.setEnabled(true);
                } else {
                    Snackbar.make(relativeLayout, getString(R.string.security_code_not_match), Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        if (confirmPassword.getEditText() != null) {
            confirmPassword.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(newPassword.getEditText() != null) {
                        String password = newPassword.getEditText().getText().toString();
                        if (s.length() > 0 && password.length() > 0) {
                            if (!s.equals(password)) {
                                Snackbar.make(relativeLayout, getString(R.string.pw_not_match), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }

        sendNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = "";
                if (confirmPassword.getEditText() != null) {
                    confirmPass = confirmPassword.getEditText().toString();
                }
                if (newPassword.getEditText() != null) {
                    pass = newPassword.getEditText().getText().toString();
                }
                if (pass.isEmpty()) {
                    Snackbar.make(relativeLayout, "Password Could Not Be Empty !", Snackbar.LENGTH_SHORT).show();
                } else if (pass.length() < 8) {
                    Snackbar.make(relativeLayout, "Password Length Should Be Greater Than 7", Snackbar.LENGTH_SHORT).show();
                } else if (pass.equals(confirmPass)) {
                    confirmPassword.setError(getString(R.string.pw_not_match));
                    Snackbar.make(relativeLayout, getString(R.string.pw_not_match), Snackbar.LENGTH_SHORT).show();
                } else {
                    snackbar = Snackbar.make(relativeLayout, "Changing password, please wait...", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                    progressBar.setVisibility(View.VISIBLE);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            "https://careeranna.com/api/changePassword.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.INVISIBLE);
                            snackbar.dismiss();
                            snackbar = Snackbar.make(relativeLayout, response, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            Intent intent = new Intent(PasswordReset.this, SignUp.class);
                            finish();
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            snackbar = Snackbar.make(relativeLayout, getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            // Posting params to login url
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("email", email);
                            params.put("password", pass);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(PasswordReset.this);
                    requestQueue.add(stringRequest);
                }
            }
        });
    }
}
