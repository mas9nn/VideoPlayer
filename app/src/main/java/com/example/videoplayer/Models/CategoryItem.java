package com.example.videoplayer.Models;

public class CategoryItem {
    private String name,id,back_color;
    private int image;

    public CategoryItem(String name, String id, int image,String back_color) {
        this.name = name;
        this.id = id;
        this.image = image;
        this.back_color = back_color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getBack_color() {
        return back_color;
    }

    public void setBack_color(String back_color) {
        this.back_color = back_color;
    }
}
