package com.careeranna.careeranna.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.activity.MyCourses;
import com.careeranna.careeranna.activity.PasswordReset;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Constants;
import com.careeranna.careeranna.data.User;
import com.careeranna.careeranna.helper.ChangeLanguageDialog;
import com.careeranna.careeranna.helper.ForgetDialog;
import com.careeranna.careeranna.helper.InternetDialog;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class SignUp extends AppCompatActivity implements View.OnClickListener, ForgetDialog.ForgetPasswordClickListener {


    public static final String TAG = "SignUpActivity";
    private final static int RC_SIGN_IN = 2;


//    TextInputLayout et_usermail,
//            et_userpassword;

    boolean showDimmer;

    FrameLayout frame_dimmer;

    EditText et_usermail, et_userpassword;

    RelativeLayout relativeLayout;

    FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;

//    GoogleSignInButton googleLoginButton;
    SignInButton googleSigninButton;

    private CallbackManager mCallbackManager;

    Button bt_google_login, bt_fb_login, bt_create_account, bt_manual_login;

    TextView tv_forgotPw;

    LoginButton fbLoginButton;

//    ProgressBar progressBar;

    AlertDialog.Builder builder;

    Snackbar snackbar;

    AlertDialog alert;

    String userphone = "";

    String verificationCode, email;

    private boolean isFieldsFragmentShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up) ;

        if(getSupportActionBar().isShowing()){
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_sign_up_3);

        //************Drawing the center line and circle ************
        /*
        Get the screen width, set the circle's width and height = width/9
         */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        View centerCircle = findViewById(R.id.view_centerCircle);
        centerCircle.setLayoutParams(new LinearLayout.LayoutParams(width/9, width/9));
        //***********************************************************


        Paper.init(this);

        showDimmer = false;

        frame_dimmer = findViewById(R.id.signin_dimmer);

        relativeLayout = findViewById(R.id.snackbar_3);

        bt_fb_login = findViewById(R.id.bt_facebook_login);

        bt_google_login = findViewById(R.id.bt_google_login_1);

        googleSigninButton = findViewById(R.id.bt_google_sign_in_button_3);

//        progressBar = findViewById(R.id.signUp_progressCircle_3);

        tv_forgotPw = findViewById(R.id.tv_forgot_password_3);

        fbLoginButton =  findViewById(R.id.fb_login_button_3);

        bt_manual_login = findViewById(R.id.signInAccount_3);

//        fragmentManager = getSupportFragmentManager();

        bt_google_login.setOnClickListener(this);

        bt_fb_login.setOnClickListener(this);
//        googleSigninButton.setOnClickListener(this);

        et_usermail = findViewById(R.id.et_email);

        et_userpassword = findViewById(R.id.et_pw);

        mAuth = FirebaseAuth.getInstance();

        bt_create_account = findViewById(R.id.bt_create_account_3);

        bt_create_account.setOnClickListener(this);

        bt_manual_login.setOnClickListener(this);

        tv_forgotPw.setOnClickListener(this);

        //Hide the circuler progress bar and only show when needed
        //INVISIBLING 2
        Log.d(TAG, "invi 2");
        frame_dimmer.setVisibility(View.INVISIBLE);
//        progressBar.bringToFront();
//        progressBar.setVisibility(View.GONE);

        //Show the signIn_Buttons fragment at the starting, not the signIn_Fields fragment
//        isFieldsFragmentShowing = false;
//        FragmentTransaction signInBtTrans = fragmentManager.beginTransaction();
//            signInBtTrans.replace(R.id.fragment_btAndFields, new signIn_buttons());
//            signInBtTrans.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//            signInBtTrans.commit();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(SignUp.this, gso);

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();

        fbLoginButton.setReadPermissions("email, public_profile");

        fbLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                snackbar = Snackbar.make(relativeLayout, "Please Wait...", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
                //VISIBLING 1
                Log.d(TAG, "visi 1");
                frame_dimmer.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.VISIBLE);

                Log.d("facebook", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.d("facebook", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("facebook", "facebook:onError", error);
            }

        });

        int language;

        try {
            language = Paper.book().read(Constants.LANGUAGE);
            Log.d("in_try", language +" ");
        } catch (Exception e){
            openLanguageChangeDialog();
        }

    }

    @Override
    public void onStart() {

        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

                case R.id.bt_google_login_1:
                    Log.d(TAG, "Google signin clicked ");

                    if(!amIConnect()) {

                        InternetDialog internetDialog = new InternetDialog();
                        internetDialog.showDialog(this, "none");
                    } else {
                        signIn();
                    }
                    break;

            case R.id.bt_facebook_login:
                Log.d(TAG, "Facebook signin clicked ");
//                VISIBLING 2
                Log.d(TAG, "visi 2");
                frame_dimmer.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.VISIBLE);
                fbLoginButton.performClick();
                break;

            case R.id.signInAccount_3:
                String emailInput = et_usermail.getText().toString().trim();
                String pwInput = et_userpassword.getText().toString().trim();
                if(validateUsernameAndPW(emailInput, pwInput)){
                    loginWithEmailPw(emailInput, pwInput);
                }
                break;

            case R.id.tv_forgot_password_3:
                forgotPw();
                break;

            case R.id.bt_create_account_3:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    public void forgotPw(){
        ForgetDialog forgetDialog = new ForgetDialog();
        forgetDialog.show(getSupportFragmentManager(), "Forget Password");
    }

    private void signIn() {
        snackbar = Snackbar.make(relativeLayout, getString(R.string.please_wait), Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
//        VISIBLING 3
        Log.d(TAG, "visi 3");
        frame_dimmer.setVisibility(View.VISIBLE);
        showDimmer = true;
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
            Log.d(TAG, "Stopping dimmer");
            showDimmer = false;
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                snackbar.dismiss();
                snackbar = Snackbar.make(relativeLayout, getString(R.string.fetching_your_details), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
//
                Log.d(TAG, "onActivityResult: Successful sign in");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("SignUp1", "Google sign in failed", e);
                snackbar.dismiss();
                snackbar = Snackbar.make(relativeLayout, "Google sign-in failed!", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
                // ...
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
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
                            snackbar = Snackbar.make(relativeLayout, getString(R.string.successfully_signing_in), Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();
//
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            snackbar.dismiss();
                            snackbar = Snackbar.make(relativeLayout, getString(R.string.failed_signing_in), Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();
                            Log.w("SignUp1", "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d("facebook", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        INVISIBLING 3
                        Log.d(TAG, "invi 3");
                        frame_dimmer.setVisibility(View.INVISIBLE);
//                        progressBar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback(){

                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    try {
                                        Log.i("facebook_email", object.getString("email"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("facebook", "signInWithCredential:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("facebook", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
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
                userphone = userAccount.getPhoneNumber();
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
                            snackbar = Snackbar.make(relativeLayout, "Sign In As " + user.getUser_username(), Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            Intent intent =  new Intent(SignUp.this, MyCourses.class);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            snackbar = Snackbar.make(relativeLayout, response.toString(), Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        frame_dimmer.setVisibility(View.INVISIBLE);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    snackbar = Snackbar.make(relativeLayout, getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    frame_dimmer.setVisibility(View.INVISIBLE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting params to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", useremail);
                    params.put("username", username);
                    params.put("phoneNumber", userphone);
                    params.put("photo", userPhoto);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
            requestQueue.add(stringRequest);
        }
    }

    private boolean amIConnect() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

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
                    snackbar = Snackbar.make(relativeLayout, response.toString(), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    Intent intent = new Intent(SignUp.this, PasswordReset.class);
                    intent.putExtra("email", email);
                    intent.putExtra("code", verificationCode);
                    startActivity(intent);
                }
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
                params.put("code", verificationCode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
        requestQueue.add(stringRequest);
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
                    snackbar = Snackbar.make(relativeLayout, "Signed in as " + user.getUser_username(), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    startActivity(new Intent(SignUp.this, MyCourses.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                    snackbar = Snackbar.make(relativeLayout,response.toString(), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                frame_dimmer.setVisibility(View.INVISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                snackbar = Snackbar.make(relativeLayout, getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT);
                snackbar.show();
//                INVISIBLING 4
                Log.d(TAG, "invi 4");
                frame_dimmer.setVisibility(View.INVISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
//        progressBar.setVisibility(View.INVISIBLE);
        //INVISIBLING 1
        Log.d(TAG, "invi 1, showing dimmer = "+showDimmer);
//        frame_dimmer.setVisibility(View.VISIBLE);
        if(showDimmer)
            frame_dimmer.setVisibility(View.VISIBLE);
        else
            frame_dimmer.setVisibility(View.INVISIBLE);

        super.onResume();
    }

    public boolean validateUsernameAndPW(String emailInput, String pwInput){
        //For username/email
        if(emailInput.isEmpty()) {
            et_usermail.setError("Required Field");
            return false;
        }

        //For password
        if(pwInput.isEmpty()) {
            et_userpassword.setError("Required Field");
            return false;
        }

        return true;
    }

    public void openLanguageChangeDialog(){
        ChangeLanguageDialog changeLanguageDialog = new ChangeLanguageDialog(this);
        changeLanguageDialog.showDialog();
    }

}
