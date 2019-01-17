package com.careeranna.careeranna.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseInstanceIDService  extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseInstance";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onTokenRefresh: "+refreshedToken);

        super.onTokenRefresh();

    }
}
