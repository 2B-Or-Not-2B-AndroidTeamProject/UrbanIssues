package com.example.telerik.urbanissues.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.telerik.urbanissues.R;
import com.example.telerik.urbanissues.models.Issue;

import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.EverliveAppSettings;
import com.telerik.everlive.sdk.core.model.system.AccessToken;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import java.util.ArrayList;

import static com.example.telerik.urbanissues.common.Constants.APP_ID;


public class MainActivity extends AppCompatActivity {

    private Boolean exit = false;

    public EverliveApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeSdk();
       // getAllEntries();

        Boolean isUserRegistered = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isUserRegistered", false);
        Boolean isUserLoggedIn = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isUserLoggedIn", false);


        if (!isUserRegistered) {
            Intent intent_register = new Intent(MainActivity.this, RegisterActivity.class);
            intent_register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent_register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
            finish(); // destroy current activity..
            startActivity(intent_register);
            /*Toast.makeText(MainActivity.this, "User Not Registered", Toast.LENGTH_LONG)
                    .show();*/
        } else if (!isUserLoggedIn) {
            String sp_username = "";
            String sp_password = "";

            if (getSharedPreferences("PREFERENCE", MODE_PRIVATE).contains("username") &&
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).contains("password")) {
                sp_username = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("username", "");
                sp_password = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("password", "");
            }

            if (sp_username != "" && sp_password != "") {
                myApp.workWith().
                        authentication().
                        login(sp_username, sp_password).
                        executeAsync(new RequestResultCallbackAction<AccessToken>() {
                            @Override
                            public void invoke(RequestResult<AccessToken> accessTokenRequestResult) {
                                if (accessTokenRequestResult.getSuccess()) {
                                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                                            .putBoolean("isUserLoggedIn", true).commit();
                                } else {
                                    final String errorMessage = accessTokenRequestResult.getError().getMessage();
                                    System.out.println(errorMessage);
                                }
                            }
                        });
            } else {
                Intent intent_login = new Intent(MainActivity.this, LoginActivity.class);
                intent_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
                finish(); // destroy current activity..
                startActivity(intent_login);
                Toast.makeText(MainActivity.this, "User Not Logged In", Toast.LENGTH_LONG).show();
            }
        } else {
            Intent intent_issues = new Intent(MainActivity.this, IssuesActivity.class);
            intent_issues.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent_issues.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
            finish(); // destroy current activity..
            startActivity(intent_issues);
        }
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

    private void initializeSdk() {
        String appId = APP_ID;
        EverliveAppSettings appSettings = new EverliveAppSettings();
        appSettings.setAppId(appId);
        appSettings.setUseHttps(true);

        myApp = new EverliveApp(appSettings);
    }

    public void getAllEntries() {
        myApp.workWith().data(Issue.class).getAll().executeAsync(new RequestResultCallbackAction<ArrayList<Issue>>() {

            @Override
            public void invoke(RequestResult<ArrayList<Issue>> requestResult) {
                if (requestResult.getSuccess()) {
                    for (Issue res : requestResult.getValue()) {
                        System.out.println("===== Success: " + res.getTitle() + " " + res.getDescription());
                    }
                } else {
                    System.out.println("===== Error Main: " + requestResult.getError().toString());
                }
            }
        });
    }
}