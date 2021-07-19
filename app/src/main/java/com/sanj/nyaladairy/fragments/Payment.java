package com.sanj.nyaladairy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.sanj.nyaladairy.R;
import com.sanj.nyaladairy.data.URLs;
import com.sanj.nyaladairy.wrapper.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Payment extends Fragment {
    private TextInputEditText edIdNo, edYear;
    private Spinner spinnerMonths;
    private Context mContext;
    private FragmentActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = requireContext();
        mActivity = requireActivity();
        View root = inflater.inflate(R.layout.fragment_payment, container, false);
        edIdNo = root.findViewById(R.id.edIdNo);
        edYear = root.findViewById(R.id.edYear);
        spinnerMonths = root.findViewById(R.id.spinnerMonths);
        Button btnGenerateAmount = root.findViewById(R.id.btnGenerateAmount);
        btnGenerateAmount.setOnClickListener(v -> generateAmount());
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Calendar calendar = Calendar.getInstance();
        edYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        spinnerMonths.setSelection(calendar.get(Calendar.MONTH));
    }

    private void generateAmount() {
        android.app.AlertDialog waitingDialog = new Helper().waitingDialog("Generating farmer's payment", mContext);
        Runnable generateAmountThread = () -> {
            mActivity.runOnUiThread(waitingDialog::show);
            String[] months = mContext.getResources().getStringArray(R.array.months);
            String selectedMonth = months[(int) spinnerMonths.getSelectedItemId()];
            HashMap<String, String> params = new HashMap<>();
            params.put("nid", Objects.requireNonNull(edIdNo.getText()).toString().trim());
            params.put("month", selectedMonth);
            params.put("year", Objects.requireNonNull(edYear.getText()).toString().trim());

            StringRequest request = new StringRequest(Request.Method.POST, URLs.calculatePayoutUrl, response -> {
                try {
                    JSONObject responseObject = new JSONObject(response);
                    mActivity.runOnUiThread(waitingDialog::dismiss);
                    String responseCode = responseObject.getString("responseCode");
                    if (responseCode.equals("1")) {
                        String responseCount = responseObject.getString("responseCount"),
                                responseCash = responseObject.getString("responseCash"),
                                responseCapacity = responseObject.getString("responseCapacity");

                        mActivity.runOnUiThread(() -> new Helper().messageDialog(responseCount + " collections made in the month of " + selectedMonth + " and " + responseCapacity + "ltrs of milk was collected worthy Kshs." + responseCash, mContext));


                    } else {
                        String responseMessage = responseObject.getString("responseMessage");
                        mActivity.runOnUiThread(() -> new Helper().messageDialog(responseMessage, mContext));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mActivity.runOnUiThread(waitingDialog::dismiss);
                    mActivity.runOnUiThread(() -> new Helper().messageDialog("Sorry an internal error occurred please try again later\n" + e.getMessage() + response, mContext));
                }

            }, error -> {
                mActivity.runOnUiThread(waitingDialog::dismiss);
                mActivity.runOnUiThread(() -> new Helper().messageDialog("Sorry failed to connect to server please check your internet connection and try again later\n" + error.getMessage(), mContext));
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };
            Volley.newRequestQueue(mContext).add(request);
        };
        if (!(TextUtils.isEmpty(Objects.requireNonNull(edIdNo.getText()).toString().trim()) || TextUtils.isEmpty(Objects.requireNonNull(edYear.getText()).toString().trim()))) {
            new Thread(generateAmountThread).start();
        } else {
            new Helper().errorToast("Incomplete data!", mContext);
        }

    }
}