package com.example.videoplayer.Models;

public class LoginCategory {
    private int icon;
    private String naming;

    public LoginCategory(int icon, String naming) {
        this.icon = icon;
        this.naming = naming;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getNaming() {
        return naming;
    }

    public void setNaming(String naming) {
        this.naming = naming;
    }
}
