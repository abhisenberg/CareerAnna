package com.careeranna.careeranna;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.View;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

@TargetApi(Build.VERSION_CODES.M)
@RequiresApi(api = Build.VERSION_CODES.M)
public class InstructorProfileActivity extends AppCompatActivity implements RecyclerViewPager.OnPageChangedListener,
        RecyclerViewPager.OnScrollChangeListener
{

    public static final String TAG = "InstructorProfile";
    RecyclerViewPager rvp_instructors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_profile);

        rvp_instructors = findViewById(R.id.rvp_instructors);
        rvp_instructors.setFlingFactor(0.1f);
        rvp_instructors.setVerticalFadingEdgeEnabled(true);
        rvp_instructors.addOnPageChangedListener(this);
        rvp_instructors.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int childCount = rvp_instructors.getChildCount();
                int width = rvp_instructors.getChildAt(0).getWidth();
                int padding = (rvp_instructors.getWidth() - width) / 2;

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    float rate = 0;
                    ;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                        v.setScaleX(1 - rate * 0.1f);

                    } else {
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });
        rvp_instructors.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if (rvp_instructors.getChildCount() < 3) {
                    if (rvp_instructors.getChildAt(1) != null) {
                        if (rvp_instructors.getCurrentPosition() == 0) {
                            View v1 = rvp_instructors.getChildAt(1);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        } else {
                            View v1 = rvp_instructors.getChildAt(0);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        }
                    }
                } else {
                    if (rvp_instructors.getChildAt(0) != null) {
                        View v0 = rvp_instructors.getChildAt(0);
                        v0.setScaleY(0.9f);
                        v0.setScaleX(0.9f);
                    }
                    if (rvp_instructors.getChildAt(2) != null) {
                        View v2 = rvp_instructors.getChildAt(2);
                        v2.setScaleY(0.9f);
                        v2.setScaleX(0.9f);
                    }
                }
            }
        });
    }

    @Override
    public void OnPageChanged(int i, int i1) {
        Log.d(TAG, "OnPageChanged: oldPage = "+i+", newPage = "+i1);
    }

    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {

    }
}
