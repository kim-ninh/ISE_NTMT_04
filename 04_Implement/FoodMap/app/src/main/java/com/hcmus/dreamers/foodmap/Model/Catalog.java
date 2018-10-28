package com.hcmus.dreamers.foodmap.Model;
<<<<<<< HEAD
<<<<<<< HEAD

import com.google.gson.annotations.SerializedName;
=======
>>>>>>> 1f75ee0703c52a89cb505e966fffdcd4f95bb295
=======


import com.google.gson.annotations.SerializedName;
>>>>>>> 4699d4b57b07c302afafa523ac3181478c538834

public class Catalog {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public Catalog() {
    }

    public Catalog(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
