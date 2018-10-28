package com.hcmus.dreamers.foodmap.Model;

import com.hcmus.dreamers.foodmap.AsyncTaskOwner.AsyncTaskCreateRestaurant;
import com.hcmus.dreamers.foodmap.AsyncTaskOwner.AsyncTaskDelete;
import com.hcmus.dreamers.foodmap.AsyncTaskOwner.AsyncTaskForLogin;
import com.hcmus.dreamers.foodmap.AsyncTaskOwner.AsyncTaskUpdateInfo;
import com.hcmus.dreamers.foodmap.common.GenerateRequest;
import com.hcmus.dreamers.foodmap.common.ResponseJSON;
import com.hcmus.dreamers.foodmap.common.SendRequest;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Owner extends com.hcmus.dreamers.foodmap.Model.User {
    private static Owner instance;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("phoneNumber")
    private String phoneNumber;

    private Restaurant restaurant;

    @SerializedName("token")
    private String token;

    private List<Restaurant> listRestaurant;


    private Owner() {
        super();
    }

    private Owner(String name, String email) {
        super(name, email);
    }

    public List<Restaurant> getlistRestaurant() {
        return listRestaurant;
    }

    public void setlistRestaurant(List<Restaurant> listRestaurant) {
        this.listRestaurant = listRestaurant;
    }

    public List<Restaurant> getListRestaurant() {
        return listRestaurant;
    }

    public Restaurant getRestaurant(int index) {
        return listRestaurant.get(index);
    }

    private void addRestaurant(Restaurant restaurant) {
        listRestaurant.add(restaurant);

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

        instance.username = username;
        instance.password = password;

        String respond;

        AsyncTaskForLogin asyncTaskForLogin = new AsyncTaskForLogin();
        asyncTaskForLogin.execute(instance);

        respond = asyncTaskForLogin.getRespond();

        ResponseJSON parseJSON = ParseJSON.fromStringToResponeJSON(respond);

        if(parseJSON.getCode() == 200)
        {
            try {
                instance = ParseJSON.parseOwnerFromCreateAccount(respond);
            } catch (JSONException e) {
                e.printStackTrace();

                return false;
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean changePassword(String newPassword)
    {
        String pass_temp = instance.password;
        instance.password = newPassword;

        AsyncTaskUpdateInfo asyncTaskUpdateInfo = new AsyncTaskUpdateInfo();

        asyncTaskUpdateInfo.execute(instance);

        String respond = asyncTaskUpdateInfo.getRespond();

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
        String name_temp = instance.getName();
        String phone_temp = instance.phoneNumber;
        String email_temp = instance.getEmail();
        instance.setName(name);
        instance.phoneNumber = phoneNumber;
        instance.setEmail(email);

        AsyncTaskUpdateInfo asyncTaskUpdateInfo = new AsyncTaskUpdateInfo();

        asyncTaskUpdateInfo.execute(instance);

        String respond = asyncTaskUpdateInfo.getRespond();

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
        AsyncTaskDelete asyncTaskDelete = new AsyncTaskDelete();

        asyncTaskDelete.execute(instance.username, instance.token);

        String respond = asyncTaskDelete.getRespond();

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

    public boolean createRestaurant(Restaurant restaurant)
    {
        addRestaurant(restaurant);

        AsyncTaskCreateRestaurant asyncTaskCreateRestaurant = new AsyncTaskCreateRestaurant();

        asyncTaskCreateRestaurant.execute(instance);

        String respond = asyncTaskCreateRestaurant.getRespond();

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
