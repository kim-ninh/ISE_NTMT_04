package com.hcmus.dreamers.foodmap_admin.model;

import java.util.ArrayList;
import java.util.List;

public class Admin {
    private List<Restaurant> restaurantList;
    private static Admin instance;

    private String username;
    private String password;

    private Admin(){
        restaurantList = new ArrayList<>();
    }

    public static Admin getInstance() {
        if (instance == null)
            instance = new Admin();
        return instance;
    }

    public static void setInstance(Admin instance) {
        Admin.instance = instance;
    }

    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
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

    public Restaurant findRestaurant(int id_rest){
        for (Restaurant rest: restaurantList) {
            if(rest.getId() == id_rest)
                return rest;
        }
        return null;
    }

}
