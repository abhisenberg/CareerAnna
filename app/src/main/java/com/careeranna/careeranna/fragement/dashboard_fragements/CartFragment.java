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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.activity.Payment;
import com.careeranna.careeranna.activity.PaytmPayment;
import com.careeranna.careeranna.adapter.OrderCourseAdapter;
import com.careeranna.careeranna.data.OrderedCourse;
import com.careeranna.careeranna.data.PromoCode;
import com.careeranna.careeranna.data.UrlConstants;
import com.careeranna.careeranna.data.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.paperdb.Paper;

import static android.content.Context.INPUT_METHOD_SERVICE;


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

    PromoCode promoCode;

    AlertDialog alertDialog;

    RelativeLayout relativeLayout;

    Button checkout, apply_promo;

    Dialog dialog;

    TextView price;

    LinearLayout layout;

    Intent intent;

    CardView cardNoCourseAdded;

    float grand_total = 0;

    ArrayList<String> arrayList, arrayListWish;


    CardView cardTotalPriceCheckout;

    EditText promocode_edit_tv;

    ArrayList<PromoCode> promoCodes;


    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        linearLayout = view.findViewById(R.id.layout);

        promocode_edit_tv = view.findViewById(R.id.promo);

        apply_promo = view.findViewById(R.id.apply);

        arrayListWish = new ArrayList<>();

        if(getContext() != null) {
            Paper.init(getContext());
        }
        cardTotalPriceCheckout = view.findViewById(R.id.card1);

        checkout = view.findViewById(R.id.checkout1);

        cardNoCourseAdded = view.findViewById(R.id.card);

        promoCodes = new ArrayList<>();

        recyclerView = view.findViewById(R.id.ordered_rv);

        price = view.findViewById(R.id.grand_total);

        apply_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getContext() != null) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                if(promocode_edit_tv.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), getString(R.string.enter_promocode) , Toast.LENGTH_SHORT).show();
                } else {

                    if(!promoCodes.isEmpty()) {
                        for(PromoCode promoCode: promoCodes) {
                            if(promoCode.getCode_name().equals(promocode_edit_tv.getText().toString())) {
                                Toast.makeText(getContext(), getString(R.string.promocode_already_applied) , Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    snackbar = Snackbar.make(linearLayout, getString(R.string.promocode_checking), Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();

                    final String promocode =promocode_edit_tv.getText().toString();
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.GET,
                            UrlConstants.PROMO_CODE_URL + promocode,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        snackbar.dismiss();
                                        Log.d("CartFragment", "promo_response : "+ response);
                                        JSONArray jsonArray = new JSONArray(response);
                                        if(jsonArray.length() == 0) {
                                            Toast.makeText(getContext(), getString(R.string.promocode_invalid) , Toast.LENGTH_SHORT).show();
                                            return;
                                        } else {
                                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                promoCode = new PromoCode(jsonObject.getString("code_id"),
                                                        jsonObject.getString("user_id"),
                                                        jsonObject.getString("code_name"),
                                                        jsonObject.getString("discount_amount"),
                                                        jsonObject.getString("active_status"),
                                                        jsonObject.getString("from_date"),
                                                        jsonObject.getString("to_date"),
                                                        jsonObject.getString("product_id"),
                                                        jsonObject.getString("promocode_details"),
                                                        jsonObject.getString("min_amount"),
                                                        jsonObject.getString("for_all"));
                                        }
                                        boolean isApplied = false;
                                        for(int j=0;j<orderedCourses.size();j++) {
                                            Log.d("PromoCodeWithId", orderedCourses.get(j).getCourse_id() + ", " + promoCode.getProduct_id());
                                            if(orderedCourses.get(j).getCourse_id().equals(promoCode.getProduct_id())) {
                                                promoCodes.add(promoCode);
                                                Toast.makeText(getContext(), getString(R.string.promocode_already_applied) + orderedCourses.get(j).getName(), Toast.LENGTH_SHORT).show();
                                                isApplied = true;
                                                orderCourseAdapter.changePrice(j, promoCode.getDiscount_amount());
                                                Float price_value = Float.valueOf(price.getText().toString());
                                                price_value -= Float.valueOf(promoCode.getDiscount_amount());
                                                price.setText(String.valueOf(price_value));
                                                break;
                                            }
                                        }
                                        if(!isApplied) {
                                            Toast.makeText(getContext(), getString(R.string.promocode_invalid) , Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    snackbar.dismiss();
                                    Toast.makeText(getContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );

                    requestQueue.add(stringRequest);
                }
            }
        });

        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            user = new Gson().fromJson(cache, User.class);

            String cart = Paper.book().read("cart");

            orderedCourses = new ArrayList<>();

            if (cart != null && !cart.isEmpty()) {

                Log.d("cart_details", cart);

                Gson gson = new Gson();

                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();

                arrayList = gson.fromJson(cart, type);

                if (arrayList.size() > 0) {
                    cardNoCourseAdded.setVisibility(View.INVISIBLE);
                    cardTotalPriceCheckout.setVisibility(View.VISIBLE);
                } else {
                    cardNoCourseAdded.setVisibility(View.VISIBLE);
                    cardTotalPriceCheckout.setVisibility(View.INVISIBLE);
                }

                for (String orderedCourse : arrayList) {

                    String course[] = orderedCourse.split(",");

                    grand_total += Float.valueOf(course[1]);

                    orderedCourses.add(new OrderedCourse(course[0], course[1], course[2], course[3], course[4]));
                }
                price.setText(String.valueOf(grand_total));

            } else {
                cardNoCourseAdded.setVisibility(View.VISIBLE);
                cardTotalPriceCheckout.setVisibility(View.INVISIBLE);
            }

            checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(getContext() != null) {

                        dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.custom_payment_layout);
                        dialog.setTitle(getString(R.string.pay_now));

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
                }
            });

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
            cardNoCourseAdded.setVisibility(View.VISIBLE);


            cardTotalPriceCheckout.setVisibility(View.INVISIBLE);
        }
        return view;

    }

    private void removeAlert(final int pos) {

        if(getContext() != null) {
            mBuilder = new AlertDialog.Builder(getContext());
            mBuilder.setTitle("Course Deletion");
            mBuilder.setCancelable(false);
            mBuilder.setMessage(getString(R.string.course_delete_confirm));
            mBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    orderedCourse = orderedCourses.get(pos);
                    orderCourseAdapter.remove(pos);

                    myCourse = arrayList.get(pos);
                    arrayList.remove(pos);

                    Paper.book().write("cart", new Gson().toJson(arrayList));

                    if (orderedCourses.size() == 0) {
                        cardNoCourseAdded.setVisibility(View.VISIBLE);

                        cardTotalPriceCheckout.setVisibility(View.INVISIBLE);
                    } else {

                        cardNoCourseAdded.setVisibility(View.INVISIBLE);
                        cardTotalPriceCheckout.setVisibility(View.VISIBLE);
                    }
                    snackbar = Snackbar.make(linearLayout, getString(R.string.course_deleted), Snackbar.LENGTH_SHORT);
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

                            if (orderedCourses.size() == 0) {
                                cardNoCourseAdded.setVisibility(View.VISIBLE);

                                cardTotalPriceCheckout.setVisibility(View.INVISIBLE);
                            } else {

                                cardNoCourseAdded.setVisibility(View.INVISIBLE);

                                cardTotalPriceCheckout.setVisibility(View.VISIBLE);

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
                cardNoCourseAdded.setVisibility(View.VISIBLE);

                cardTotalPriceCheckout.setVisibility(View.INVISIBLE);
            } else {

                cardNoCourseAdded.setVisibility(View.INVISIBLE);

                cardTotalPriceCheckout.setVisibility(View.VISIBLE);

            }
            snackbar = Snackbar.make(linearLayout, getString(R.string.course_deleted), Snackbar.LENGTH_SHORT);
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
                        cardNoCourseAdded.setVisibility(View.VISIBLE);
                        cardTotalPriceCheckout.setVisibility(View.INVISIBLE);
                    } else {

                        cardNoCourseAdded.setVisibility(View.INVISIBLE);
                        cardTotalPriceCheckout.setVisibility(View.VISIBLE);

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
                cardNoCourseAdded.setVisibility(View.VISIBLE);
                cardTotalPriceCheckout.setVisibility(View.INVISIBLE);
            } else {

                cardNoCourseAdded.setVisibility(View.INVISIBLE);
                cardTotalPriceCheckout.setVisibility(View.VISIBLE);

            }
            Toast.makeText(getContext(), getString(R.string.added_to_wishlist), Toast.LENGTH_SHORT).show();
        }
    }
}
