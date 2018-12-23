package com.careeranna.careeranna.misc;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.careeranna.careeranna.R;

public class AnimationEffect {

    public static final String TAG = "AnimationEffects";

    public static void slide_down(Context context, View v){
        Animation a = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void slide_up(Context context, final View v){
        Animation a = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
                v.setVisibility(View.GONE);
            }
        }
    }
}
