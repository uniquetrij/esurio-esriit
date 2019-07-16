package com.infy.esurio.esuriit.activities.main.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.infy.esurio.R;
import com.infy.esurio.esuriit.app.services.GooglePayService;
import com.infy.esurio.esuriit.data.model.payTM.ApiClient;
import com.infy.esurio.esuriit.data.model.payTM.ApiService;
import com.infy.esurio.esuriit.data.model.payTM.AppConfig;
import com.infy.esurio.esuriit.data.model.payTM.AppDatabase;
import com.infy.esurio.esuriit.data.model.payTM.BuildConfig;
import com.infy.esurio.esuriit.data.model.payTM.CartItem;
import com.infy.esurio.esuriit.data.model.payTM.ChecksumResponse;
import com.infy.esurio.esuriit.data.model.payTM.Constants;
import com.infy.esurio.esuriit.data.model.payTM.Order;
import com.infy.esurio.esuriit.data.model.payTM.OrderItem;
import com.infy.esurio.esuriit.data.model.payTM.PrepareOrderRequest;
import com.infy.esurio.esuriit.data.model.payTM.PrepareOrderResponse;
import com.infy.esurio.esuriit.data.model.payTM.User;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class GooglePayFragment extends Fragment {

    /**
     * A constant integer you define to track a request for payment data activity
     */
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 42;
    View view;
    /**
     * A client for interacting with the Google Pay API
     *
     * @see <a
     * href="https://developers.google.com/android/reference/com/google/android/gms/wallet/PaymentsClient">PaymentsClient</a>
     */
    private PaymentsClient mPaymentsClient;
    /**
     * A Google Pay payment button presented to the viewer for interaction
     *
     * @see <a href="https://developers.google.com/pay/api/android/guides/brand-guidelines">Google Pay
     * payment button brand guidelines</a>
     */
    private View mGooglePayButton;

    public GooglePayFragment() {
        // Required empty public constructor
    }

    private static ApiClient mApi;
    private Realm realm;
    private AppConfig appConfig;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_google_pay, container, false);

        // getPaymentsClient can use context or activity
        mPaymentsClient =
                Wallet.getPaymentsClient(getActivity(),
                        new Wallet.WalletOptions.Builder()
                                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                                .build());

        possiblyShowGooglePayButton();
        Button payTm = view.findViewById(R.id.ptmPay);
//        payTm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pressPayTm();
//            }
//        });
        payTm.setOnClickListener(v -> pressPayTm());
        return view;
    }

    public void pressPayTm() {
        realm = Realm.getDefaultInstance();
        realm.where(CartItem.class).findAllAsync()
                .addChangeListener(cartItems -> {

                });

        user = AppDatabase.getUser();
        appConfig = realm.where(AppConfig.class).findFirst();


        /**
         * Steps to process order:
         * 1. Make server call to prepare the order. Which will create a new order in the db
         * and returns the unique Order ID
         * <p>
         * 2. Once the order ID is received, send the PayTM params to server to calculate the
         * Checksum Hash
         * <p>
         * 3. Send the PayTM params along with checksum hash to PayTM gateway
         * <p>
         * 4. Once the payment is done, send the Order Id back to server to verify the
         * transaction status
         */

        /**
         * STEP 1: Sending all the cart items to server and receiving the
         * unique order id that needs to be sent to PayTM
         */
        prepareOrder();
    }

    private void prepareOrder() {
        List<CartItem> cartItems = realm.where(CartItem.class).findAll();
        PrepareOrderRequest request = new PrepareOrderRequest();
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.productId = cartItem.product.id;
            orderItem.quantity = cartItem.quantity;
            orderItems.add(orderItem);
        }
        request.orderItems = orderItems;

        getApi().prepareOrder(request).enqueue(new Callback<PrepareOrderResponse>() {
            @Override
            public void onResponse(Call<PrepareOrderResponse> call, Response<PrepareOrderResponse> response) {
                if (!response.isSuccessful()) {
                    handleUnknownError();
                    return;
                }

                getChecksum(response.body());
            }

            @Override
            public void onFailure(Call<PrepareOrderResponse> call, Throwable t) {
                handleError(t);
            }
        });
    }

    public void handleUnknownError() {
    }

    public void handleError(Throwable throwable) {
    }

    private ApiClient getApi() {
        if (mApi == null) {
            mApi = ApiService.getClient().create(ApiClient.class);
        }
        return mApi;
    }


    /**
     * STEP 2:
     * Sending the params to server to generate the Checksum
     * that needs to be sent to PayTM
     */
    private void getChecksum(PrepareOrderResponse response) {
        if (appConfig == null) {
            Timber.e("App config is null! Can't place the order. This usually shouldn\'t happen");
            // navigating user to login screen
//            launchLogin(PayTMActivity.this);
//            finish();
            return;
        }

        HashMap<String, String> paramMap = preparePayTmParams(response);
        Timber.d("PayTm Params: %s", paramMap);
        getApi().getCheckSum(paramMap).enqueue(new Callback<ChecksumResponse>() {
            @Override
            public void onResponse(Call<ChecksumResponse> call, Response<ChecksumResponse> response) {
                if (!response.isSuccessful()) {
                    Timber.e("Network call failed");
                    handleUnknownError();
                    return;
                }

                Timber.d("Checksum Received: " + response.body().checksum);

                // Add the checksum to existing params list and send them to PayTM
                paramMap.put("CHECKSUMHASH", response.body().checksum);
                placeOrder(paramMap);
            }

            @Override
            public void onFailure(Call<ChecksumResponse> call, Throwable t) {
                handleError(t);
            }
        });
    }

    public HashMap<String, String> preparePayTmParams(PrepareOrderResponse response) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("CALLBACK_URL", String.format(BuildConfig.PAYTM_CALLBACK_URL, response.orderGatewayId));
        paramMap.put("CHANNEL_ID", appConfig.getChannel());
        paramMap.put("CUST_ID", "CUSTOMER_" + user.id);
        paramMap.put("INDUSTRY_TYPE_ID", appConfig.getIndustryType());
        paramMap.put("MID", appConfig.getMerchantId());
        paramMap.put("WEBSITE", appConfig.getWebsite());
        paramMap.put("ORDER_ID", response.orderGatewayId);
        paramMap.put("TXN_AMOUNT", response.amount);
        return paramMap;
    }

    /**
     * STEP 3: Redirecting to PayTM gateway with necessary params along with checksum
     * This will redirect to PayTM gateway and gives us the PayTM transaction response
     */
    private void placeOrder(HashMap<String, String> params) {
        PaytmPGService pgService = BuildConfig.IS_PATM_STAGIN ? PaytmPGService.getStagingService() : PaytmPGService.getProductionService();

        PaytmOrder Order = new PaytmOrder(params);

        pgService.initialize(Order, null);

        pgService.startPaymentTransaction(getContext(), true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        Timber.e("someUIErrorOccurred: %s", inErrorMessage);

                        getActivity().finish();
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Timber.d("PayTM Transaction Response: %s", inResponse.toString());
                        String orderId = inResponse.getString("ORDERID");
                        verifyTransactionStatus(orderId);
                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        Timber.e("networkNotAvailable");
                        getActivity().finish();
                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        Timber.e("clientAuthenticationFailed: %s", inErrorMessage);
                        getActivity().finish();
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {
                        Timber.e("onErrorLoadingWebPage: %d | %s | %s", iniErrorCode, inErrorMessage, inFailingUrl);
                        getActivity().finish();
                    }

                    @Override
                    public void onBackPressedCancelTransaction() {
                        //Toast.makeText(PayTMActivity.this, "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Timber.e("onTransactionCancel: %s | %s", inErrorMessage, inResponse);
                        getActivity().finish();
                    }
                });
    }

    /**
     * STEP 4: Verifying the transaction status once PayTM transaction is over
     * This makes server(own) -> server(PayTM) call to verify the transaction status
     */
    private void verifyTransactionStatus(String orderId) {
        getApi().checkTransactionStatus(orderId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (!response.isSuccessful()) {
                    Timber.e("Network call failed");
                    handleUnknownError();
                    return;
                }

                showOrderStatus(response.body().status.equalsIgnoreCase(Constants.ORDER_STATUS_COMPLETED));
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                handleError(t);
                showOrderStatus(false);
            }
        });
    }

    /*
     * Displaying Order Status on UI. This toggles UI between success and failed cases
     * */
    private void showOrderStatus(boolean isSuccess) {

    }






    private void possiblyShowGooglePayButton() {
        final Optional<JSONObject> isReadyToPayJson = GooglePayService.getIsReadyToPayRequest();
        if (!isReadyToPayJson.isPresent()) {
            return;
        }
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
        if (request == null) {
            return;
        }
        Task<Boolean> task = mPaymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(
                new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        try {
                            boolean result = task.getResult(ApiException.class);
                            if (result) {
                                // show Google as a payment option
                                mGooglePayButton = view.findViewById(R.id.googlepay);
                                mGooglePayButton.setOnClickListener(
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                requestPayment(view);
                                            }
                                        });
                                mGooglePayButton.setVisibility(View.VISIBLE);
                            }
                        } catch (ApiException exception) {
                            // handle developer errors
                        }
                    }
                });

    }

    /**
     * Display the Google Pay payment sheet after interaction with the Google Pay payment button
     *
     * @param view optionally uniquely identify the interactive element prompting for payment
     */
    public void requestPayment(View view) {
        Optional<JSONObject> paymentDataRequestJson = GooglePayService.getPaymentDataRequest();
        if (!paymentDataRequestJson.isPresent()) {
            return;
        }
        PaymentDataRequest request =
                PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());


        // Require Activity, try use getActivity()
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    mPaymentsClient.loadPaymentData(request), getActivity(), LOAD_PAYMENT_DATA_REQUEST_CODE);
        }
    }

    /**
     * Handle a resolved activity from the Google Pay payment sheet
     *
     * @param requestCode the request code originally supplied to AutoResolveHelper in
     *                    requestPayment()
     * @param resultCode  the result code returned by the Google Pay API
     * @param data        an Intent from the Google Pay API containing payment or error data
     * @see <a href="https://developer.android.com/training/basics/intents/result">Getting a result
     * from an Activity</a>
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // value passed in AutoResolveHelper
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        String json = Objects.requireNonNull(paymentData).toJson();
                        // if using gateway tokenization, pass this token without modification

                        // In original code paymentMethodData is String. But there is error so I change to JSONObject
                        JSONObject paymentMethodData = null;
                        try {
                            paymentMethodData = new JSONObject(json)
                                    .getJSONObject("paymentMethodData");
                            String paymentToken = paymentMethodData
                                    .getJSONObject("tokenizationData")
                                    .getString("token");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        // Log the status for debugging.
                        // Generally, there is no need to show an error to the user.
                        // The Google Pay payment sheet will present any account errors.
                        break;
                    default:
                        // Do nothing.
                }
                break;
            default:
                // Do nothing.
        }
    }


}
