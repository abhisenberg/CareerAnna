package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Notification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.view.View.GONE;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotifViewHolder> {

    public static final String TAG = "NotificationAdapter";

    private ArrayList<Notification> notificationsList;
    private Context context;
    private OnNotifClickListener listener;

    public NotificationAdapter(Context context){
        this.context = context;
        notificationsList = new ArrayList<>();
    }

    public void updateData(ArrayList<Notification> notificationsList){
        this.notificationsList = notificationsList;
    }

    public interface OnNotifClickListener {
        void onItemClick(int position);
    }

    public void setOnNotifClickListener(OnNotifClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification_list, viewGroup, false);
        return new NotifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifViewHolder notifViewHolder, int i) {
        Notification notification = notificationsList.get(i);

        notifViewHolder.tv_noti_desc.setText(notification.getTitle());
        notifViewHolder.tv_noti_title.setText(notification.getTitle());
        if(notification.getTitle()!= null) {
            if(notification.getTitle().length() >= 120) {
                notifViewHolder.tv_noti_desc.setText(notification.getTitle().substring(0, 120));
            }
        }
        if(notification.getDate() != null) {
            notifViewHolder.tv_noti_date.setText(
                    notification.getDate());
        }
        notifViewHolder.noti_image.setVisibility(GONE);
        Glide.with(context)
                .load(notification.getImage_url())
                .into(notifViewHolder.noti_image);

    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class NotifViewHolder extends RecyclerView.ViewHolder{

        ImageView noti_image;
        TextView tv_noti_title, tv_noti_date, tv_noti_desc;

        public NotifViewHolder(@NonNull View itemView) {
            super(itemView);

            noti_image = itemView.findViewById(R.id.noti_image);
            tv_noti_date = itemView.findViewById(R.id.noti_time);
            tv_noti_desc = itemView.findViewById(R.id.noti_content);
            tv_noti_title = itemView.findViewById(R.id.noti_heading);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
