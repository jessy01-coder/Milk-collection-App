package com.sanj.nyaladairy.wrapper;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sanj.nyaladairy.R;

import dmax.dialog.SpotsDialog;

public class Helper {
    public static String agentNationalIdentificationNumber;
    public static Boolean isDeleted = false;

    public void errorToast(String text, Context context) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            View view = toast.getView();
            view.getBackground().setColorFilter(context.getResources().getColor(R.color.red_700), PorterDuff.Mode.SRC_IN);
            toast.setGravity(Gravity.TOP, 0, 0);
            TextView textView = view.findViewById(android.R.id.message);
            textView.setTextColor(context.getResources().getColor(R.color.white));
        }
        toast.show();
    }

    public void successToast(String text, Context context) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            View view = toast.getView();
            view.getBackground().setColorFilter(context.getResources().getColor(R.color.green_700), PorterDuff.Mode.SRC_IN);
            toast.setGravity(Gravity.TOP, 0, 0);
            TextView textView = view.findViewById(android.R.id.message);
            textView.setTextColor(context.getResources().getColor(R.color.white));
        }
        toast.show();
    }

    public AlertDialog waitingDialog(String message, Context context) {
        return new SpotsDialog.Builder()
                .setCancelable(false)
                .setMessage(message)
                .setContext(context)
                .build();
    }

    public void messageDialog(String message, Context context) {
        new androidx.appcompat.app.AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle("NYALA DAIRY")
                .setMessage(message)
                .create().show();
    }

}
