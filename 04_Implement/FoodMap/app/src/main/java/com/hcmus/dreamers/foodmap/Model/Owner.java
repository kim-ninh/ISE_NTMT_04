package com.hcmus.dreamers.foodmap.Model;

import android.util.Log;

import com.hcmus.dreamers.foodmap.AsyncTask.DoingTask;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskRequest;

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
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("url_image")
    private String urlImage;
    @SerializedName("token")
    private String token;

    private List<Restaurant> listRestaurant;


    private Owner() {
        super();
    }

    private Owner(String name, String email) {
        super(name, email);
    }

    private Owner(String name, String email, String phoneNumber) {
        super(name, email);
        this.phoneNumber = phoneNumber;
        listRestaurant = new ArrayList<Restaurant>();
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
        if (listRestaurant == null || listRestaurant.size() == 0)
            return null;
        return listRestaurant.get(index);
    }

    public void addRestaurant(Restaurant restaurant) {
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

    public void changePassword(String newPassword)
    {
        final String pass_temp = instance.getPassword();
        instance.setPassword(newPassword);

        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();

                if (Sresponse != null) {
                    ResponseJSON parseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if (parseJSON.getCode() != 200) {
                        Owner.getInstance().setPassword(pass_temp);
                    }
                    Log.i("CHANGE_PASS_RESPONSE", parseJSON.getMessage());
                }
                else
                {
                    Log.i("CHANGE_PASS_RESPONSE", "null");
                }
            }

        });

        taskRequest.execute(new DoingTask(GenerateRequest.updateAccount(instance)));
    }

    public void updateInformation(String name, String phoneNumber, String email)
    {
        final String name_temp = instance.getName();
        final String phone_temp = instance.getPhoneNumber();
        final String email_temp = instance.getEmail();
        instance.setName(name);
        instance.setPhoneNumber(phoneNumber);
        instance.setEmail(email);


        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response){
                String Sresponse = response.toString();
                if (Sresponse != null) {
                    ResponseJSON parseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if (parseJSON.getCode() != 200) {
                        Owner.getInstance().setName(name_temp);
                        Owner.getInstance().setPhoneNumber(phone_temp);
                        Owner.getInstance().setEmail(email_temp);
                    }
                    Log.i("UPDATE_INFO_RESPONSE", parseJSON.getMessage());
                }
                else
                {
                    Log.i("UPDATE_INFO_RESPONSE", "null");
                }
            }
        });

        taskRequest.execute(new DoingTask(GenerateRequest.updateAccount(instance)));
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
