package com.example.telerik.urbanissues.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.telerik.urbanissues.fragments.IssuesFragment;
import com.example.telerik.urbanissues.fragments.MyIssuesFragment;
import com.example.telerik.urbanissues.fragments.SubmitIssueFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new IssuesFragment();
            case 1:
                return new SubmitIssueFragment();
            case 2:
                return new MyIssuesFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}