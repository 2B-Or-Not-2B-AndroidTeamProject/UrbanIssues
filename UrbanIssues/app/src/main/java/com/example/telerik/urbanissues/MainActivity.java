package com.example.telerik.urbanissues;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.telerik.urbanissues.models.Issue;

import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.EverliveAppSettings;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    EverliveApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MyApp","I am here");
        initializeSdk();
        getAllEntries();
    }


    private void initializeSdk() {
        String appId = "tmieglwbnjbr358i";
        EverliveAppSettings appSettings = new EverliveAppSettings();
        appSettings.setAppId(appId);
        appSettings.setUseHttps(true);

        myApp = new EverliveApp(appSettings);
    }

    public void getAllEntries() {
        myApp.workWith().data(Issue.class).getAll().executeAsync(new RequestResultCallbackAction<ArrayList<Issue>>() {

            @Override
            public void invoke(RequestResult<ArrayList<Issue>> requestResult) {
                if(requestResult.getSuccess()) {
                    for (Issue res  : requestResult.getValue()) {
                        System.out.println("===== Success: " + res.getTitle() + " " + res.getDescription());
                    }
                }
                else {
                    System.out.println("===== Errror: " + requestResult.getError().toString());
                }
            }
        });
    }
}
