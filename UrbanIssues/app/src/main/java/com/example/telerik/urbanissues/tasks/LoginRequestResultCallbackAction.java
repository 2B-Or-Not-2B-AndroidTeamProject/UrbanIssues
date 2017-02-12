package com.example.telerik.urbanissues.tasks;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.telerik.urbanissues.activities.LoginActivity;
import com.telerik.everlive.sdk.core.model.system.AccessToken;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import static android.content.Context.MODE_PRIVATE;


public class LoginRequestResultCallbackAction extends RequestResultCallbackAction<AccessToken> {
    private String loginMethod;
    private Activity activity;
    private ProgressDialog progressDialog;

    public LoginRequestResultCallbackAction(Activity activity, String loginMethod, ProgressDialog progressDialog) {
        this.activity = activity;
        this.loginMethod = loginMethod;
        this.progressDialog = progressDialog;
    }

    @Override
    public void invoke(RequestResult<AccessToken> accessTokenRequestResult) {
        if (accessTokenRequestResult.getSuccess()) {
            progressDialog.dismiss();
            LoginActivity.startMainActivity(this.activity);
            final String errorMessage = accessTokenRequestResult.getError().getMessage();
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LoginActivity.showAlertMessage(activity, errorMessage, null);
                }
            });
        }
    }
}