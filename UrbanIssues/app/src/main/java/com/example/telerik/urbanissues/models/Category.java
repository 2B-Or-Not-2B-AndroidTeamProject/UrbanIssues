package com.example.telerik.urbanissues.models;

import com.telerik.everlive.sdk.core.model.base.DataItem;
import com.telerik.everlive.sdk.core.serialization.ServerProperty;
import com.telerik.everlive.sdk.core.serialization.ServerType;

import java.util.ArrayList;
import java.util.UUID;

@ServerType("Category")
public class Category extends DataItem {

    @ServerProperty("Name")
    private String name;

    @ServerProperty("Issues")
    private ArrayList<UUID> issues;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<UUID> getIssues() { return issues; }

    public void setIssues(ArrayList<UUID> issues) { this.issues = issues; }
}

