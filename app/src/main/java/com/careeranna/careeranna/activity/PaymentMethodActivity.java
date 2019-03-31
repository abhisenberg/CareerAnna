package com.careeranna.careeranna.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.OrderedCourse;

import java.util.ArrayList;

public class PaymentMethodActivity extends AppCompatActivity {

    RelativeLayout paytm, payu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        paytm = findViewById(R.id.paytm_linear_layout);
        payu = findViewById(R.id.payu_biz_linear_layout);

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(PaymentMethodActivity.this, PaytmPayment.class);
                    intent.putExtra("grand_total", getIntent().getStringExtra("grand_total"));
                    intent.putExtra("gst_total",getIntent().getStringExtra("gst_total") );
                    intent.putExtra("gst_price", getIntent().getStringExtra("gst_price"));
                    intent.putExtra("name", getIntent().getStringExtra("name"));
                    intent.putExtra("phone", getIntent().getStringExtra("phone"));
                    intent.putExtra("city", getIntent().getStringExtra("city"));
                    intent.putExtra("email", getIntent().getStringExtra("email"));
                intent.putExtra("ids", getIntent().getStringExtra("ids"));
                intent.putExtra("product_prices", getIntent().getStringExtra("product_prices"));
                intent.putExtra("discount_prices", getIntent().getStringExtra("discount_prices"));
                    intent.putExtra("OrderedCourses", (ArrayList<OrderedCourse>)getIntent().getSerializableExtra("OrderedCourses"));
                    startActivity(intent);
            }
        });

        payu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentMethodActivity.this, Payment.class);
                intent.putExtra("grand_total", getIntent().getStringExtra("grand_total"));
                intent.putExtra("gst_total",getIntent().getStringExtra("gst_total") );
                intent.putExtra("gst_price", getIntent().getStringExtra("gst_price"));
                intent.putExtra("name", getIntent().getStringExtra("name"));
                intent.putExtra("phone", getIntent().getStringExtra("phone"));
                intent.putExtra("city", getIntent().getStringExtra("city"));
                intent.putExtra("email", getIntent().getStringExtra("email"));
                intent.putExtra("ids", getIntent().getStringExtra("ids"));
                intent.putExtra("product_prices", getIntent().getStringExtra("product_prices"));
                intent.putExtra("discount_prices", getIntent().getStringExtra("discount_prices"));
                intent.putExtra("OrderedCourses", (ArrayList<OrderedCourse>)getIntent().getSerializableExtra("OrderedCourses"));
                startActivity(intent);

            }
        });
    }
}
