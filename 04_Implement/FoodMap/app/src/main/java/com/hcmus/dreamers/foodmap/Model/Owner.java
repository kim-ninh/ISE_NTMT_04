package com.hcmus.dreamers.foodmap.Model;

import com.google.gson.annotations.SerializedName;

public class Owner extends com.hcmus.dreamers.foodmap.Model.User {
    private static Owner instance;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    private com.hcmus.dreamers.foodmap.Model.Restaurant restaurant;

    private Owner() {
        super();
    }

    private Owner(String name, String email) {
        super(name, email);
    }

    public com.hcmus.dreamers.foodmap.Model.Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(com.hcmus.dreamers.foodmap.Model.Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public static void setInstance(Owner instance) {
        Owner.instance = instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static Owner getInstance() {
        if (instance == null)
            instance = new Owner();
        return instance;
    }

}
