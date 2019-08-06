package com.example.videoplayer.Models;

public class MainPageItems {
    String preview_image, duration, name, channel_name, views, days;
    String url,id;

    public MainPageItems() {
    }

    public MainPageItems(String preview_image, String duration, String name, String channel_name, String views, String days,String url,String id) {
        this.preview_image = preview_image;
        this.duration = duration;
        this.name = name;
        this.channel_name = channel_name;
        this.views = views;
        this.days = days;
        this.url = url;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPreview_image() {
        return preview_image;
    }

    public void setPreview_image(String preview_image) {
        this.preview_image = preview_image;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
