package com.example.videoplayer.Models;

public class FollowedItems {
    String logo,id;

    public FollowedItems(String logo, String id) {
        this.logo = logo;
        this.id = id;
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
}
