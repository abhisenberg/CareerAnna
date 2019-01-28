package com.careeranna.careeranna.fragement.dashboard_fragements;


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

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.WishListAdapter;
import com.careeranna.careeranna.data.OrderedCourse;
import com.careeranna.careeranna.data.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.paperdb.Paper;


public class WishListFragment extends Fragment implements WishListAdapter.OnItemClickListener{

    public static final String TAG = "WishListFragment";

    private ArrayList<OrderedCourse> orderedCourses;
    RecyclerView recyclerView;

    User user;

    RelativeLayout linearLayout;

    WishListAdapter WishListAdapter;

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



    public WishListFragment() {
        // Required empty public constructor
        Log.d(TAG, "WishListFragment: ");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_whis_list_fragement, container, false);

        arrayListWish = new ArrayList<>();

        linearLayout = view.findViewById(R.id.layout);

        if(getContext() != null) {

            Paper.init(getContext());
        }

        checkout = view.findViewById(R.id.checkout);

        promo = view.findViewById(R.id.promo);

        cardView = view.findViewById(R.id.card);

        recyclerView = view.findViewById(R.id.ordered_rv);

        price = view.findViewById(R.id.grand_total);

        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            user = new Gson().fromJson(cache, User.class);

            String cart = Paper.book().read("wish");

            orderedCourses = new ArrayList<>();

            if (cart != null && !cart.isEmpty()) {

                Log.d("cart_details", cart);

                Gson gson = new Gson();

                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();

                arrayList = gson.fromJson(cart, type);

                for (String orderedCourse : arrayList) {

                    String course[] = orderedCourse.split(",");

                    grand_total += Float.valueOf(course[1]);

                    orderedCourses.add(new OrderedCourse(course[0], course[1], course[2], course[3], course[4]));
                }

            } else {
                cardView.setVisibility(View.VISIBLE);
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            WishListAdapter = new WishListAdapter(orderedCourses, getContext());
            WishListAdapter.setOnItemClicklistener(this);
            recyclerView.setAdapter(WishListAdapter);
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

        }
        return view;

    }

    private void removeAlert(final int pos) {

        if(getContext() != null ) {
            mBuilder = new AlertDialog.Builder(getContext());
            mBuilder.setTitle("Course Deletion");
            mBuilder.setCancelable(false);
            mBuilder.setMessage(getString(R.string.course_delete_confirm));
            mBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    orderedCourse = orderedCourses.get(pos);
                    WishListAdapter.remove(pos);

                    myCourse = arrayList.get(pos);
                    arrayList.remove(pos);

                    Paper.book().write("wish", new Gson().toJson(arrayList));

                    if (orderedCourses.size() == 0) {
                        cardView.setVisibility(View.VISIBLE);

                    } else {

                        cardView.setVisibility(View.INVISIBLE);
                    }
                    snackbar = Snackbar.make(linearLayout, getString(R.string.course_deleted), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    snackbar.setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            arrayList.add(myCourse);
                            orderedCourses.add(orderedCourse);
                            WishListAdapter = new WishListAdapter(orderedCourses, getContext());
                            recyclerView.setAdapter(WishListAdapter);
                            WishListAdapter.setOnItemClicklistener(WishListFragment.this);
                            recyclerView.smoothScrollToPosition(0);

                            Paper.book().write("wish", new Gson().toJson(arrayList));

                            if (orderedCourses.size() == 0) {
                                cardView.setVisibility(View.VISIBLE);

                            } else {

                                cardView.setVisibility(View.INVISIBLE);


                            }
                        }
                    });
                }
            });
            mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                    WishListAdapter = new WishListAdapter(orderedCourses, getContext());
                    recyclerView.setAdapter(WishListAdapter);
                    WishListAdapter.setOnItemClicklistener(WishListFragment.this);
                    recyclerView.smoothScrollToPosition(0);
                }
            });
            alertDialog = mBuilder.show();
        }

    }

    @Override
    public void onItemClick1(int position, String type) {
        if (type.equals("remove")) {
            orderedCourse = orderedCourses.get(position);
            WishListAdapter.remove(position);

            myCourse = arrayList.get(position);
            arrayList.remove(position);

            Paper.book().write("wish", new Gson().toJson(arrayList));

            if (orderedCourses.size() == 0) {
                cardView.setVisibility(View.VISIBLE);

            } else {

                cardView.setVisibility(View.INVISIBLE);


            }
            snackbar = Snackbar.make(linearLayout, getString(R.string.course_deleted), Snackbar.LENGTH_SHORT);
            snackbar.show();
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrayList.add(myCourse);
                    orderedCourses.add(orderedCourse);
                    WishListAdapter = new WishListAdapter(orderedCourses, getContext());
                    recyclerView.setAdapter(WishListAdapter);
                    recyclerView.smoothScrollToPosition(0);

                    Paper.book().write("wish", new Gson().toJson(arrayList));

                    if (orderedCourses.size() == 0) {
                        cardView.setVisibility(View.VISIBLE);
                    } else {

                        cardView.setVisibility(View.INVISIBLE);

                    }
                }
            });

        } else if(type.equals("wish")) {

            arrayListWish = new ArrayList<>();

            String cart = Paper.book().read("cart");

            if(cart != null && !cart.isEmpty()) {

                Log.i( "details",   cart);

                Gson gson = new Gson();

                Type type1 = new TypeToken<ArrayList<String>>() {}.getType();

                ArrayList<String> arrayList = gson.fromJson(cart, type1);

                String courseString = orderedCourses.get(position).getCourse_id() + "," + orderedCourses.get(position).getPrice() + "," + orderedCourses.get(position).getImage() + "," + orderedCourses.get(position).getName() + ","+orderedCourses.get(position).getCategory_id();

                arrayList.add(courseString);

                Paper.book().write("cart", new Gson().toJson(arrayList));

            } else {

                String courseString = orderedCourses.get(position).getCourse_id() + "," + orderedCourses.get(position).getPrice() + "," + orderedCourses.get(position).getImage() + "," + orderedCourses.get(position).getName() + ","+orderedCourses.get(position).getCategory_id();

                ArrayList<String> array = new ArrayList<>();

                array.add(courseString);

                Paper.book().write("cart", new Gson().toJson(array));
            }

            orderedCourse = orderedCourses.get(position);
            WishListAdapter.remove(position);

            myCourse = arrayList.get(position);
            arrayList.remove(position);

            Paper.book().write("wish", new Gson().toJson(arrayList));

            if (orderedCourses.size() == 0) {
                cardView.setVisibility(View.VISIBLE);

            } else {

                cardView.setVisibility(View.INVISIBLE);

            }
            Toast.makeText(getContext(), getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show();
        }
    }
}
