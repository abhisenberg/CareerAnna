package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.dummy_data.ProfileCerti;
import com.careeranna.careeranna.dummy_data.ProfileCerti;

import java.util.List;

public class MyCerti_Adapter extends RecyclerView.Adapter<MyCerti_Adapter.ViewHolder> implements View.OnClickListener {

    public static final String TAG = "adapter_myCerti";

    private List<ProfileCerti> myCertiList;
    private Context context;

    public MyCerti_Adapter(Context context, List<ProfileCerti> myCertiList){
        this.context = context;
        this.myCertiList = myCertiList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_mycertis, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        ProfileCerti myCourse = myCertiList.get(i);
        holder.tv_certiName.setText(myCertiList.get(i).getCertiName());
        holder.tv_certiDate.setText(myCertiList.get(i).getCertiDate());
        holder.tv_certiDownload.setOnClickListener(this);
        //Here set the image of the course from the image link
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+myCertiList.size());
        return myCertiList.size();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_certiDownload:
                Toast.makeText(context, "Download functionality test", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_certiName, tv_certiDate, tv_certiDownload;
        ImageView iv_certiImg;

        public ViewHolder(@NonNull View view) {
            super(view);

            tv_certiDate = view.findViewById(R.id.tv_certiDate);
            tv_certiName = view.findViewById(R.id.tv_certiName);
            iv_certiImg = view.findViewById(R.id.iv_certiImage);
            tv_certiDownload = view.findViewById(R.id.tv_certiDownload);

        }
    }

}