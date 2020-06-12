package com.example.videoplayer.Models;

public class Video {
    private String channel_name;
    private String days;
    private String duration;
    private String name;
    private String preview_image;
    private String url;
    private String video_id;
    private String views;

    public Video() {
    }

    public Video(String channel_name, String days, String duration, String name, String preview_image, String url, String video_id, String views) {
        this.channel_name = channel_name;
        this.days = days;
        this.duration = duration;
        this.name = name;
        this.preview_image = preview_image;
        this.url = url;
        this.video_id = video_id;
        this.views = views;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
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

    public String getPreview_image() {
        return preview_image;
    }

    public void setPreview_image(String preview_image) {
        this.preview_image = preview_image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }
}
