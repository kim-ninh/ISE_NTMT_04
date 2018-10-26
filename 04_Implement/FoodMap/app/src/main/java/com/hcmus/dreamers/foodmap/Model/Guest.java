package com.hcmus.dreamers.foodmap.Model;

import java.util.List;

public class Guest extends com.hcmus.dreamers.foodmap.Model.User {
    private static Guest instance;
    private List<com.hcmus.dreamers.foodmap.Model.Restaurant> favRestaurant;

    private Guest() {
        super();
    }

    private Guest(String name, String email) {
        super(name, email);
    }

    public static Guest getInstance(){
        if (instance == null)
            instance = new Guest();
        return instance;
    }
}
