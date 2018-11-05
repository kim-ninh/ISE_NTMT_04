package com.hcmus.dreamers.foodmap.common;

import com.hcmus.dreamers.foodmap.Model.Restaurant;

import java.util.List;

public class FoodMapManager {

    private static List<Restaurant> restaurants;

    public static Restaurant findRestaurant(int id_rest){
        int n = restaurants.size();
        for (int i = 0; i< n; i++)
        {
            if (restaurants.get(i).getId() == id_rest)
            {
                return restaurants.get(i);
            }
        }
        return null;
    }

    public static void addRestaurant(Restaurant restaurant){
        restaurants.add(restaurant);
    }

    public static List<Restaurant> getRestaurants(){
        return restaurants;
    }

    public static void setRestaurants(List<Restaurant> restaurants) {
        FoodMapManager.restaurants = restaurants;
    }
}
