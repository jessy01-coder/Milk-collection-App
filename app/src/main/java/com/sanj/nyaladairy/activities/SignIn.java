package com.sanj.nyaladairy.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.sanj.nyaladairy.R;
import com.sanj.nyaladairy.data.URLs;
import com.sanj.nyaladairy.wrapper.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.sanj.nyaladairy.wrapper.Helper.agentNationalIdentificationNumber;

public class SignIn extends AppCompatActivity {
    private TextInputEditText edIdNo, edPassword;
    private Context mContext;
    private CheckBox checkRemember;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        mContext = this;
        edIdNo = findViewById(R.id.edIdNo);
        checkRemember = findViewById(R.id.checkRemember);
        TextView txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ForgotPassword.class);
            intent.putExtra("auth", false);
            startActivity(intent);
        });
        edPassword = findViewById(R.id.edPassword);
        Button btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(v -> authenticate());
        Button btnToCreateAccount = findViewById(R.id.btnCreateAccount);
        btnToCreateAccount.setOnClickListener(v -> startActivity(new Intent(mContext, SignUp.class)));
        SharedPreferences sharedPreferences = getSharedPreferences("nyala", MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    private void authenticate() {
        AlertDialog waitingDialog = new Helper().waitingDialog("Authenticating Agent", mContext);
        Runnable authenticationThread = () -> {
            if (!(TextUtils.isEmpty(Objects.requireNonNull(edIdNo.getText()).toString().trim()) || TextUtils.isEmpty(Objects.requireNonNull(edPassword.getText()).toString().trim()))) {
                runOnUiThread(waitingDialog::show);
                String password = edPassword.getText().toString().trim();
                String idNo = edIdNo.getText().toString().trim();

                HashMap<String, String> params = new HashMap<>();
                params.put("nid", idNo);
                params.put("password", password);

                StringRequest request = new StringRequest(Request.Method.POST, URLs.authenticateUrl, response -> {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        String responseCode = responseObject.getString("responseCode");
                        String responseMessage = responseObject.getString("responseMessage");

                        if (responseCode.equals("1")) {
                            runOnUiThread(() -> {
                                new Helper().successToast(responseMessage, mContext);
                                if (checkRemember.isChecked()) {
                                    editor.putString("id", idNo);
                                    editor.putBoolean("loggedIn", true);
                                    editor.apply();
                                }
                            });
                            agentNationalIdentificationNumber = idNo;
                            startActivity(new Intent(mContext, MainActivity.class));
                            finish();
                        } else {
                            runOnUiThread(() -> new Helper().messageDialog(responseMessage, mContext));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            waitingDialog.dismiss();
                            new Helper().messageDialog("Sorry an internal error occurred please try again later\n" + e.getMessage(), mContext);
                        });
                    }
                    runOnUiThread(waitingDialog::dismiss);
                }, error -> runOnUiThread(() -> {
                    waitingDialog.dismiss();
                    new Helper().messageDialog("Sorry failed to connect to server please check your internet connection and try again later\n" + error.getMessage(), mContext);
                })) {
                    @Override
                    protected Map<String, String> getParams() {
                        return params;
                    }
                };
                Volley.newRequestQueue(mContext).add(request);
            } else {
                runOnUiThread(() -> new Helper().errorToast("Submitting empty fields!", mContext));
            }
        };
        new Thread(authenticationThread).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }
    }
}