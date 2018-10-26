package com.hcmus.dreamers.foodmap.Model;

import com.hcmus.dreamers.foodmap.common.GenerateRequest;
import com.hcmus.dreamers.foodmap.common.SendRequest;

import java.io.IOException;

import okhttp3.Request;

public class Owner extends User {

    private static Owner instance;
    private String username;
    private String password;
    private String phoneNumber;
    private Restaurant restaurant;
    private String token;

    private Owner() {
        super();
    }

    private Owner(String name, String email) {
        super(name, email);
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
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

    //uncomplete....
    public static boolean Login(String username, String password)
    {
        instance.username = username;
        instance.password = password;

        Request request;
        String respond;

        try {
            request = GenerateRequest.checkLogin(instance);
            respond = SendRequest.send(request);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean changePassword(String newPassword)
    {

        Request request;
        String respond;

        instance.password = password;

        try {
            request = GenerateRequest.updateAccount(instance, token);
            respond = SendRequest.send(request);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }


}
