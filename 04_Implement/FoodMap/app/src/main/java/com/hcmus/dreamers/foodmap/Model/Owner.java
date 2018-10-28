package com.hcmus.dreamers.foodmap.Model;
<<<<<<< HEAD
=======

import com.hcmus.dreamers.foodmap.AsyncTaskOwner.AsyncTaskDelete;
import com.hcmus.dreamers.foodmap.AsyncTaskOwner.AsyncTaskForLogin;
import com.hcmus.dreamers.foodmap.AsyncTaskOwner.AsyncTaskUpdateInfo;
import com.hcmus.dreamers.foodmap.common.GenerateRequest;
import com.hcmus.dreamers.foodmap.common.ResponseJSON;
import com.hcmus.dreamers.foodmap.common.SendRequest;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;

>>>>>>> 1f75ee0703c52a89cb505e966fffdcd4f95bb295

import com.google.gson.annotations.SerializedName;

public class Owner extends com.hcmus.dreamers.foodmap.Model.User {
    private static Owner instance;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("phoneNumber")
    private String phoneNumber;
<<<<<<< HEAD
    private com.hcmus.dreamers.foodmap.Model.Restaurant restaurant;
=======
    private Restaurant restaurant;
    private String token;
>>>>>>> 1f75ee0703c52a89cb505e966fffdcd4f95bb295

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

    public String getToken() {
        return token;
    }

    public static boolean Login(String username, String password)
    {
        String user_temp = instance.username;
        String pass_temp = instance.password;

        instance.username = username;
        instance.password = password;

        String respond;

        AsyncTaskForLogin asyncTaskForLogin = new AsyncTaskForLogin();
        asyncTaskForLogin.execute(instance);

        respond = asyncTaskForLogin.getRespond();

        ResponseJSON parseJSON = ParseJSON.fromStringToResponeJSON(respond);

        if(parseJSON.getCode() == 200)
        {
           /*instance.setName(parseJSON.getName());
            instance.phoneNumber = parseJSON.getPhoneNumber();
            instance.setEmail(parseJSON.getEmail());
            instance.token = parseJSON.getToken();*/
            return true;
        }
        else
        {
            instance.username = user_temp;
            instance.password = pass_temp;
            return false;
        }
    }

    public boolean changePassword(String newPassword)
    {
        String respond;
        String pass_temp = instance.password;
        instance.password = newPassword;

        AsyncTaskUpdateInfo asyncTaskUpdateInfo = new AsyncTaskUpdateInfo();

        asyncTaskUpdateInfo.equals(instance);

        respond = asyncTaskUpdateInfo.getRespond();

        ResponseJSON parseJSON = ParseJSON.fromStringToResponeJSON(respond);

        if(parseJSON.getCode() == 200)
        {
            return true;
        }
        else
        {
            instance.password = pass_temp;
            return false;
        }
    }

    public boolean updateInformation(String name, String phoneNumber, String email)
    {
        String respond;
        String name_temp = instance.getName();
        String phone_temp = instance.phoneNumber;
        String email_temp = instance.getEmail();
        instance.setName(name);
        instance.phoneNumber = phoneNumber;
        instance.setEmail(email);

        AsyncTaskUpdateInfo asyncTaskUpdateInfo = new AsyncTaskUpdateInfo();

        asyncTaskUpdateInfo.execute(instance);

        respond = asyncTaskUpdateInfo.getRespond();

        ResponseJSON parseJSON = ParseJSON.fromStringToResponeJSON(respond);

        if(parseJSON.getCode() == 200)
        {
            return true;
        }
        else
        {
            instance.setName(name_temp);
            instance.phoneNumber = phone_temp;
            instance.setEmail(email_temp);
            return false;
        }
    }

    public boolean deleteAcount()
    {
        String respond;

        AsyncTaskDelete asyncTaskDelete = new AsyncTaskDelete();

        asyncTaskDelete.execute(instance.username, instance.token);

        respond = asyncTaskDelete.getRespond();

        ResponseJSON parseJSON = ParseJSON.fromStringToResponeJSON(respond);

        if(parseJSON.getCode() == 200)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
