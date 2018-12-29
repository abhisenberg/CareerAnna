package com.careeranna.careeranna.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class ExpandableListViewForNestedScroll extends ExpandableListView {

    public ExpandableListViewForNestedScroll(Context context) {
        super(context);
    }

    public ExpandableListViewForNestedScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableListViewForNestedScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
            Integer.MAX_VALUE >> 2,
            MeasureSpec.AT_MOST
        );

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}
