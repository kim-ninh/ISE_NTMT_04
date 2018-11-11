package com.hcmus.dreamers.foodmap.common;


import android.content.Context;

import com.hcmus.dreamers.foodmap.Model.Comment;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.database.DBManager;

import org.osmdroid.util.GeoPoint;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class FoodMapManager {

    private static List<Restaurant> restaurants;

    public  static List<Restaurant> getRestaurants(String username){
        List<Restaurant> restaurantList = new ArrayList<>();
        int length = restaurants.size();
        for (int i = 0; i <length; i++){
            if (restaurants.get(i).getOwnerUsername().equals(username)){
                restaurantList.add(restaurants.get(i));
            }
        }
        return restaurantList;
    }

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

    public static Restaurant findRestaurant(GeoPoint point){
        int n = restaurants.size();
        for (int i = 0; i< n; i++)
        {
            if (restaurants.get(i).getLocation().equals(point))
            {
                return restaurants.get(i);
            }
        }
        return null;
    }

    public static void addRestaurant(Context context, Restaurant restaurant){
        //DBManager dbManager = new DBManager(context);
        restaurants.add(restaurant);
        //dbManager.addRestaurant(restaurant);
        //dbManager.close();
    }

    public static List<Restaurant> getRestaurants(){
        return restaurants;
    }

    public static void setRestaurants(Context context, List<Restaurant> restaurants) {
        FoodMapManager.restaurants = restaurants;

//        DBManager dbManager = new DBManager(context);
//        for (Restaurant rest: FoodMapManager.restaurants) {
//            dbManager.addRestaurant(rest);
//        }
//        dbManager.close();
    }

    public static List<Comment> getComment(Context context, int id_rest){
        List<Comment> comments;
        DBManager dbManager = new DBManager(context);
        try {
            comments = dbManager.getComments(id_rest);
        } catch (ParseException e) {
            dbManager.close();
            return null;
        }
        dbManager.close();
        return comments;
    }

    public static void addComment(Context context, int id_rest, Comment comment){

        int n = restaurants.size();
        for (int i = 0; i< n; i++)
        {
            if (restaurants.get(i).getId() == id_rest)
            {
                restaurants.get(i).getComments().add(comment);
                return;
            }
        }
    }
}
