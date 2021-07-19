package com.sanj.nyaladairy.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.sanj.nyaladairy.R;
import com.sanj.nyaladairy.data.URLs;
import com.sanj.nyaladairy.fragments.Farmers;
import com.sanj.nyaladairy.fragments.Home;
import com.sanj.nyaladairy.fragments.Payment;
import com.sanj.nyaladairy.fragments.Report;
import com.sanj.nyaladairy.wrapper.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.sanj.nyaladairy.wrapper.Helper.isDeleted;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment;
    private String price;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_black);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuHome:
                    fragment = new Home();
                    break;
                case R.id.menuFarmer:
                    fragment = new Farmers();
                    break;
                case R.id.menuPayouts:
                    fragment = new Payment();
                    break;
                case R.id.menuReport:
                    fragment = new Report();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
            return true;
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new Home()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSignOut:
                signOut();
                break;
            case R.id.menuProfile:
                startActivity(new Intent(this, Profile.class));
                break;
            case R.id.menuHistory:
                startActivity(new Intent(this, MyCollections.class));
                break;
            case R.id.menuUpdatePrice:
                displayUpdatePriceForm();
                break;
        }
        return true;
    }

    private void displayUpdatePriceForm() {
        @SuppressLint("InflateParams") View root= LayoutInflater.from(this).inflate(R.layout.update_milk_price_item,null);
        TextInputEditText edPrice=root.findViewById(R.id.edPrice);
        Button btnSubmit=root.findViewById(R.id.btnSubmit);
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setView(root)
                .create();
        dialog.show();
        edPrice.setText(price);
        btnSubmit.setOnClickListener(v -> {
            if (!(TextUtils.isEmpty(Objects.requireNonNull(edPrice.getText()).toString().trim()))){
                dialog.dismiss();
                String mPrice=edPrice.getText().toString().trim();
                updatePrice(mPrice);
            }else{
                new Helper().errorToast("Input price!",MainActivity.this);
            }
        });
    }

    private void updatePrice(String mPrice) {
        Runnable updatePricingThread = () -> {
            HashMap<String,String> params=new HashMap<>();
            params.put("price",mPrice);
            StringRequest request = new StringRequest(Request.Method.POST, URLs.updatePricingUrl, response -> {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    String responseCode = responseObject.getString("responseCode");
                    String responseMessage = responseObject.getString("responseMessage");
                    if (responseCode.equals("1")) {
                        runOnUiThread(() -> new Helper().successToast(responseMessage, MainActivity.this));
                    } else {
                        runOnUiThread(() -> new Helper().messageDialog(responseMessage, MainActivity.this));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> new Helper().messageDialog("Sorry an internal error occurred please try again later\n" + e.getMessage() + response, MainActivity.this));
                }

            }, error -> runOnUiThread(() -> new Helper().messageDialog("Sorry failed to connect to server please check your internet connection and try again later\n" + error.getMessage(), MainActivity.this))){
                @Override
                protected Map<String, String> getParams(){
                    return params;
                }
            };
            Volley.newRequestQueue(MainActivity.this).add(request);
        };
        new Thread(updatePricingThread).start();
    }

    private void signOut() {
        SharedPreferences sharedPreferences = getSharedPreferences("nyala", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        startActivity(new Intent(this, Splash.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isDeleted) {
            signOut();
        }else{
            loadPrice();
        }
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
                        runOnUiThread(() -> new Helper().messageDialog(responseMessage, MainActivity.this));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> new Helper().messageDialog("Sorry an internal error occurred please try again later\n" + e.getMessage() + response, MainActivity.this));
                }

            }, error -> runOnUiThread(() -> new Helper().messageDialog("Sorry failed to connect to server please check your internet connection and try again later\n" + error.getMessage(), MainActivity.this))) ;
            Volley.newRequestQueue(MainActivity.this).add(request);
        };
        new Thread(fetchPricingThread).start();
    }
}