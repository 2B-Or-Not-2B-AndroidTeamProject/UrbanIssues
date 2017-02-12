package com.example.telerik.urbanissues.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.example.telerik.urbanissues.R;
import static com.example.telerik.urbanissues.common.Constants.APP_ID;
import  com.example.telerik.urbanissues.models.BaseViewModel;
import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.model.system.AccessToken;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

public class LoginActivity extends Activity implements View.OnClickListener {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login : {
                this.onLogin();
                break;
            }
            case R.id.login_register :  {
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
                break;
            }
        }
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
        Intent i = new Intent(activity, MainActivity.class);
        activity.startActivity(i);
    }

}

