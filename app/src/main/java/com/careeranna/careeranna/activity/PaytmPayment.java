package com.careeranna.careeranna.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Constants;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.OrderedCourse;
import com.careeranna.careeranna.data.User;
import com.careeranna.careeranna.helper.Api;
import com.careeranna.careeranna.helper.CheckSum;
import com.careeranna.careeranna.helper.Paytm;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaytmPayment extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    User user;

    ProgressBar progressBar;

    TextView please_wait_tv;
    ArrayList<String> arrayList;
    private ArrayList<OrderedCourse> orderedCourses;

    String ids = "";
    String price = "";
    String discounted_price = "";
    Course course;
    float grand_total = 0;
    int price1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_payment);

        Paper.init(this);

        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            user = new Gson().fromJson(cache, User.class);
        }

        String cart = Paper.book().read("cart1");

        ArrayList<OrderedCourse> object = (ArrayList<OrderedCourse>) getIntent().getSerializableExtra("OrderedCourses");

        for(OrderedCourse orderedCourse: object) {
            ids += orderedCourse.getCourse_id() + ",";
            price += orderedCourse.getOld_price() + ",";
            discounted_price += orderedCourse.getPrice() + ",";
            grand_total += Float.valueOf(orderedCourse.getPrice());
        }

        price1 = Integer.valueOf(getIntent().getStringExtra("gst_price"));

        progressBar = findViewById(R.id.progress);
        please_wait_tv = findViewById(R.id.please_wait_tv);

        generateCheckSum();

     //   courseCheckout(new Bundle());

    }

    private void generateCheckSum() {

        //getting the tax amount first.
        String txnAmount = "";

        if (getIntent().hasExtra("gst_price")) {
            txnAmount = getIntent().getStringExtra("gst_price");
        }

        //creating a retrofit object.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //creating the retrofit api service
        Api apiService = retrofit.create(Api.class);

        //creating paytm object
        //containing all the values required
        final Paytm paytm = new Paytm(
                Constants.M_ID,
                Constants.CHANNEL_ID,
                txnAmount,
                Constants.WEBSITE,
                Constants.CALLBACK_URL,
                Constants.INDUSTRY_TYPE_ID
        );

        //creating a call object from the apiService
        Call<CheckSum> call = apiService.getChecksum(
                paytm.getmId(),
                paytm.getOrderId(),
                paytm.getCustId(),
                paytm.getChannelId(),
                paytm.getTxnAmount(),
                paytm.getWebsite(),
                paytm.getCallBackUrl(),
                paytm.getIndustryTypeId()
        );

        //making the call to generate checksum
        call.enqueue(new Callback<CheckSum>() {
            @Override
            public void onResponse(Call<CheckSum> call, Response<CheckSum> response) {

                initializePaytmPayment(response.body().getChecksumHash(), paytm);
            }

            @Override
            public void onFailure(Call<CheckSum> call, Throwable t) {

            }
        });

    }

    private void initializePaytmPayment(String checksumHash, Paytm paytm) {

        //getting paytm service
        PaytmPGService Service = PaytmPGService.getProductionService();

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constants.M_ID);
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder((HashMap<String, String>) paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(this, true, true, this);

    }

    @Override
    public void onTransactionResponse(Bundle inResponse) {
        String response = "Bundle[{STATUS=TXN_SUCCESS, CHECKSUMHASH=W1qF7coHHR61tZxvtXaBUAsUbKeHiNX3L/M9Z7gSQd957/kRYWlqI4zxCAs0SIMsAMSBKB2qh03s+biJzCQfICPbHfgHY2W1lMJnYN9kuIs=, BANKNAME=WALLET, ORDERID=b47598b8d2fa493e8b453471846e46d1, TXNAMOUNT=1.00, TXNDATE=2019-06-02 19:11:40.0, MID=tweSrj29619213274989, TXNID=20190602111212800110168393272572844, RESPCODE=01, PAYMENTMODE=PPI, BANKTXNID=114240516328, CURRENCY=INR, GATEWAYNAME=WALLET, RESPMSG=Txn Success}]";
        if(inResponse.containsKey("STATUS")) {
            if(inResponse.getString("STATUS").equals("TXN_SUCCESS")) {
                courseCheckout(inResponse, inResponse.getString("TXNID"));
            } else {
                Toast.makeText(this, "Transaction Error", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        }
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {
        Toast.makeText(this, "Clirnt Side" + inErrorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {
        Toast.makeText(this, "UI" + inErrorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
        Toast.makeText(this, "Loading Page" + inErrorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(this, "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
        Toast.makeText(this, "Cancel" + inErrorMessage + inResponse.toString(), Toast.LENGTH_LONG).show();
    }

    private void courseCheckout(final Bundle inResponse, final String orderid) {
        progressBar.setVisibility(View.VISIBLE);
        please_wait_tv.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(PaytmPayment.this);
        String url = "https://careeranna.com/websiteapi/pdfCheckCareer";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        Toast.makeText(PaytmPayment.this, response, Toast.LENGTH_SHORT).show();
                        Paper.book().delete("cart1");
                        startActivity(new Intent(PaytmPayment.this, MyCourses.class).putExtra("fragment_name", "my_course"));
                        progressBar.setVisibility(View.INVISIBLE);
                        please_wait_tv.setVisibility(View.GONE);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        please_wait_tv.setVisibility(View.GONE);
                        if (error == null || error.networkResponse == null) {
                            return;
                        }
                        String body;
                        final String statusCode = String.valueOf(error.networkResponse.statusCode);
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                            Log.e("error_message", body);
                        } catch (UnsupportedEncodingException e) {
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("user", user.getUser_id());
                params.put("product_ids", ids);
                params.put("name", getIntent().getStringExtra("name"));
                params.put("city", getIntent().getStringExtra("city"));
                params.put("email", getIntent().getStringExtra("email"));
                params.put("phone", getIntent().getStringExtra("phone"));
                params.put("ids", getIntent().getStringExtra("ids"));
                params.put("payment_response", inResponse.toString());
                params.put("order_id", orderid+"");
                params.put("prices", getIntent().getStringExtra("product_prices"));
                params.put("discount_price", getIntent().getStringExtra("discount_prices"));
                params.put("sub_total", getIntent().getStringExtra("grand_total")+"");
                params.put("amount", price1+"");
                params.put("gst", getIntent().getStringExtra("gst_total")+"");
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }
}
