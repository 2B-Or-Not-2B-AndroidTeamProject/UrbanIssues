package com.example.telerik.urbanissues.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.telerik.urbanissues.R;
import com.example.telerik.urbanissues.models.Issue;
import com.example.telerik.urbanissues.adapters.IssueAdapter;
import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.EverliveAppSettings;
import com.telerik.everlive.sdk.core.result.RequestResult;

import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import java.util.ArrayList;

import static com.example.telerik.urbanissues.common.Constants.APP_ID;

public class IssuesActivity extends AppCompatActivity {

    private  EverliveApp myApp;
    private ActionBar actionBar;
    private ArrayList<Issue> issues;
    private IssueAdapter issueAdapter;

    public ArrayList<Issue> getIssues() {
        return issues;
    }

    public ArrayAdapter<Issue> getIssueAdapter() {
        return issueAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_issues_list);

        initializeSdk();

        ListView listView = (ListView) findViewById(R.id.list_issues_listView);
        this.issues = new ArrayList<Issue>();
        this.issueAdapter = new IssueAdapter(this, R.layout.fragment_issues_row, issues);

        listView.setAdapter(issueAdapter);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        /*
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 52, 73, 94)));
        */
        this.loadIssues(listView, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                Issue selectedIssue = (Issue) parent.getAdapter().getItem(position);
                if (selectedIssue != null) {
                    BaseViewModel.getInstance().setselectedIssue(selectedPost);
                    Intent i = new Intent(getBaseContext(), DetailViewActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(i);
                }
                */
            }
        });
    }

    private void initializeSdk() {
        String appId = APP_ID;
        EverliveAppSettings appSettings = new EverliveAppSettings();
        appSettings.setAppId(appId);
        appSettings.setUseHttps(true);

        myApp = new EverliveApp(appSettings);
    }

    private void loadIssues(final ListView target, final IssuesActivity issuesActivity) {
       myApp.workWith().
                data(Issue.class).
                getAll().
                executeAsync(new RequestResultCallbackAction<ArrayList<Issue>>() {
                    @Override
                    public void invoke(RequestResult<ArrayList<Issue>> requestResult) {
                        if (requestResult.getSuccess()) {
                            issuesActivity.getIssues().clear();
                            for (Issue issue : requestResult.getValue()) {
                                issuesActivity.getIssues().add(issue);
                            }
                            target.post (new Runnable() {
                                @Override
                                public void run() {
                                    issuesActivity.getIssueAdapter().notifyDataSetChanged();
                                }
                            });
                        } else {

                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add : {
                Intent i = new Intent(this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
            default : return super.onOptionsItemSelected(item);
        }
    }
}
