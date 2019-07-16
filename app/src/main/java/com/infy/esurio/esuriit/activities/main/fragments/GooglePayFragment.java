package com.infy.esurio.esuriit.activities.main.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Optional;

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

        return view;
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
