package com.careeranna.careeranna;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.careeranna.careeranna.adapter.OrderCourseAdapter;
import com.careeranna.careeranna.data.OrderedCourse;
import com.careeranna.careeranna.data.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.paperdb.Paper;


public class CartFragment extends Fragment implements OrderCourseAdapter.OnItemClickListener{


    private ArrayList<OrderedCourse> orderedCourses;
    RecyclerView recyclerView;

    User user;

    RelativeLayout linearLayout;

    OrderCourseAdapter orderCourseAdapter;

    Animation mAnimation;

    Snackbar snackbar;

    OrderedCourse orderedCourse;

    AlertDialog.Builder mBuilder;

    String myCourse;

    AlertDialog alertDialog;

    Button checkout;

    Dialog dialog;

    EditText promo;

    TextView price;

    LinearLayout layout;

    Intent intent;

    CardView cardView;

    float grand_total = 0;

    ArrayList<String> arrayList, arrayListWish;

    CardView card1;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        linearLayout = view.findViewById(R.id.layout);

        arrayListWish = new ArrayList<>();

        Paper.init(getContext());

        card1 = view.findViewById(R.id.card1);

        checkout = view.findViewById(R.id.checkout);

        promo = view.findViewById(R.id.promo);

        cardView = view.findViewById(R.id.card);

        recyclerView = view.findViewById(R.id.ordered_rv);

        price = view.findViewById(R.id.grand_total);

        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            user = new Gson().fromJson(cache, User.class);

            String cart = Paper.book().read("cart");

            orderedCourses = new ArrayList<>();

            if (cart != null && !cart.isEmpty()) {

                if (cache != null && !cache.isEmpty()) {

                    user = new Gson().fromJson(cache, User.class);

                }

                Log.d("cart_details", cart);

                Gson gson = new Gson();

                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();

                arrayList = gson.fromJson(cart, type);

                if (arrayList.size() > 0) {

                    cardView.setVisibility(View.INVISIBLE);

                    card1.setVisibility(View.VISIBLE);
                } else {

                    cardView.setVisibility(View.VISIBLE);

                    card1.setVisibility(View.INVISIBLE);
                }

                for (String orderedCourse : arrayList) {

                    String course[] = orderedCourse.split(",");

                    grand_total += Float.valueOf(course[1]);

                    orderedCourses.add(new OrderedCourse(course[0], course[1], course[2], course[3], course[4]));
                }


                price.setText(String.valueOf(grand_total));

                checkout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.custom_payment_layout);
                        dialog.setTitle("Pay Now ... ");
                        dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.custom_payment_layout);
                        dialog.setTitle("Pay Now ... ");

                        Button paytm, payu;

                        TextView price = dialog.findViewById(R.id.price);

                        TextView email = dialog.findViewById(R.id.email);

                        price.setText(String.valueOf(grand_total));

                        email.setText(user.getUser_email());

                        paytm = (Button) dialog.findViewById(R.id.paytm);
                        payu = (Button) dialog.findViewById(R.id.payu);

                        paytm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                intent = new Intent(getContext(), PaytmPayment.class);
                                intent.putExtra("price", grand_total);
                                startActivity(intent);
                            }
                        });

                        payu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                intent = new Intent(getContext(), Payment.class);
                                intent.putExtra("price", grand_total);
                                startActivity(intent);
                            }
                        });

                        dialog.show();
                    }
                });
            }


            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            orderCourseAdapter = new OrderCourseAdapter(orderedCourses, getContext());
            orderCourseAdapter.setOnItemClicklistener(this);
            recyclerView.setAdapter(orderCourseAdapter);
            recyclerView.smoothScrollToPosition(0);
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    int pos = (int) viewHolder.itemView.getTag();
                    removeAlert(pos);
                }
            }).attachToRecyclerView(recyclerView);
            mAnimation = new AlphaAnimation(1, 0);
            mAnimation.setDuration(200);
            mAnimation.setInterpolator(new LinearInterpolator());
            mAnimation.setRepeatCount(Animation.INFINITE);
            mAnimation.setRepeatMode(Animation.REVERSE);

        } else {
            cardView.setVisibility(View.VISIBLE);


            card1.setVisibility(View.INVISIBLE);
        }
        return view;

    }

    private void removeAlert(final int pos) {

        mBuilder = new AlertDialog.Builder(getContext());
        mBuilder.setTitle("Course Deletion");
        mBuilder.setCancelable(false);
        mBuilder.setMessage("Are you sure you want remove from cart ?");
        mBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                orderedCourse = orderedCourses.get(pos);
                orderCourseAdapter.remove(pos);

                myCourse = arrayList.get(pos);
                arrayList.remove(pos);

                Paper.book().write("cart", new Gson().toJson(arrayList));

                if(orderedCourses.size() == 0) {
                    cardView.setVisibility(View.VISIBLE);

                    card1.setVisibility(View.INVISIBLE);
                } else {

                    cardView.setVisibility(View.INVISIBLE);
                    card1.setVisibility(View.VISIBLE);
                }
                snackbar = Snackbar.make(linearLayout, "Item Removed !! ", Snackbar.LENGTH_SHORT);
                snackbar.show();
                snackbar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arrayList.add(myCourse);
                        orderedCourses.add(orderedCourse);
                        orderCourseAdapter = new OrderCourseAdapter(orderedCourses, getContext());
                        recyclerView.setAdapter(orderCourseAdapter);
                        orderCourseAdapter.setOnItemClicklistener(CartFragment.this);
                        recyclerView.smoothScrollToPosition(0);

                        Paper.book().write("cart", new Gson().toJson(arrayList));

                        if(orderedCourses.size() == 0) {
                            cardView.setVisibility(View.VISIBLE);

                            card1.setVisibility(View.INVISIBLE);
                        } else {

                            cardView.setVisibility(View.INVISIBLE);

                            card1.setVisibility(View.VISIBLE);

                        }
                    }
                });
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                orderCourseAdapter = new OrderCourseAdapter(orderedCourses, getContext());
                recyclerView.setAdapter(orderCourseAdapter);
                orderCourseAdapter.setOnItemClicklistener(CartFragment.this);
                recyclerView.smoothScrollToPosition(0);
            }
        });
        alertDialog = mBuilder.show();

    }

    @Override
    public void onItemClick1(int position, String type) {
        if (type.equals("remove")) {
            orderedCourse = orderedCourses.get(position);
            orderCourseAdapter.remove(position);

            myCourse = arrayList.get(position);
            arrayList.remove(position);

            Paper.book().write("cart", new Gson().toJson(arrayList));

            if (orderedCourses.size() == 0) {
                cardView.setVisibility(View.VISIBLE);

                card1.setVisibility(View.INVISIBLE);
            } else {

                cardView.setVisibility(View.INVISIBLE);

                card1.setVisibility(View.VISIBLE);

            }
            snackbar = Snackbar.make(linearLayout, "Item Removed !! ", Snackbar.LENGTH_SHORT);
            snackbar.show();
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrayList.add(myCourse);
                    orderedCourses.add(orderedCourse);
                    orderCourseAdapter = new OrderCourseAdapter(orderedCourses, getContext());
                    recyclerView.setAdapter(orderCourseAdapter);
                    recyclerView.smoothScrollToPosition(0);

                    Paper.book().write("cart", new Gson().toJson(arrayList));

                    if (orderedCourses.size() == 0) {
                        cardView.setVisibility(View.VISIBLE);
                        card1.setVisibility(View.INVISIBLE);
                    } else {

                        cardView.setVisibility(View.INVISIBLE);
                        card1.setVisibility(View.VISIBLE);

                    }
                }
            });
        } else if(type.equals("wish")) {

            String cart1 = Paper.book().read("wish");

            if(cart1 != null && !cart1.isEmpty()) {

                Log.i( "details",   cart1);

                Gson gson = new Gson();

                Type type1 = new TypeToken<ArrayList<String>>() {}.getType();

                ArrayList<String> arrayList = gson.fromJson(cart1, type1);

                String courseString = orderedCourses.get(position).getCourse_id() + "," + orderedCourses.get(position).getPrice() + "," + orderedCourses.get(position).getImage() + "," + orderedCourses.get(position).getName() + ","+orderedCourses.get(position).getCategory_id();

                arrayList.add(courseString);

                Paper.book().write("wish", new Gson().toJson(arrayList));

            } else {

                String courseString = orderedCourses.get(position).getCourse_id() + "," + orderedCourses.get(position).getPrice() + "," + orderedCourses.get(position).getImage() + "," + orderedCourses.get(position).getName() + ","+orderedCourses.get(position).getCategory_id();

                ArrayList<String> array = new ArrayList<>();

                array.add(courseString);

                Paper.book().write("wish", new Gson().toJson(array));
            }

            orderedCourse = orderedCourses.get(position);
            orderCourseAdapter.remove(position);

            myCourse = arrayList.get(position);
            arrayList.remove(position);

            Paper.book().write("cart", new Gson().toJson(arrayList));

            if (orderedCourses.size() == 0) {
                cardView.setVisibility(View.VISIBLE);
                card1.setVisibility(View.INVISIBLE);
            } else {

                cardView.setVisibility(View.INVISIBLE);
                card1.setVisibility(View.VISIBLE);

            }
            Toast.makeText(getContext(), "Added To WishList", Toast.LENGTH_SHORT).show();
        }
    }
}
