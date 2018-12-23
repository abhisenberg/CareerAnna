package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Banner;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<Banner> mBanners;

    public ViewPagerAdapter(Context mContext, ArrayList<Banner> mBanners) {
        this.mContext = mContext;
        this.mBanners = mBanners;
    }

    @Override
    public int getCount() {
        return mBanners.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.banner_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.bannerImage);
        Glide.with(view)
                .load(mBanners.get(position).getmLink())
                .into(imageView);
        ViewPager vp = (ViewPager) container;
        vp.addView(view,0);
        return  view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
