package com.example.telerik.urbanissues.models;

import com.telerik.everlive.sdk.core.model.base.DataItem;
import com.telerik.everlive.sdk.core.serialization.ServerProperty;
import com.telerik.everlive.sdk.core.serialization.ServerType;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

@ServerType("Issue")
public class Issue extends DataItem {

    @ServerProperty("Title")
    private String title;

    @ServerProperty("Description")
    private String description;

    @ServerProperty("Rating")
    private int rating;
/*
    @ServerProperty("Category")
    private Category category;

    @ServerProperty("City")
    private Location location;

    @ServerProperty("Comments")
    private ArrayList<UUID> comments;

    @ServerProperty("Images")
    private ArrayList<UUID> images;
*/
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
/*
    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }

    public Location getLocation() { return location; }

    public void setLocation(Location location) { this.location = location; }

    public ArrayList<UUID> getComments() { return comments; }

    public void setComments(ArrayList<UUID> comments) { this.comments = comments; }

    public ArrayList<UUID> getImages() { return images; }

    public void setImages(ArrayList<UUID> images) { this.images = images; }
    */
}

