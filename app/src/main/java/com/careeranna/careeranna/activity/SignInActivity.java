package com.careeranna.careeranna.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Constants;
import com.careeranna.careeranna.data.User;
import com.careeranna.careeranna.helper.ForgetDialog;
import com.careeranna.careeranna.helper.InternetDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, ForgetDialog.ForgetPasswordClickListener {

    public static final String TAG = "SignInActivity";
    private final static int RC_SIGN_IN = 2;

    RelativeLayout signInLayout;

    Snackbar snackbar;

    Button log_in_tab,
            sign_up_tab,
            login_btn,
            forgetPassword,
            bt_google_login,
            sign_up_btn;

    LinearLayout log_in_layout, sign_up_layout;

    TextInputLayout tl_login_email,
            tl_login_password,
            emailLayout,
            passwordLayout,
            cityLayout,
            phoneLayout,
            userNameLayout;

    String verificationCode,
            email,
            userPhone = "",
            userName,
            phoneNumber,
            city,
            password;

    private GoogleSignInClient mGoogleSignInClient;

    FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Paper.init(this);

        signInLayout = findViewById(R.id.sign_in_and_up_layout);

        log_in_tab = findViewById(R.id.log_in_tab);
        sign_up_tab = findViewById(R.id.sign_up_tab);
        forgetPassword = findViewById(R.id.forgot_password);
        bt_google_login = findViewById(R.id.google_login_btn);

        log_in_layout = findViewById(R.id.login_layout);
        sign_up_layout = findViewById(R.id.sign_up_layout);

        tl_login_email = findViewById(R.id.input_layout_email);
        tl_login_password = findViewById(R.id.input_layout_password);

        userNameLayout = findViewById(R.id.input_layout_sign_up_full_name);
        emailLayout = findViewById(R.id.input_layout_sign_up_email);
        passwordLayout = findViewById(R.id.input_layout_sign_up_password);
        phoneLayout = findViewById(R.id.input_layout_sign_up_phone);
        cityLayout = findViewById(R.id.input_layout_sign_up_city);

        login_btn = findViewById(R.id.login_btn);

        sign_up_btn = findViewById(R.id.create_account_btn);

        log_in_tab.setOnClickListener(this);
        sign_up_tab.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        bt_google_login.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        sign_up_btn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("818754991008-93r45lm5oa5d0gb54ihsd1ftppd6m60s.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(SignInActivity.this, gso);
    }

    @Override
    public void onClick(View v) {

        final int sdk = android.os.Build.VERSION.SDK_INT;

        switch (v.getId()) {

            case R.id.log_in_tab:

                changeLoginAndSignUp();
                break;

            case R.id.sign_up_tab:

                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    log_in_tab.setBackgroundDrawable(ContextCompat.getDrawable(SignInActivity.this, R.drawable.border_bottom_for_non_active_tab));
                    sign_up_tab.setBackgroundDrawable(ContextCompat.getDrawable(SignInActivity.this, R.drawable.border_bottom_for_active_tab));
                } else {
                    log_in_tab.setBackground(ContextCompat.getDrawable(SignInActivity.this, R.drawable.border_bottom_for_non_active_tab));
                    sign_up_tab.setBackground(ContextCompat.getDrawable(SignInActivity.this, R.drawable.border_bottom_for_active_tab));
                }
                sign_up_tab.setTextColor(getResources().getColor(R.color.white));
                log_in_tab.setTextColor(getResources().getColor(R.color.non_active_tab));
                log_in_layout.setVisibility(View.GONE);
                sign_up_layout.setVisibility(View.VISIBLE);
                break;

            case R.id.login_btn:

                String emailInput = tl_login_email.getEditText().getText().toString().trim();
                String pwInput = tl_login_password.getEditText().getText().toString().trim();
                if(validateUsernameAndPW(emailInput, pwInput)){
                    loginWithEmailPw(emailInput, pwInput);
                }

                break;

            case R.id.forgot_password:
                forgotPw();
                break;

            case R.id.google_login_btn:
                Log.d(TAG, "Google signin clicked ");

                if(!amIConnect()) {

                    InternetDialog internetDialog = new InternetDialog();
                    internetDialog.showDialog(this, "none");
                } else {
                    signIn();
                }
                break;
            case R.id.create_account_btn:
                if( !validateEmailTv() |
                        !validateUserCityTv() |
                        !validateUserNameTv() |
                        !validateUserNumberTv() |
                        !validateUserPasswordTv()
                ) {
                    return;
                } else {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Creating your account, please wait..");
                    progressDialog.show();

                    email = emailLayout.getEditText().getText().toString();
                    userName = userNameLayout.getEditText().getText().toString();
                    city = cityLayout.getEditText().getText().toString();
                    password = passwordLayout.getEditText().getText().toString();
                    phoneNumber = phoneLayout.getEditText().getText().toString();


                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            "https://careeranna.com/api/signUp.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            snackbar = Snackbar.make(signInLayout, response, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            progressDialog.dismiss();
                            if(response.equals("User created successfully Please Login")) {
                                changeLoginAndSignUp();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            snackbar = Snackbar.make(signInLayout, error.getMessage(), Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            progressDialog.dismiss();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            // Posting params to login url
                            String phone = phoneNumber;
                            Map<String, String> params = new HashMap<>();
                            params.put(Constants.USER_EMAIL, email);
                            params.put(Constants.USER_PASSWORD, password);
                            params.put(Constants.USER_USERNAME, userName);
                            params.put(Constants.USER_PHONE, phone);
                            params.put(Constants.USER_CITY, city);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    requestQueue.add(stringRequest);
                }
                break;
        }

    }

    public boolean validateUsernameAndPW(String emailInput, String pwInput){
        //For username/email
        if(emailInput.isEmpty()) {
            tl_login_email.setError("Required Field");
            return false;
        }

        //For password
        if(pwInput.isEmpty()) {
            tl_login_password.setError("Required Field");
            return false;
        }

        return true;
    }

    public void loginWithEmailPw(final String email, final String pw){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://careeranna.com/api/login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("url_respon", response.toString());
                User user = new User();
                try {
                    JSONObject userObject = new JSONObject(response.toString());
                    user.setUser_username(userObject.getString("USER_USERNAME"));
                    user.setUser_id(userObject.getString("USER_ID"));
                    user.setGoogle_id(userObject.getString("google_id"));
                    user.setFacebook_id(userObject.getString("facebook_id"));
                    user.setUser_photo(userObject.getString("img_url_app"));
                    user.setUser_email(userObject.getString("USER_EMAIL"));
                    Paper.book().write("user", new Gson().toJson(user));
                    snackbar = Snackbar.make(signInLayout, "Signed in as " + user.getUser_username(), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    startActivity(new Intent(SignInActivity.this, MyCourses.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                    snackbar = Snackbar.make(signInLayout,response.toString(), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                snackbar = Snackbar.make(signInLayout, getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT);
                snackbar.show();
                Log.d(TAG, "invi 4");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", pw);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SignInActivity.this);
        requestQueue.add(stringRequest);
    }

    public void forgotPw(){
        ForgetDialog forgetDialog = new ForgetDialog();
        forgetDialog.show(getSupportFragmentManager(), "Forget Password");
    }

    @Override
    public void sendMail(final String useremail) {
        email = useremail;
        int number = (int) Math.abs(Math.floor(Math.random()*90000)-10000);
        verificationCode =String.valueOf(number);
        Log.i("valus", "email : "+email + " code : " + verificationCode);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://careeranna.com/api/forgotEmail.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("url_respon", response.toString());
                if(response.toString().equals(getString(R.string.no_user_exists))) {
                    snackbar = Snackbar.make(signInLayout, response.toString(), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    Intent intent = new Intent(SignInActivity.this, PasswordReset.class);
                    intent.putExtra("email", email);
                    intent.putExtra("code", verificationCode);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                snackbar = Snackbar.make(signInLayout, getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("code", verificationCode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SignInActivity.this);
        requestQueue.add(stringRequest);
    }

    private boolean amIConnect() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    private void signIn() {
        mGoogleSignInClient.signOut();
        snackbar = Snackbar.make(signInLayout, getString(R.string.please_wait), Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
//        VISIBLING 3
        Log.d(TAG, "visi 3");
//        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "Inside onActivityResult function");

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                fireBaseAuthWithGoogle(account);
                snackbar.dismiss();
                snackbar = Snackbar.make(signInLayout, getString(R.string.fetching_your_details), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
//
                Log.d(TAG, "onActivityResult: Successful sign in");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("SignUp1", "Google sign in failed", e);
                snackbar.dismiss();
                snackbar = Snackbar.make(signInLayout, "Google sign-in failed!", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
                // ...
            }
        }
    }

    private void fireBaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("SignUp1", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignUp1", "signInWithCredential:success");
                            snackbar.dismiss();
                            snackbar = Snackbar.make(signInLayout, getString(R.string.successfully_signing_in), Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();
//
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            snackbar.dismiss();
                            snackbar = Snackbar.make(signInLayout, getString(R.string.failed_signing_in), Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();
                            Log.w("SignUp1", "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private void updateUI(final FirebaseUser userAccount) {

        if(userAccount != null) {
            if (snackbar != null)
                snackbar.dismiss();

            if (userAccount.getPhoneNumber() != null) {
                userPhone = userAccount.getPhoneNumber();
            }
            if (userAccount.getDisplayName() != null) {
                final String username = userAccount.getDisplayName().toString();
            }
            Log.d("username", userAccount.getDisplayName());
            Log.d(TAG, "updateUI: "+userAccount.getEmail());
            Log.d(TAG, "updateUI: "+userAccount.getPhotoUrl());
            final String username = userAccount.getDisplayName().toString();
            final String useremail = userAccount.getEmail();
            final String userPhoto = ""+userAccount.getPhotoUrl();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    "https://careeranna.com/api/google_login.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("url_respon", response.toString());
                    User user = new User();
                    try {
                        JSONObject userObject = new JSONObject(response.toString());
                        user.setUser_username(userObject.getString("USER_USERNAME"));
                        user.setUser_id(userObject.getString("USER_ID"));
                        user.setGoogle_id(userObject.getString("google_id"));
                        user.setFacebook_id(userObject.getString("facebook_id"));
                        user.setUser_photo(userObject.getString("img_url_app"));
                        user.setUser_email(userObject.getString("USER_EMAIL"));
                        Paper.book().write("user", new Gson().toJson(user));
                        snackbar = Snackbar.make(signInLayout, "Sign In As " + user.getUser_username(), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        Intent intent =  new Intent(SignInActivity.this, MyCourses.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        snackbar = Snackbar.make(signInLayout, response.toString(), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    snackbar = Snackbar.make(signInLayout, getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting params to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", useremail);
                    params.put("username", username);
                    params.put("phoneNumber", userPhone);
                    params.put("photo", userPhoto);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(SignInActivity.this);
            requestQueue.add(stringRequest);
        }
    }


    private boolean validateEmailTv() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailInput = emailLayout.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()) {

            emailLayout.setError("UserEmail can't be empty ");
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

            userNameLayout.setError("UserName can't be empty ");
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

            passwordLayout.setError("Password can't be empty ");
            return false;

        } else {

            passwordLayout.setError(null);
            return true;

        }
    }

    private boolean validateUserCityTv() {

        String emailInput = cityLayout.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()) {

            cityLayout.setError("City can't be empty ");
            return false;

        } else {

            cityLayout.setError(null);
            return true;

        }
    }

    private boolean validateUserNumberTv() {
        String MobilePattern = "[0-9]{10}";
        String emailInput = phoneLayout.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()) {

            phoneLayout.setError("Phone Number can't be empty ");
            return false;

        } else if(!emailInput.matches(MobilePattern)) {

            phoneLayout.setError("Enter a Valid Phone Number ");
            return false;

        }else {

            phoneLayout.setError(null);
            return true;
        }
    }

    private void changeLoginAndSignUp() {

        final int sdk = android.os.Build.VERSION.SDK_INT;

        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            log_in_tab.setBackgroundDrawable(ContextCompat.getDrawable(SignInActivity.this, R.drawable.border_bottom_for_active_tab));
            sign_up_tab.setBackgroundDrawable(ContextCompat.getDrawable(SignInActivity.this, R.drawable.border_bottom_for_non_active_tab));
        } else {
            log_in_tab.setBackground(ContextCompat.getDrawable(SignInActivity.this, R.drawable.border_bottom_for_active_tab));
            sign_up_tab.setBackground(ContextCompat.getDrawable(SignInActivity.this, R.drawable.border_bottom_for_non_active_tab));
        }
        log_in_tab.setTextColor(getResources().getColor(R.color.white));
        sign_up_tab.setTextColor(getResources().getColor(R.color.non_active_tab));
        log_in_layout.setVisibility(View.VISIBLE);
        sign_up_layout.setVisibility(View.GONE);

    }

}
