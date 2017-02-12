package com.example.telerik.urbanissues.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.telerik.urbanissues.R;



public class LoginActivity extends Activity {


    public static void showAlertMessage(Context context, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(R.string.app_name);
        alertDialogBuilder.setPositiveButton("OK", listener);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.create().show();
    }
}

