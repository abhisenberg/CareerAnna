package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.OrderedCourse;

import java.util.ArrayList;

public class Cart_ProductsAdapter extends RecyclerView.Adapter<Cart_ProductsAdapter.ViewHolder> {

    private ArrayList<OrderedCourse> orderedCourses;
    private Context context;

    private OnItemClickListener mListener;

    public ArrayList<OrderedCourse> getOrderedCourses() {
        return this.orderedCourses;
    }

    public Cart_ProductsAdapter(ArrayList<OrderedCourse> orderedCourses, Context context) {
        this.orderedCourses = orderedCourses;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick1(int position, String type);
    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product_in_cart_payment, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.course_name.setText(orderedCourses.get(i).getName());
        viewHolder.price.setText("₹ " +orderedCourses.get(i).getPrice());

        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    int position = i;
                    if(position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick1(i, "remove");
                    }
                }
            }
        });

    }

    public void changePrice(int pos, String Price) {
        orderedCourses.get(pos).setOld_price(orderedCourses.get(pos).getPrice());
        Float price = Float.valueOf(orderedCourses.get(pos).getPrice()) - Float.valueOf(Price);
        orderedCourses.get(pos).setPrice(""+price);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return orderedCourses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView course_name, price;

        ImageView remove;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            course_name = itemView.findViewById(R.id.course_name);
            price = itemView.findViewById(R.id.price);

            remove = itemView.findViewById(R.id.remove);

        }
    }

    public void remove(int pos) {
        orderedCourses.remove(pos);
        notifyDataSetChanged();
    }
}
