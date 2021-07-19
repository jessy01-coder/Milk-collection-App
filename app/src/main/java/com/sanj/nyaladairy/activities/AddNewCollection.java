package com.sanj.nyaladairy.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.sanj.nyaladairy.R;
import com.sanj.nyaladairy.data.URLs;
import com.sanj.nyaladairy.fragments.Farmers;
import com.sanj.nyaladairy.wrapper.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.sanj.nyaladairy.wrapper.Helper.agentNationalIdentificationNumber;

public class AddNewCollection extends AppCompatActivity {

    private TextInputEditText edAgentIdNo, edCapacity, edCanNumber, edFarmerPhone;
    private String todayDay, todayMonth, todayYear;
    private Context mContext;
    private String price, farmerPhone, collectionId, cash, time, capacity, canNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_collection);

        edFarmerPhone = findViewById(R.id.edFarmerPhone);
        Intent intent = getIntent();
        String farmerPhone = intent.getStringExtra("phone");
        edFarmerPhone.setText(String.valueOf(farmerPhone));



        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_black);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        edAgentIdNo = findViewById(R.id.edAgentIdNo);
        edCapacity = findViewById(R.id.edCapacity);
        edCanNumber = findViewById(R.id.edCanNumber);

        Button btnSubmit = findViewById(R.id.btnSubmit);

        String[] months_of_year = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dece"};
        Calendar calendar = Calendar.getInstance();
        todayYear = String.valueOf(calendar.get(Calendar.YEAR));
        todayMonth = months_of_year[calendar.get(Calendar.MONTH)];
        todayDay = String.valueOf(calendar.get(Calendar.DATE));

        mContext = this;
        price = null;

        edAgentIdNo.setText(agentNationalIdentificationNumber);
        btnSubmit.setOnClickListener(v -> {

            if (price != null) {
                submit();
            } else {
                new Helper().errorToast("Failed to load milk price. Click [+] to try again", mContext);
                finish();
            }
        });
    }

    private void submit() {
        AlertDialog waitingDialog = new Helper().waitingDialog("Submitting new collection", mContext);
        Runnable addNewCollectionThread = () -> {
            if (!(TextUtils.isEmpty(Objects.requireNonNull(edAgentIdNo.getText()).toString().trim()) || TextUtils.isEmpty(Objects.requireNonNull(edCanNumber.getText()).toString().trim())
                    || TextUtils.isEmpty(Objects.requireNonNull(edCapacity.getText()).toString().trim()))) {
                runOnUiThread(waitingDialog::show);
                String agentNID = edAgentIdNo.getText().toString().trim();
                capacity = edCapacity.getText().toString().trim();
                collectionId = generateCollectionCode();
                canNumber = edCanNumber.getText().toString().trim();
                cash = String.valueOf(Float.parseFloat(price) * Float.parseFloat(capacity));
                Calendar calendar = Calendar.getInstance();
                time = calendar.get(Calendar.HOUR_OF_DAY) + "" + calendar.get(Calendar.MINUTE);


                HashMap<String, String> params = new HashMap<>();
                params.put("id", collectionId);
                params.put("phone", farmerPhone);
                params.put("agentNID", agentNID);
                params.put("capacity", capacity);
                params.put("cash", cash);
                params.put("canNumber", canNumber);
                params.put("shift", getShifts());
                params.put("day", todayDay);
                params.put("month", todayMonth);
                params.put("year", todayYear);
                params.put("time", time);

                StringRequest request = new StringRequest(Request.Method.POST, URLs.addNewCollectionUrl, response -> {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        String responseCode = responseObject.getString("responseCode");
                        String responseMessage = responseObject.getString("responseMessage");

                        if (responseCode.equals("1")) {
                            runOnUiThread(() -> {
                                new Helper().successToast(responseMessage, mContext);
                                sendConfirmationMessage();
                            });
                            finish();
                        } else {
                            runOnUiThread(() -> new Helper().messageDialog(responseMessage, mContext));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            waitingDialog.dismiss();
                            new Helper().messageDialog("Sorry an internal error occurred please try again later\n" + e.getMessage() + response, mContext);
                        });
                    }
                    runOnUiThread(waitingDialog::dismiss);
                }, error -> runOnUiThread(() -> {
                    waitingDialog.dismiss();
                    error.printStackTrace();
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
        if (!TextUtils.isEmpty(Objects.requireNonNull(edFarmerPhone.getText()).toString().trim())) {
            farmerPhone = edFarmerPhone.getText().toString().trim();
            new androidx.appcompat.app.AlertDialog.Builder(mContext)
                    .setCancelable(true)
                    .setTitle("NYALA DAIRY")
                    .setMessage("Send confirmation message to " + farmerPhone)
                    .setPositiveButton("PROCEED", (dialog, which) -> {
                        dialog.dismiss();
                        new Thread(addNewCollectionThread).start();
                    })
                    .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                    .create().show();
        }

    }

    private String generateCollectionCode() {
        StringBuilder code = new StringBuilder();
        String timestamp = String.valueOf(new Date().getTime());
        String[] firstAlpha = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String[] secondAlphas = new String[]{"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T"};
        String[] thirdAlphas = new String[]{"U", "V", "W", "X", "Y", "Z", "A", "B", "C", "D"};
        String[] numeric = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        boolean isFirstAlphas, isSecondAlphas, isThirdAlphas;

        isFirstAlphas = true;
        isSecondAlphas = false;
        isThirdAlphas = false;
        for (int i = 0; i < timestamp.length(); i++) {
            int index = Integer.parseInt(String.valueOf(timestamp.charAt(i)));
            if (isFirstAlphas) {
                isFirstAlphas = false;
                isSecondAlphas = true;
                isThirdAlphas = false;
                code.append(firstAlpha[index]);
            } else if (isSecondAlphas) {
                isSecondAlphas = false;
                isThirdAlphas = true;
                code.append(secondAlphas[index]);
            } else if (isThirdAlphas) {
                isThirdAlphas = false;
                code.append(thirdAlphas[index]);
            } else {
                isFirstAlphas = true;
                isSecondAlphas = false;
                isThirdAlphas = false;
                code.append(numeric[index]);
            }
        }

        return code.toString();
    }

    private void sendConfirmationMessage() {


            //String confirmationMessage = collectionId + " Confirmed can number " + canNumber + " with " + capacity + " litres of milk worthy Ksh." + cash + " delivered to Nyala Dairy agent on "
                    //+ todayDay + "/" + todayMonth + "/" + todayYear + " at " + time + "hrs. Agent Ref. #" + agentNationalIdentificationNumber + "\n If you didn't deliver milk to Nyala dairy kindly ignore this message.";
            //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", farmerPhone, null));
            //intent.putExtra("sms_body", confirmationMessage);
            //startActivity(intent);
        Intent intent = new Intent(getApplicationContext(), Farmers.class);
        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
        String phone =edFarmerPhone.getText().toString().trim();
        String capacity =edCapacity.getText().toString().trim();
        //String canNo =edCanNumber.getText().toString().trim();

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone,null, collectionId + "\n Liters of milk delivered: " + capacity + "\n Can number: " +canNumber  + "\n delivered on: " + todayDay + "/" + todayMonth + "/" + todayYear + " at " + time + " hrs ",pi, null);
        Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadPrice();
    }

    @SuppressLint("SetTextI18n")
    private void loadPrice() {
        Runnable fetchPricingThread = () -> {
            StringRequest request = new StringRequest(Request.Method.POST, URLs.fetchPricingUrl, response -> {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    String responseCode = responseObject.getString("responseCode");
                    if (responseCode.equals("1")) {
                        price = responseObject.getString("responsePrice");
                    } else {
                        String responseMessage = responseObject.getString("responseMessage");
                        runOnUiThread(() -> new Helper().errorToast(responseMessage, mContext));
                        finish();
                    }
                } catch (JSONException e) {
                    finish();
                    e.printStackTrace();
                    runOnUiThread(() -> new Helper().errorToast("Sorry an internal error occurred please try again later\n" + e.getMessage() + response, mContext));
                }

            }, error -> runOnUiThread(() -> {
                new Helper().errorToast("Sorry failed to connect to server please check your internet connection and try again later\n" + error.getMessage(), mContext);
                finish();
            }));
            Volley.newRequestQueue(mContext).add(request);
        };
        new Thread(fetchPricingThread).start();
    }

    private String getShifts() {
        Calendar calendar = Calendar.getInstance();
        int hoursOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (hoursOfDay >= 12 && hoursOfDay <= 16) {
            return "Afternoon";
        } else if (hoursOfDay >= 17 || hoursOfDay <= 3) {
            return "Evening";
        } else {
            return "Morning";
        }
    }
}