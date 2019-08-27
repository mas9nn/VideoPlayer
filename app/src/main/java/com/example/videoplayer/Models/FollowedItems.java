package com.example.videoplayer.Models;

public class FollowedItems {
    String logo,id,title;
    int resource;

    public FollowedItems(String logo, String id,String title) {
        this.logo = logo;
        this.id = id;
        this.title = title;
    }

    public FollowedItems(int resource) {
        this.resource = resource;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}
