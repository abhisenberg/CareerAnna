package com.careeranna.careeranna.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.careeranna.careeranna.adapter.Cart_ProductsAdapter;
import com.careeranna.careeranna.adapter.OrderCourseAdapter;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.ExamPrep;
import com.careeranna.careeranna.data.OrderedCourse;
import com.careeranna.careeranna.data.PromoCode;
import com.careeranna.careeranna.data.UrlConstants;
import com.careeranna.careeranna.data.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class PaymentGateway extends AppCompatActivity implements Cart_ProductsAdapter.OnItemClickListener{

    Button  apply_promo, payment;

    RelativeLayout paytm, payu;

    TextView tv_price, error_message, actual_price, gst_price_tv, error_message_1, sub_total_price;

    EditText name, phone, city, tv_email;

    String price;

    EditText promocode_edit_tv;

    User user;

    ArrayList<PromoCode> promoCodes;

    PromoCode promoCode;

    Snackbar snackbar;

    RequestQueue requestQueue;


    ArrayList<String> arrayList;

    Course course;

    RecyclerView recyclerView;

    Cart_ProductsAdapter cart_productsAdapter;

    private ArrayList<OrderedCourse> orderedCourses;

    LinearLayout linearLayout;

    long gst_price;

    long original_price;
    long grand_total = 0;

    String ids = "";
    String price1 = "";
    String discount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("CheckOut");
        }

        linearLayout = findViewById(R.id.payment_gateway);

        promoCodes = new ArrayList<>();
        payment = findViewById(R.id.payment_method);

        sub_total_price = findViewById(R.id.sub_total_price);


        recyclerView = findViewById(R.id.my_product_cart_rv);

        gst_price_tv = findViewById(R.id.gst_price);

        Paper.init(this);

        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            user = new Gson().fromJson(cache, User.class);
        }

        String cart = Paper.book().read("cart1");

        tv_price = findViewById(R.id.total_price);

        orderedCourses = new ArrayList<>();

        if (cart != null && !cart.isEmpty()) {
            Gson gson = new Gson();

            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();

            arrayList = gson.fromJson(cart, type);

            for (String orderedCourse : arrayList) {

                String course[] = orderedCourse.split(",");

                grand_total += Integer.valueOf(course[1]);

                orderedCourses.add(new OrderedCourse(course[0], course[1], course[2], course[3], course[4], course[1], course[6], course[7]));
            }

            sub_total_price.setText("₹ "+grand_total+"");

            gst_price = grand_total + Math.round(grand_total*0.18);

            gst_price_tv.setText(String.format("₹ %d",  Math.round(grand_total*0.18)));

            tv_price.setText(String.format("₹ %d", gst_price)+"");

            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

            cart_productsAdapter = new Cart_ProductsAdapter(orderedCourses, this);
            cart_productsAdapter.setOnItemClicklistener(this);

            recyclerView.setAdapter(cart_productsAdapter);


        }
        tv_email = findViewById(R.id.email);

        error_message = findViewById(R.id.error_message);
        error_message_1 = findViewById(R.id.error_message_1);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.mobile);
        city = findViewById(R.id.city);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        tv_email.setText(user.getUser_email());

        if(user.getUser_username() != null) {
            name.setText(user.getUser_username());
        }
        if(user.getUser_phone() != null) {
            phone.setText(user.getUser_phone());
        }
        if(user.getUser_city() != null) {
            city.setText(user.getUser_city());
        }

        paytm = findViewById(R.id.paytm_linear_layout);
        payu = findViewById(R.id.payu_biz_linear_layout);

        apply_promo = findViewById(R.id.apply);
        promocode_edit_tv = findViewById(R.id.promo);

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().length() > 0 &&
                        phone.getText().toString().length() > 0 &&
                        city.getText().toString().length() > 0
                ) {

                    ids = "";
                    price1 = "";
                    discount = "";

                    for(OrderedCourse orderedCourse: cart_productsAdapter.getOrderedCourses()) {
                        ids += orderedCourse.getCourse_id() + ",";

                        price1 += orderedCourse.getOld_price() + ",";

                        if(orderedCourse.getPrice().equals(orderedCourse.getOld_price())) {
                            discount += 0 + ",";
                        } else {
                            discount += orderedCourse.getPrice() + ",";
                        }
                    }

                    Intent intent = new Intent(PaymentGateway.this, PaymentMethodActivity.class);
                    intent.putExtra("grand_total", grand_total+"");
                    intent.putExtra("gst_total", Math.round(grand_total*0.18));
                    intent.putExtra("gst_price", gst_price+"");
                    intent.putExtra("ids", ids+"");
                    intent.putExtra("product_prices", price1+"");
                    intent.putExtra("discount_prices", discount+"");
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("phone", phone.getText().toString());
                    intent.putExtra("city", city.getText().toString());
                    intent.putExtra("email", tv_email.getText().toString());
                    intent.putExtra("OrderedCourses", cart_productsAdapter.getOrderedCourses());
                    startActivity(intent);
////
//                    Intent intent = new Intent(PaymentGateway.this, PaytmPayment.class);
//                    intent.putExtra("price", String.format("%.1f", gst_price)+"");
//                    intent.putExtra("name", name.getText().toString());
//                    intent.putExtra("phone", phone.getText().toString());
//                    intent.putExtra("city", city.getText().toString());
//                    intent.putExtra("email", tv_email.getText().toString());
//                    startActivity(intent);
                  //  courseCheckout();
                } else {
                    error_message.setText("Please Enter Valid Details");
                    error_message.setVisibility(View.VISIBLE);
                    error_message_1.setText("Please Enter Valid Details");
                    error_message_1.setVisibility(View.VISIBLE);
                }

            }
        });

        payu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().length() > 0 &&
                        phone.getText().toString().length() > 0 &&
                        phone.getText().toString().length() > 9 &&
                        city.getText().toString().length() > 0
                ) {

                    Intent intent = new Intent(PaymentGateway.this, PaymentMethodActivity.class);
                    intent.putExtra("grand_total", grand_total+"");
                    intent.putExtra("gst_total", Math.round(grand_total*0.18));
                    intent.putExtra("gst_price", gst_price+"");
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("phone", phone.getText().toString());
                    intent.putExtra("city", city.getText().toString());
                    intent.putExtra("email", tv_email.getText().toString());
                    intent.putExtra("OrderedCourses", cart_productsAdapter.getOrderedCourses());
                    startActivity(intent);

//                    Intent intent = new Intent(PaymentGateway.this, Payment.class);
//                    intent.putExtra("price", String.format("%.1f", gst_price) +"");
//                    intent.putExtra("name", name.getText().toString());
//                    intent.putExtra("phone", phone.getText().toString());
//                    intent.putExtra("city", city.getText().toString());
//                    intent.putExtra("email", tv_email.getText().toString());
//                    startActivity(intent);
                } else {
                    error_message.setText("Please Enter Valid Details");
                    error_message.setVisibility(View.VISIBLE);
                    error_message_1.setText("Please Enter Valid Details");
                    error_message_1.setVisibility(View.VISIBLE);
                }
            }
        });

        apply_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                if(promocode_edit_tv.getText().toString().isEmpty()) {
                    Toast.makeText(PaymentGateway.this, getString(R.string.enter_promocode) , Toast.LENGTH_SHORT).show();
                } else {

                    if(!promoCodes.isEmpty()) {
                        /*
                        Check if the user has already applied this promo-code
                         */
                        for(PromoCode promoCode: promoCodes) {
                            if(promoCode.getCode_name().trim().equalsIgnoreCase(promocode_edit_tv.getText().toString())) {
                                Toast.makeText(PaymentGateway.this, getString(R.string.promocode_already_applied) , Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }


                    }
                    snackbar = Snackbar.make(linearLayout, getString(R.string.promocode_checking), Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();

                    final String promocode =promocode_edit_tv.getText().toString();
                    RequestQueue requestQueue = Volley.newRequestQueue(PaymentGateway.this);
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
                                            Toast.makeText(PaymentGateway.this, getString(R.string.promocode_invalid) , Toast.LENGTH_SHORT).show();
                                            return;
                                        } else {
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            promoCode = new PromoCode(jsonObject.getString("code_id"),
                                                    jsonObject.getString("user_id"),
                                                    jsonObject.getString("code_name"),
                                                    jsonObject.getString("discount_amount"),
                                                    jsonObject.getString("valid_status"),
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

                                            /*
                                               Check if the promo-code's date is still valid
                                            */
                                            if(!checkPromocodeDate(promoCode.getFrom_date(), promoCode.getTo_date())){
                                                return;
                                            }

                                            /*
                                                Check if the promo code is available for current product
                                             */
                                            if(orderedCourses.get(j).getCourse_id().equals(promoCode.getProduct_id())) {
                                                promoCodes.add(promoCode);
                                                Toast.makeText(PaymentGateway.this, "Promocode applied to " + orderedCourses.get(j).getName(), Toast.LENGTH_SHORT).show();
                                                isApplied = true;
                                                cart_productsAdapter.changePrice(j, promoCode.getDiscount_amount());
                                                grand_total -= Double.valueOf(promoCode.getDiscount_amount());

                                                sub_total_price.setText("₹ "+grand_total+"");

                                                gst_price = grand_total + Math.round(grand_total*0.18);

                                                gst_price_tv.setText(String.format("₹ %d",  Math.round(grand_total*0.18)));

                                                tv_price.setText(String.format("₹ %d", gst_price)+"");
                                                break;
                                            }
                                        }
                                        if(!isApplied) {
                                            Toast.makeText(PaymentGateway.this, getString(R.string.promocode_invalid) , Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(PaymentGateway.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );

                    requestQueue.add(stringRequest);
                }

            }
        });
    }

    private boolean checkPromocodeDate(String startDateString, String endDateString){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try{
            Date startDate = format.parse(startDateString);
            Date endDate = format.parse(endDateString);
            Date currentDate = new Date();

            if(!(currentDate.after(startDate) && currentDate.before(endDate))){
                Log.d("PaymentGateway", "Promocode date not valid!");
                Toast.makeText(PaymentGateway.this, getResources().getText(R.string.promocode_date_expired), Toast.LENGTH_SHORT).show();
            } else return true;

        } catch (ParseException e){
            Toast.makeText(PaymentGateway.this, getResources().getText(R.string.promocode_date_parse_failed), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onItemClick1(int position, String type) {

        if (type.equals("remove")) {
            cart_productsAdapter.remove(position);
            arrayList.remove(position);

            Paper.book().write("cart1", new Gson().toJson(arrayList));

            Toast.makeText(this,  getString(R.string.course_deleted), Toast.LENGTH_SHORT).show();

            grand_total = 0;

            for (OrderedCourse orderedCourse : cart_productsAdapter.getOrderedCourses()) {

                grand_total += Float.valueOf(orderedCourse.getPrice());

            }

            sub_total_price.setText("₹ "+ grand_total+"");

            gst_price = grand_total + Math.round(grand_total*0.18);

            gst_price_tv.setText(String.format("₹ %d",  Math.round(grand_total*0.18)));

            tv_price.setText(String.format("₹ %d", gst_price)+"");

            if(grand_total == 0) {
                startActivity(new Intent(PaymentGateway.this, MyCourses.class));
                finish();
            }

        }
    }

    private void courseCheckout() {
        RequestQueue requestQueue = Volley.newRequestQueue(PaymentGateway.this);
        String url = "https://careeranna.com/websiteapi/pdfCheck";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(PaymentGateway.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PaymentGateway.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url


                ids = "";
                price1 = "";
                discount = "";

                for(OrderedCourse orderedCourse: cart_productsAdapter.getOrderedCourses()) {
                    ids += orderedCourse.getCourse_id() + ",";

                    price1 += orderedCourse.getOld_price() + ",";

                    if(orderedCourse.getPrice().equals(orderedCourse.getOld_price())) {
                        discount += 0 + ",";
                    } else {
                        discount += orderedCourse.getPrice() + ",";
                    }
                }

                Map<String, String> params = new HashMap<String, String>();
                params.put("user", user.getUser_id());
                params.put("product_ids", ids);
                params.put("name", name.getText().toString());
                params.put("city", city.getText().toString());
                params.put("email", tv_email.getText().toString());
                params.put("prices", price1);
                params.put("discount_price", discount);
                params.put("sub_total", grand_total+"");
                params.put("amount", gst_price+"");
                params.put("gst", (grand_total*0.18)+"");
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }
}
