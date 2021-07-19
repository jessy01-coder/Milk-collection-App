package com.sanj.nyaladairy.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sanj.nyaladairy.R;

import static com.sanj.nyaladairy.wrapper.Helper.agentNationalIdentificationNumber;
import static java.lang.Thread.sleep;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Runnable splashThread = () -> {
            try {
                sleep(3000);
                SharedPreferences sharedPreferences = getSharedPreferences("nyala", MODE_PRIVATE);
                if (sharedPreferences.getBoolean("loggedIn", false)) {
                    agentNationalIdentificationNumber = sharedPreferences.getString("id", "");
                    startActivity(new Intent(Splash.this, MainActivity.class));
                } else {
                    startActivity(new Intent(Splash.this, SignIn.class));
                }
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(splashThread).start();
    }
}