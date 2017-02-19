package com.example.telerik.urbanissues.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.telerik.urbanissues.R;
import com.example.telerik.urbanissues.adapters.IssueAdapter;
import com.example.telerik.urbanissues.models.Issue;
import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.EverliveAppSettings;
import com.telerik.everlive.sdk.core.query.definition.sorting.SortDirection;
import com.telerik.everlive.sdk.core.query.definition.sorting.SortingDefinition;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import java.util.ArrayList;

import static com.example.telerik.urbanissues.common.Constants.APP_ID;

public class ListIssuesActivity extends AppCompatActivity {

    EverliveApp myApp;

    private ArrayList<Issue> issues;
    private IssueAdapter issueAdapter;

    public ArrayList<Issue> getIssues() {
        return issues;
    }

    public ArrayAdapter<Issue> getIssuesAdapter() {
        return issueAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_issues_list);

        initializeSdk();

        //ListView listView = (ListView) findViewById(R.id.list);
        //this.issueAdapter = new IssueAdapter(this, R.layout.fragment_issues, issues);

        //listView.setAdapter(issueAdapter);

        this.issues = new ArrayList<>();

        issues = GetIssues(myApp);
        if (this.getIssues() == null) {
            System.out.println("WTF");
        }
        else {
            System.out.println(this.getIssues());
            System.out.println("++++++++++++ PRINTING ISSUES:");
            for (Issue issue : this.getIssues()) {
                System.out.println(issue);
            }
        }
    }

    // This is from the documentation - not sure why it returns null
    // http://docs.telerik.com/platform/backend-services/android/queries/queries-sorting
    // I am not able to store the request result in an array
    public ArrayList<Issue> GetIssues(EverliveApp app) {
        SortingDefinition sortAsc = new SortingDefinition("CreatedAt", SortDirection.Ascending);
        RequestResult<ArrayList<Issue>> requestResult = app.workWith().data(Issue.class).get().sort(sortAsc).executeSync();
        return requestResult.getSuccess() ? requestResult.getValue() : null;
    }

    private void initializeSdk() {
        String appId = APP_ID;
        EverliveAppSettings appSettings = new EverliveAppSettings();
        appSettings.setAppId(appId);
        //appSettings.setUseHttps(true);

        myApp = new EverliveApp(appSettings);
    }
}
