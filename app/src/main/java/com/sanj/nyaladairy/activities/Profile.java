package com.sanj.nyaladairy.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.sanj.nyaladairy.R;
import com.sanj.nyaladairy.data.URLs;
import com.sanj.nyaladairy.wrapper.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.sanj.nyaladairy.data.URLs.updateAccountDetailsUrl;
import static com.sanj.nyaladairy.wrapper.Helper.agentNationalIdentificationNumber;
import static com.sanj.nyaladairy.wrapper.Helper.isDeleted;

public class Profile extends AppCompatActivity {
    private Context mContext;
    private TextView txtInitial, txtWelcome;
    private String name, nid, phone, route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_black);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        CardView cardSecurity = findViewById(R.id.cardSecurity);
        CardView cardDelete = findViewById(R.id.cardDelete);
        CardView cardAboutUs = findViewById(R.id.cardAboutUs);
        TextView btnEdit = findViewById(R.id.btnEdit);
        txtInitial = findViewById(R.id.txtInitial);
        txtWelcome = findViewById(R.id.txtWelcome);
        mContext = this;
        btnEdit.setOnClickListener(v -> editDetailsForm());
        cardSecurity.setOnClickListener(v -> resetPassword());
        cardDelete.setOnClickListener(v -> deleteAccount());
        cardAboutUs.setOnClickListener(v -> aboutUs());
    }

    private void editDetailsForm() {
        @SuppressLint("InflateParams") View root = LayoutInflater.from(mContext).inflate(R.layout.edit_account_details_item, null);
        TextInputEditText edFirstName, edSecondName, edIdNo, edPhone;
        Spinner routeSpinner;
        Button btnSubmit;
        edFirstName = root.findViewById(R.id.edFirstName);
        edSecondName = root.findViewById(R.id.edSecondName);
        edIdNo = root.findViewById(R.id.edIdNo);
        edPhone = root.findViewById(R.id.edPhone);
        routeSpinner = root.findViewById(R.id.routeSpinner);
        btnSubmit = root.findViewById(R.id.btnSubmit);
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setView(root)
                .create();
        dialog.show();
        String[] names = name.split(" ");
        edFirstName.setText(names[0]);
        edSecondName.setText(names[1]);
        edIdNo.setText(nid);
        edPhone.setText(phone);
        String[] routes = getResources().getStringArray(R.array.routes);
        List<String> routeList = Arrays.asList(routes.clone());
        routeSpinner.setSelection(routeList.indexOf(route));
        btnSubmit.setOnClickListener(v -> {
            if (!(Objects.requireNonNull(edFirstName.getText()).toString().isEmpty() || Objects.requireNonNull(edSecondName.getText()).toString().isEmpty()
                    || Objects.requireNonNull(edIdNo.getText()).toString().isEmpty() || Objects.requireNonNull(edPhone.getText()).toString().isEmpty())) {
                dialog.dismiss();
                name = edFirstName.getText().toString().trim() + " " + edSecondName.getText().toString().trim();
                nid = edIdNo.getText().toString().trim();
                phone = edPhone.getText().toString().trim();
                route = routes[(int) routeSpinner.getSelectedItemId()];
                updateAccountDetails();
            } else {
                new Helper().errorToast("Incomplete form!", mContext);
            }
        });
    }

    private void updateAccountDetails() {
        android.app.AlertDialog dialogWaiting = new Helper().waitingDialog("Updating agent account", mContext);
        Runnable updateAccountDetailsThread = () -> {
            runOnUiThread(dialogWaiting::show);

            HashMap<String, String> params = new HashMap<>();
            params.put("name", name);
            params.put("nid", nid);
            params.put("phone", phone);
            params.put("route", route);

            StringRequest request = new StringRequest(Request.Method.POST, updateAccountDetailsUrl, response -> {
                try {
                    JSONObject responseObject = new JSONObject(response);
                    String responseCode = responseObject.getString("responseCode");
                    String responseMessage = responseObject.getString("responseMessage");

                    if (responseCode.equals("1")) {
                        onStart();
                        runOnUiThread(() -> new Helper().successToast(responseMessage, mContext));
                    } else {
                        runOnUiThread(() -> new Helper().messageDialog(responseMessage, mContext));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        dialogWaiting.dismiss();
                        new Helper().messageDialog("Sorry an internal error occurred please try again later\n" + e.getMessage() + response, mContext);
                    });
                }
                runOnUiThread(dialogWaiting::dismiss);
            }, error -> runOnUiThread(() -> {
                dialogWaiting.dismiss();
                new Helper().messageDialog("Sorry failed to connect to server please check your internet connection and try again later\n" + error.getMessage(), mContext);
            })) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };
            Volley.newRequestQueue(mContext).add(request);
        };
        new Thread(updateAccountDetailsThread).start();
    }

    private void resetPassword() {
        Intent intent = new Intent(mContext, ForgotPassword.class);
        intent.putExtra("auth", true);
        startActivity(intent);
    }

    private void deleteAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Nyala Dairy")
                .setMessage("Do you want to delete this account")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    deleteMyAccount();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void deleteMyAccount() {
        android.app.AlertDialog dialogWaiting = new Helper().waitingDialog("Deleting Account", mContext);

        Runnable deleteMyAccountThread = () -> {
            runOnUiThread(dialogWaiting::show);
            HashMap<String, String> params = new HashMap<>();
            params.put("nid", agentNationalIdentificationNumber);

            StringRequest request = new StringRequest(Request.Method.POST, URLs.deleteAccountUrl, response -> {
                try {
                    JSONObject responseObject = new JSONObject(response);
                    runOnUiThread(dialogWaiting::dismiss);
                    String responseCode = responseObject.getString("responseCode");
                    String responseMessage = responseObject.getString("responseMessage");
                    if (responseCode.equals("1")) {
                        runOnUiThread(() -> {
                            isDeleted = true;
                            finish();
                            new Helper().errorToast(responseMessage, mContext);
                        });
                    } else {
                        runOnUiThread(() -> new Helper().errorToast(responseMessage, mContext));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(dialogWaiting::dismiss);
                    runOnUiThread(() -> new Helper().errorToast("Sorry an internal error occurred please try again later\n" + e.getMessage() + response, mContext));
                }

            }, error -> runOnUiThread(() -> {
                runOnUiThread(dialogWaiting::dismiss);
                new Helper().errorToast("Sorry failed to connect to server please check your internet connection and try again later\n" + error.getMessage(), mContext);
            })) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };
            Volley.newRequestQueue(mContext).add(request);
        };
        new Thread(deleteMyAccountThread).start();
    }

    private void aboutUs() {
        String about = "Nyala Dairy App 4.1.3\n" +
                "Build #AI-201.8743.12.41.7199119, built on May 20, 2021\n" +
                "Runtime version: 1.8.0_242-release-1644-b01 amd64\n" +
                "Android version 5 and above\n\n\n" +
                "Powered by The Jesse Inc.";
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("About Nyala Dairy App")
                .setMessage(about)
                .create()
                .show();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {
        android.app.AlertDialog dialogWaiting = new Helper().waitingDialog("Fetching Account Details", mContext);

        Runnable loadDataThread = () -> {
            runOnUiThread(dialogWaiting::show);
            HashMap<String, String> params = new HashMap<>();
            params.put("nid", agentNationalIdentificationNumber);

            StringRequest request = new StringRequest(Request.Method.POST, URLs.fetchAgentDetailsUrl, response -> {
                try {
                    JSONObject responseObject = new JSONObject(response);
                    runOnUiThread(dialogWaiting::dismiss);
                    String responseCode = responseObject.getString("responseCode");
                    if (responseCode.equals("1")) {
                        JSONArray responseArray = responseObject.getJSONArray("responseData");
                        for (int i = 0; i < responseArray.length(); i++) {
                            JSONObject responseArrayObject = responseArray.getJSONObject(i);

                            name = responseArrayObject.getString("name");
                            nid = responseArrayObject.getString("nid");
                            phone = responseArrayObject.getString("phone");
                            route = responseArrayObject.getString("route");

                            runOnUiThread(() -> {
                                txtInitial.setText(String.valueOf(name.charAt(0)));
                                txtWelcome.setText("Welcome " + name);
                            });
                        }
                    } else {
                        new Helper().errorToast(responseObject.getString("responseMessage"), mContext);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(dialogWaiting::dismiss);
                    runOnUiThread(() -> new Helper().errorToast("Sorry an internal error occurred please try again later\n" + e.getMessage() + response, mContext));
                    finish();
                }

            }, error -> runOnUiThread(() -> {
                runOnUiThread(dialogWaiting::dismiss);
                new Helper().errorToast("Sorry failed to connect to server please check your internet connection and try again later\n" + error.getMessage(), mContext);
                finish();
            })) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };
            Volley.newRequestQueue(mContext).add(request);
        };
        new Thread(loadDataThread).start();
    }
}