package com.example.telerik.urbanissues.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.telerik.urbanissues.R;
import com.example.telerik.urbanissues.adapters.TabsPagerAdapter;
import com.example.telerik.urbanissues.models.Issue;

import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.EverliveAppSettings;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import java.util.ArrayList;

import static com.example.telerik.urbanissues.common.Constants.APP_ID;


public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

    public EverliveApp myApp;

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Issues", "Submit", "My Issues" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeSdk();
        getAllEntries();

        Boolean isUserRegistered = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isUserRegistered", false);
        Boolean isUserLoggedIn = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isUserLoggedIn", false);


        if (!isUserRegistered) {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            Toast.makeText(MainActivity.this, "User Not Registered", Toast.LENGTH_LONG)
                    .show();
        }
        else if (!isUserLoggedIn) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            Toast.makeText(MainActivity.this, "User Not Logged In", Toast.LENGTH_LONG)
                    .show();
        }
        else {


            // Initilization
            viewPager = (ViewPager) findViewById(R.id.pager);
            actionBar = getSupportActionBar();
            mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

            viewPager.setAdapter(mAdapter);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            // Adding Tabs
            for (String tab_name : tabs) {
                actionBar.addTab(actionBar.newTab().setText(tab_name)
                        .setTabListener(this));
            }
            /**
             * on swiping the viewpager make respective tab selected
             * */
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    // on changing the page
                    // make respected tab selected
                    actionBar.setSelectedNavigationItem(position);
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            });
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }
}
