package com.example.telerik.urbanissues.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.telerik.urbanissues.R;
import com.example.telerik.urbanissues.models.IssueType;

public class SubmitIssueFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_submit_issue, container, false);


        Spinner dropdown = (Spinner) rootView.findViewById(R.id.spinner_create_issue_categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_dropdown_item, getIssueTypes());
        dropdown.setAdapter(adapter);

        return rootView;
    }

    private String[] getIssueTypes() {
        String[] items = new String[IssueType.values().length];

        int i = 0;
        for (IssueType issueType : IssueType.values()) {
            items[i] = issueType.toString();
            i++;
        }

        return items;
    }
}
