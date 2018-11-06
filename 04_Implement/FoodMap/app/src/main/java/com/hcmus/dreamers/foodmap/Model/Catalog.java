package com.hcmus.dreamers.foodmap.Model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Catalog implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    private static String[] dishTypes = {"Cơm",
            "Phở",
            "Chè",
            "Vỉa hè",};

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

    public static String[] getDishTypes(){
        return dishTypes;
    }
}
