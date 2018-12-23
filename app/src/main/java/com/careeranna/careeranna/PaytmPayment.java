package com.careeranna.careeranna;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.careeranna.careeranna.data.Constants;
import com.careeranna.careeranna.helper.Api;
import com.careeranna.careeranna.helper.CheckSum;
import com.careeranna.careeranna.helper.Paytm;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Checksum;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaytmPayment extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_payment);

        generateCheckSum();
    }

    private void generateCheckSum() {

        //getting the tax amount first.
        String txnAmount = "10";

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
        PaytmPGService Service = PaytmPGService.getStagingService();

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
        Toast.makeText(this, "Response" + inResponse.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {
        Toast.makeText(this, "Clirnt Side" +inErrorMessage, Toast.LENGTH_LONG).show();
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
}
