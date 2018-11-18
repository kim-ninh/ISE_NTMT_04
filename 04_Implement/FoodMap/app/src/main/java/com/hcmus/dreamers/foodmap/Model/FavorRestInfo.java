package com.hcmus.dreamers.foodmap.Model;

public class FavorRestInfo {
    private String name;
    private String url;
    private int ID;

    public FavorRestInfo(String name, String url, int id) {
        this.name = name;
        this.url = url;
        ID = id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getID() {
        return ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
