package com.example.telerik.urbanissues.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.telerik.urbanissues.R;
import static com.example.telerik.urbanissues.common.Constants.APP_ID;
import  com.example.telerik.urbanissues.models.BaseViewModel;
import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.model.system.AccessToken;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Boolean exit = false;

    private EditText username;
    private EditText password;
    private ProgressDialog connectionProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.checkAppSettings(getIntent().getAction() != null);

        connectionProgressDialog = new ProgressDialog(this);
        connectionProgressDialog.setMessage("Logging in ...");

        this.username = (EditText) findViewById(R.id.login_username);
        this.password = (EditText) findViewById(R.id.login_password);

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.login_register).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login : {
                this.onLogin();
                break;
            }
            case R.id.login_register :  {
                Intent intent_register = new Intent(this, RegisterActivity.class);
                intent_register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent_register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
                finish(); // destroy current activity..
                startActivity(intent_register);
                break;
            }
        }
    }

    private void checkAppSettings(boolean showMessage) {
        StringBuilder sb = new StringBuilder();
        String EOL = "\r\n";

        if (APP_ID != null && APP_ID.equals("your Telerik App ID")) {
            sb.append("Telerik App ID is not set." + EOL);
        } else {
            BaseViewModel.myAppTest = new EverliveApp(APP_ID);
        }
    }

    public static void showAlertMessage(Context context, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(R.string.app_name);
        alertDialogBuilder.setPositiveButton("OK", listener);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.create().show();
    }

    public void onLogin() {
        connectionProgressDialog.show();
        BaseViewModel.myAppTest.workWith().
                authentication().
                login(this.username.getText().toString(), this.password.getText().toString()).
                executeAsync(new RequestResultCallbackAction<AccessToken>(){
                    @Override
                    public void invoke(RequestResult<AccessToken> accessTokenRequestResult) {
                        if (accessTokenRequestResult.getSuccess()) {
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                                    .putBoolean("isUserRegistered", true).commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                                    .putBoolean("isUserLoggedIn", true).commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                                    .putString("username", username.getText().toString()).commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                                    .putString("password", password.getText().toString()).commit();
                            LoginActivity.startMainActivity(LoginActivity.this);
                        }
                        else {
                            final String errorMessage = accessTokenRequestResult.getError().getMessage();
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LoginActivity.showAlertMessage(LoginActivity.this, errorMessage, null);
                                }
                            });
                        }
                    }
                });
    }

    public static void startMainActivity(Activity activity) {
        Intent intent_main = new Intent(activity, MainActivity.class);
        intent_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
        activity.finish(); // destroy current activity..
        activity.startActivity(intent_main);
    }
}

