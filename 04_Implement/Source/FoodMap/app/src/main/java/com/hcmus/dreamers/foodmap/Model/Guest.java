package com.hcmus.dreamers.foodmap.Model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Guest extends com.hcmus.dreamers.foodmap.Model.User {
    private static Guest instance;

    private Uri urlAvatar;

    private List<Restaurant> favRestaurant;

    private Guest() {
        super();
        favRestaurant = new ArrayList<Restaurant>();
    }

    public List<Restaurant> getFavRestaurant() {
        return favRestaurant;
    }

    public void setFavRestaurant(List<Restaurant> favRestaurant) {
        this.favRestaurant = favRestaurant;
    }

    private Guest(String name, String email) {
        super(name, email);
    }

    public static Guest getInstance(){
        if (instance == null)
            instance = new Guest();
        return instance;
    }

    public static void setInstance(Guest value){
        instance = value;
    }

    public Uri getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(Uri urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public boolean isFavoriteRestaurant(int restID) {
        for(int i = 0; i < favRestaurant.size(); i++)
        {
            if(favRestaurant.get(i).getId() == restID)
            {
                return true;
            }
        }
        return false;
    }

    public void removeFavoriteRestaurant(int restID){
        for(int i = 0; i < favRestaurant.size(); i++)
        {
            if(favRestaurant.get(i).getId() == restID)
            {
                favRestaurant.remove(i);
            }
        }
    }
}
