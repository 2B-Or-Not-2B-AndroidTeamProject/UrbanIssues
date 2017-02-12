package com.example.telerik.urbanissues.models;

import com.telerik.everlive.sdk.core.model.base.DataItem;
import com.telerik.everlive.sdk.core.serialization.ServerProperty;
import com.telerik.everlive.sdk.core.serialization.ServerType;

import java.util.UUID;

@ServerType("Comment")
public class Comment extends DataItem {

    @ServerProperty("Content")
    private String content;

    @ServerProperty("Issue")
    private UUID issueId;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getIssue() {
        return issueId;
    }

    public void setIssue(UUID issueId) {
        this.issueId = issueId;
    }
}