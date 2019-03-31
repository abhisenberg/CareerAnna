package com.payu.payuui.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;
import android.widget.EditText;

import com.payu.custombrowser.Bank;
import com.payu.custombrowser.CustomBrowser;
import com.payu.custombrowser.PackageListDialogFragment;
import com.payu.custombrowser.PayUCustomBrowserCallback;
import com.payu.custombrowser.PayUSurePayWebViewClient;
import com.payu.custombrowser.PayUWebChromeClient;
import com.payu.custombrowser.PayUWebViewClient;
import com.payu.custombrowser.bean.CustomBrowserConfig;
//import com.payu.custombrowser.upiintent.Payment;
import com.payu.custombrowser.util.PaymentOption;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.magicretry.MagicRetryFragment;
import com.payu.payuui.R;
import com.payu.phonepe.PhonePe;
import com.payu.phonepe.callbacks.PayUPhonePeCallback;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class PaymentsActivity extends FragmentActivity {
    private Bundle bundle;
    private String url;
    private PayuConfig payuConfig;
    private boolean isStandAlonePhonePayAvailable;
    private boolean isPaymentByPhonePe;
    private String UTF = "UTF-8";
    private boolean viewPortWide = false;
    private String merchantHash;
    private String txnId = null;
    private String merchantKey;
    private PayUChecksum checksum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            bundle = getIntent().getExtras();

            PhonePe phonePe = PhonePe.getInstance();


            if (bundle != null) {

                isStandAlonePhonePayAvailable = bundle.getBoolean("isStandAlonePhonePeAvailable", false);
                isPaymentByPhonePe = bundle.getBoolean("isPaymentByPhonePe", false);
            }


            if (bundle != null)
                payuConfig = bundle.getParcelable(PayuConstants.PAYU_CONFIG);

            if (payuConfig != null) {
                url = payuConfig.getEnvironment() == PayuConstants.PRODUCTION_ENV ? PayuConstants.PRODUCTION_PAYMENT_URL : PayuConstants.TEST_PAYMENT_URL;
              //  url="https://mobiletest.payu.in/_payment";
                String[] list=null;
                if(payuConfig.getData()!=null)
                list = payuConfig.getData().split("&");

                if(list != null) {
                    for (String item : list) {
                        String[] items = item.split("=");
                        if (items.length >= 2) {
                            String id = items[0];
                            switch (id) {
                                case "txnid":
                                    txnId = items[1];
                                    break;
                                case "key":
                                    merchantKey = items[1];
                                    break;
                                case "pg":
                                    if (items[1].contentEquals("NB")) {
                                        viewPortWide = true;
                                    }
                                    break;

                            }
                        }
                    }
                }


                PayUPhonePeCallback payUPhonePeCallback = new PayUPhonePeCallback() {

                    // Called when Payment gets Successful
                    @Override
                    public void onPaymentOptionSuccess(String payuResponse) {

                        Intent intent = new Intent();
                        intent.putExtra(getString(R.string.cb_payu_response), payuResponse);
                        setResult(PayuConstants.PAYU_REQUEST_CODE,intent);
                        finish();

                    }


                    // Called when Payment is failed
                    @Override
                    public void onPaymentOptionFailure(String payuResponse) {
                        Intent intent = new Intent();
                        intent.putExtra(getString(R.string.cb_payu_response), payuResponse);
                        setResult(PayuConstants.PAYU_REQUEST_CODE,intent);
                        finish();
                    }
                };

                //set callback to track important events
                PayUCustomBrowserCallback payUCustomBrowserCallback = new PayUCustomBrowserCallback() {



                    public void onVpaEntered(String vpa, PackageListDialogFragment packageListDialogFragment) {

                        //This hash should be generated from server

                        String input = merchantKey+"|validateVPA|"+vpa+"|1b1b0";

                         String verifyVpaHash = calculateHash(input.toString()).getResult();

                        packageListDialogFragment.verifyVpa(verifyVpaHash);


                    }

                    private PostData calculateHash(String hashString) {
                        try {
                            StringBuilder hash = new StringBuilder();
                            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
                            messageDigest.update(hashString.getBytes());
                            byte[] mdbytes = messageDigest.digest();
                            for (byte hashByte : mdbytes) {
                                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
                            }

                            return getReturnData(PayuErrors.NO_ERROR, PayuConstants.SUCCESS, hash.toString());
                        } catch (NoSuchAlgorithmException e) {
                            return getReturnData(PayuErrors.NO_SUCH_ALGORITHM_EXCEPTION, PayuErrors.INVALID_ALGORITHM_SHA);
                        }
                    }

                    protected PostData getReturnData(int code, String status, String result) {
                        PostData postData = new PostData();
                        postData.setCode(code);
                        postData.setStatus(status);
                        postData.setResult(result);
                        return postData;
                    }

                    protected PostData getReturnData(int code, String result) {
                        return getReturnData(code, PayuConstants.ERROR, result);
                    }

                    /**
                     * This method will be called after a failed transaction.
                     *
                     * @param payuResponse     response sent by PayU in App
                     * @param merchantResponse response received from Furl
                     */
                    @Override
                    public void onPaymentFailure(String payuResponse, String merchantResponse) {
                        Intent intent = new Intent();
                        intent.putExtra(getString(R.string.cb_result), merchantResponse);
                        intent.putExtra(getString(R.string.cb_payu_response), payuResponse);
                        if (null != merchantHash) {
                            intent.putExtra(PayuConstants.MERCHANT_HASH, merchantHash);
                        }
                        setResult(Activity.RESULT_CANCELED, intent);
                        finish();
                    }

                    @Override
                    public void onPaymentTerminate() {
                        finish();
                    }

                    /**
                     * This method will be called after a successful transaction.
                     *
                     * @param payuResponse     response sent by PayU in App
                     * @param merchantResponse response received from Furl
                     */
                    @Override
                    public void onPaymentSuccess(String payuResponse, String merchantResponse) {
                        Intent intent = new Intent();
                        intent.putExtra(getString(R.string.cb_result), merchantResponse);
                        intent.putExtra(getString(R.string.cb_payu_response), payuResponse);
                        if (null != merchantHash) {
                            intent.putExtra(PayuConstants.MERCHANT_HASH, merchantHash);
                        }
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onCBErrorReceived(int code, String errormsg) {
                    }

                    @Override
                    public void setCBProperties(WebView webview, Bank payUCustomBrowser) {
                        webview.setWebChromeClient(new PayUWebChromeClient(payUCustomBrowser));

                        // The following setting is optional, set WV client only when using your custom WVclient
                        // Also, custom WV client should inherit PayUSurePayWebViewClient in case of SurePay enabled,
                        // Otherwise PayUWebViewClient.
                        // webview.setWebViewClient(new PayUWebViewClient(payUCustomBrowser, merchantKey));
                    }

                    @Override
                    public void onBackApprove() {
                        PaymentsActivity.this.finish();
                    }

                    @Override
                    public void onBackDismiss() {
                        super.onBackDismiss();
                    }

                    /**
                     * This callback method will be invoked when setDisableBackButtonDialog is set to true.
                     *
                     * @param alertDialogBuilder a reference of AlertDialog.Builder to customize the dialog
                     */
                    @Override
                    public void onBackButton(AlertDialog.Builder alertDialogBuilder) {
                        super.onBackButton(alertDialogBuilder);
                    }

                    //TODO Below code is used only when magicRetry is set to true in customBrowserConfig
/*                    @Override
                    public void initializeMagicRetry(Bank payUCustomBrowser, WebView webview, MagicRetryFragment magicRetryFragment) {
                        webview.setWebViewClient(new PayUWebViewClient(payUCustomBrowser, magicRetryFragment, merchantKey));
                        Map<String, String> urlList = new HashMap<String, String>();
                        if(payuConfig!=null)
                            urlList.put(url, payuConfig.getData());
                        payUCustomBrowser.setMagicRetry(urlList);
                    }*/
                };

                //Sets the configuration of custom browser
                CustomBrowserConfig customBrowserConfig = new CustomBrowserConfig(merchantKey, txnId);
                customBrowserConfig.setViewPortWideEnable(viewPortWide);

                //TODO don't forgot to set AutoApprove and AutoSelectOTP to true for One Tap payments
                customBrowserConfig.setAutoApprove(false);  // set true to automatically approve the OTP
                customBrowserConfig.setAutoSelectOTP(false); // set true to automatically select the OTP flow

                //Set below flag to true to disable the default alert dialog of Custom Browser and use your custom dialog
                customBrowserConfig.setDisableBackButtonDialog(false);

                //Below flag is used for One Click Payments. It should always be set to CustomBrowserConfig.STOREONECLICKHASH_MODE_SERVER
                customBrowserConfig.setStoreOneClickHash(CustomBrowserConfig.STOREONECLICKHASH_MODE_SERVER);

                //Set it to true to enable run time permission dialog to appear for all Android 6.0 and above devices
                customBrowserConfig.setMerchantSMSPermission(false);

                //Set it to true to enable Magic retry (If MR is enabled SurePay should be disabled and vice-versa)
                customBrowserConfig.setmagicRetry(false);



                //Set it to false if you do not want the transaction with web-collect flow
                //customBrowserConfig.setEnableWebFlow(Payment.TEZ,true);



                /**
                 * Maximum number of times the SurePay dialog box will prompt the user to retry a transaction in case of network failures
                 * Setting the sure pay count to 0, diables the sure pay dialog
                 */
                customBrowserConfig.setEnableSurePay(3);

                //htmlData - HTML string received from PayU webservice using Server to Server call.

               // customBrowserConfig.setHtmlData("");


                //surepayS2Surl - Url on which HTML received from PayU webservice using Server to Server call is hosted.

               // customBrowserConfig.setSurepayS2Surl("");


                /**
                 * set Merchant activity(Absolute path of activity)
                 * By the time CB detects good network, if CBWebview is destroyed, we resume the transaction by passing payment post data to,
                 * this, merchant checkout activity.
                 * */
                customBrowserConfig.setMerchantCheckoutActivityPath("com.payu.testapp.MerchantCheckoutActivity");

                //Set the first url to open in WebView
                customBrowserConfig.setPostURL(url);

               // String postData = "device_type=1&udid=51e15a3e697d56fe&imei=default&key=smsplus&txnid=1547804005142&amount=10&productinfo=product_info&firstname=firstname&email=test@gmail.com&surl=+https%3A%2F%2Fpayuresponse.firebaseapp.com%2Fsuccess&furl=https%3A%2F%2Fpayuresponse.firebaseapp.com%2Ffailure&hash=a7e524ef46e320c4b5a67e23f6d22a3709eefb9fd9801cebf7ea94e273b6c5d15cafffdebda58a8176fcbb81868f0acddf277cb3214f55b3565a21662dd6a510&udf1=udf1&udf2=udf2&udf3=udf3&udf4=udf4&udf5=udf5&phone=&bankcode=INTENT&pg=upi";




                if (payuConfig!=null)
                customBrowserConfig.setPayuPostData(payuConfig.getData());

                if (isPaymentByPhonePe == true & isStandAlonePhonePayAvailable == true) {

                    phonePe.makePayment(payUPhonePeCallback, PaymentsActivity.this, payuConfig.getData());

                } else {

                    new CustomBrowser().addCustomBrowser(PaymentsActivity.this, customBrowserConfig, payUCustomBrowserCallback);
                }


            }
        }
    }
}
