package com.careeranna.careeranna;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.payuui.Activity.PayUBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;

public class Payment extends AppCompatActivity {

    private String merchantKey, userCredentials;

    private PaymentParams paymentParams;

    private PayuConfig payuConfig;

    private PayUChecksum checksum;

    private float price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        price = getIntent().getFloatExtra("price", 0);

        Payu.setInstance(this);

        navigationToBaseActivity();
    }

    public void navigationToBaseActivity() {

        merchantKey = "gtKFFx";
        int environment = PayuConstants.STAGING_ENV;
        String amount = String.valueOf(price);
        String email = "giteshshastri96@gmail.com";
        userCredentials = merchantKey + ":" + email;

        paymentParams = new PaymentParams();

        paymentParams.setKey(merchantKey);
        paymentParams.setAmount(amount);
        paymentParams.setProductInfo("course");
        paymentParams.setFirstName("Gitesh");
        paymentParams.setEmail(email);


        paymentParams.setTxnId(""+System.currentTimeMillis());

        paymentParams.setSurl("https://payu.herokuapp.com/success");
        paymentParams.setSurl("https://payu.herokuapp.com/failure");

        paymentParams.setUdf1("ufid1");
        paymentParams.setUdf2("ufid2");
        paymentParams.setUdf3("ufid3");
        paymentParams.setUdf4("ufid4");
        paymentParams.setUdf5("ufid5");

        paymentParams.setUserCredentials(userCredentials);

        payuConfig = new PayuConfig();
        payuConfig.setEnvironment(environment);

        generateHashFromServer(paymentParams);
    }

    public void generateHashFromServer(PaymentParams mPaymentParams) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayuConstants.KEY, mPaymentParams.getKey()));
        postParamsBuffer.append(concatParams(PayuConstants.AMOUNT, mPaymentParams.getAmount()));
        postParamsBuffer.append(concatParams(PayuConstants.TXNID, mPaymentParams.getTxnId()));
        postParamsBuffer.append(concatParams(PayuConstants.EMAIL, null == mPaymentParams.getEmail() ? "" : mPaymentParams.getEmail()));
        postParamsBuffer.append(concatParams(PayuConstants.PRODUCT_INFO, mPaymentParams.getProductInfo()));
        postParamsBuffer.append(concatParams(PayuConstants.FIRST_NAME, null == mPaymentParams.getFirstName() ? "" : mPaymentParams.getFirstName()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF1, mPaymentParams.getUdf1() == null ? "" : mPaymentParams.getUdf1()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF2, mPaymentParams.getUdf2() == null ? "" : mPaymentParams.getUdf2()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF3, mPaymentParams.getUdf3() == null ? "" : mPaymentParams.getUdf3()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF4, mPaymentParams.getUdf4() == null ? "" : mPaymentParams.getUdf4()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF5, mPaymentParams.getUdf5() == null ? "" : mPaymentParams.getUdf5()));
        postParamsBuffer.append(concatParams(PayuConstants.USER_CREDENTIALS, mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials()));

        // for offer_key
        if (null != mPaymentParams.getOfferKey())
            postParamsBuffer.append(concatParams(PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey()));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();

        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }

    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    private class GetHashesFromServerTask extends AsyncTask<String, String, PayuHashes> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Payment.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected PayuHashes doInBackground(String... postParams) {
            PayuHashes payuHashes = new PayuHashes();
            try {

                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                //   URL url = new URL("https://tsd.payu.in/GetHash");
                URL url = new URL("https://payu.herokuapp.com/get_hash");

                // get the payuConfig first
                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        //TODO Below three hashes are mandatory for payment flow and needs to be generated at merchant server
                        /**
                         * Payment hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_hash -
                         *
                         * sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||SALT)
                         *
                         */
                        case "payment_hash":
                            payuHashes.setPaymentHash(response.getString(key));
                            break;
                        /**
                         * vas_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating vas_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be "default"
                         *
                         */
                        case "vas_for_mobile_sdk_hash":
                            payuHashes.setVasForMobileSdkHash(response.getString(key));
                            break;
                        /**
                         * payment_related_details_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_related_details_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "payment_related_details_for_mobile_sdk_hash":
                            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(response.getString(key));
                            break;

                        //TODO Below hashes only needs to be generated if you are using Store card feature
                        /**
                         * delete_user_card_hash is used while deleting a stored card.
                         * Below is formula for generating delete_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "delete_user_card_hash":
                            payuHashes.setDeleteCardHash(response.getString(key));
                            break;
                        /**
                         * get_user_cards_hash is used while fetching all the cards corresponding to a user.
                         * Below is formula for generating get_user_cards_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "get_user_cards_hash":
                            payuHashes.setStoredCardsHash(response.getString(key));
                            break;
                        /**
                         * edit_user_card_hash is used while editing details of existing stored card.
                         * Below is formula for generating edit_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "edit_user_card_hash":
                            payuHashes.setEditCardHash(response.getString(key));
                            break;
                        /**
                         * save_user_card_hash is used while saving card to the vault
                         * Below is formula for generating save_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "save_user_card_hash":
                            payuHashes.setSaveCardHash(response.getString(key));
                            break;

                        //TODO This hash needs to be generated if you are using any offer key
                        /**
                         * check_offer_status_hash is used while using check_offer_status api
                         * Below is formula for generating check_offer_status_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be Offer Key.
                         *
                         */
                        case "check_offer_status_hash":
                            payuHashes.setCheckOfferStatusHash(response.getString(key));
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return payuHashes;
        }

        @Override
        protected void onPostExecute(PayuHashes payuHashes) {
            super.onPostExecute(payuHashes);

            progressDialog.dismiss();
            launchSdkUI(payuHashes);
        }
    }

    /**
     * This method adds the Payuhashes and other required params to intent and launches the PayuBaseActivity.java
     *
     * @param payuHashes it contains all the hashes generated from merchant server
     */
    public void launchSdkUI(PayuHashes payuHashes) {

        Intent intent = new Intent(this, PayUBaseActivity.class);
        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
        intent.putExtra(PayuConstants.PAYMENT_PARAMS, paymentParams);
        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);

        startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (data != null) {

                /**
                 * Here, data.getStringExtra("payu_response") ---> Implicit response sent by PayU
                 * data.getStringExtra("result") ---> Response received from merchant's Surl/Furl
                 *
                 * PayU sends the same response to merchant server and in app. In response check the value of key "status"
                 * for identifying status of transaction. There are two possible status like, success or failure
                 * */
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Payu's Data : " + data.getStringExtra("payu_response") + "\n\n\n Merchant's Data: " + data.getStringExtra("result"))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).show();

            } else {
                Toast.makeText(this, getString(R.string.could_not_receive_data), Toast.LENGTH_LONG).show();
            }
        }
    }


}
