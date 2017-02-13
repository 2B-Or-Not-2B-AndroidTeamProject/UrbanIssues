package com.example.telerik.urbanissues.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.telerik.urbanissues.R;
import com.example.telerik.urbanissues.adapters.IssueAdapter;
import com.example.telerik.urbanissues.models.Issue;
import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.EverliveAppSettings;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import java.util.ArrayList;

import static com.example.telerik.urbanissues.common.Constants.APP_ID;

public class IssuesFragment extends Fragment {

    EverliveApp myApp;

    private ArrayList<Issue> issues;
    private IssueAdapter issueAdapter;

    public ArrayList<Issue> getIssues() {
        return issues;
    }

    public ArrayAdapter<Issue> getIssueAdapter() {
        return issueAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initializeSdk();
        View rootView = inflater.inflate(R.layout.fragment_issues_list, container, false);


        final ArrayList<Issue> issues = new ArrayList<Issue>();;
        getIssuesFromDb();

        System.out.println("++++++++++++ PRINTING ISSUES:");
        for (Issue issue : issues) {
            System.out.println(issue);
        }

        return rootView;
    }

    private void initializeSdk() {
        String appId = APP_ID;
        EverliveAppSettings appSettings = new EverliveAppSettings();
        appSettings.setAppId(appId);
        appSettings.setUseHttps(true);

        myApp = new EverliveApp(appSettings);
    }

    public void getIssuesFromDb() {
        myApp.workWith().data(Issue.class).getAll().executeAsync(new RequestResultCallbackAction<ArrayList<Issue>>() {
            @Override
            public void invoke(RequestResult<ArrayList<Issue>> requestResult) {
                if (requestResult.getSuccess()) {
                    for (Issue res : requestResult.getValue()) {
                        //issues.add(res);
                        System.out.println("===== Success: " + res.getTitle() + " " + res.getDescription());
                    }
                } else {
                    System.out.println("===== Error: " + requestResult.getError().toString());
                }
            }
        });
    }
}
