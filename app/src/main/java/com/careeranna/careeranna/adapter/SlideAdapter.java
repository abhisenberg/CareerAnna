package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.careeranna.careeranna.R;

public class SlideAdapter extends PagerAdapter {

    public static final String TAG = "SlideAdapter";

    Context context;
    LayoutInflater inflater;

    private String[] list_title;
    private String[] list_desc;
    private int[] list_images;
    private String[] list_sub;

    public SlideAdapter(Context context){
        this.context = context;

        list_title = new String[] {
                context.getResources().getString(R.string.intro_slide1_title),
                context.getResources().getString(R.string.intro_slide2_title),
                context.getResources().getString(R.string.intro_slide3_title)
        };

        list_desc = new String[] {
                context.getResources().getString(R.string.intro_slide1_desc),
                context.getResources().getString(R.string.intro_slide2_desc),
                context.getResources().getString(R.string.intro_slide3_desc)
        };

        list_sub = new String[] {
                "10 Languages. 1 Platform",
                "On Demand Videos by Top Educators",
                "Ask Anything You Need to Know"
        };

        list_images = new int[] {
                R.drawable.intro_pic_1_indian_languages,
                R.drawable.intro_pic_2_video,
                R.drawable.intro_pic_3_ques
        };

    }

    @Override
    public int getCount() {
        return list_title.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.intro_slider_2, container, false);

        ImageView intro_image = view.findViewById(R.id.intro_slide_image);
        TextView intro_title = view.findViewById(R.id.heading);
        TextView intro_sub_title = view.findViewById(R.id.sub_heading);
        TextView intro_desc = view.findViewById(R.id.intro_slide_desc);

        intro_title.setText(list_title[position]);
        intro_desc.setText(list_desc[position]);
        intro_image.setImageResource(list_images[position]);
        intro_sub_title.setText(list_sub[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
