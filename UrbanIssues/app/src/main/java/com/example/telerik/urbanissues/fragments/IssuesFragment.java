package com.example.telerik.urbanissues.fragments;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.telerik.urbanissues.R;
import com.example.telerik.urbanissues.adapters.IssueAdapter;
import com.example.telerik.urbanissues.models.BaseViewModel;
import com.example.telerik.urbanissues.models.Issue;
import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.EverliveAppSettings;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import java.util.ArrayList;

import static com.example.telerik.urbanissues.common.Constants.APP_ID;

public class IssuesFragment extends Fragment {

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
        super.onCreate(savedInstanceState);

        BaseViewModel.initialize(BaseViewModel.urbanIssuesApp);

        View rootView = inflater.inflate(R.layout.fragment_issues_list, container, false);
/*
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        this.issues = new ArrayList<Issue>();

        System.out.println("Activity " + this.getActivity());
        System.out.println("Layout " + R.layout.fragment_issues_row);
        this.loadIssues(listView, this);
        System.out.println("Issues " + issues);

        this.issueAdapter = new IssueAdapter(this.getActivity(), R.layout.fragment_issues_row, issues);

        listView.setAdapter(issueAdapter);

        this.loadIssues(listView, this);
        System.out.println("Issues " + issues);
        System.out.println("++++++++++++ PRINTING ISSUES:");
        for (Issue issue : issues) {
            System.out.println(issue);
        }
*/

        return rootView;
    }

    private void loadIssues(final ListView target, final IssuesFragment issuesFragment) {
        BaseViewModel.urbanIssuesApp.workWith().
                data(Issue.class).
                getAll().
                executeAsync(new RequestResultCallbackAction<ArrayList<Issue>>() {
                    @Override
                    public void invoke(RequestResult<ArrayList<Issue>> requestResult) {
                        if (requestResult.getSuccess()) {
                            issuesFragment.getIssues().clear();
                            for (Issue issue : requestResult.getValue()) {
                                issuesFragment.getIssues().add(issue);
                            }
                            target.post(new Runnable() {
                                @Override
                                public void run() {
                                    issuesFragment.getIssueAdapter().notifyDataSetChanged();
                                }
                            });
                        } else {

                        }
                    }
                });
    }
}
