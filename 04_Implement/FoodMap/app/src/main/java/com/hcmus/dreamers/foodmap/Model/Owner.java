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
    @SerializedName("phoneNumber")
    private String phoneNumber;
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

    public static void Login(String username, String password)
    {

        instance.username = username;
        instance.password = password;


        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();

                if(Sresponse != null) {
                    ResponseJSON parseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if(parseJSON.getCode() == 200) {
                        try {
                            instance = ParseJSON.parseOwnerFromCreateAccount(Sresponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("LOGINRESPONSE", parseJSON.getMessage());
                }
                else
                {
                    Log.i("LOGINRESPONSE", "null");
                }
            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.checkLogin(instance)));

    }

   public void changePassword(String newPassword)
    {
        final String pass_temp = instance.password;
        instance.password = newPassword;

        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();

                if (Sresponse != null) {
                    ResponseJSON parseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if (parseJSON.getCode() != 200) {
                        instance.password = pass_temp;
                    }
                    Log.i("CHANGE_PASS_RESPONSE", parseJSON.getMessage());
                }
                else
                {
                    Log.i("CHANGE_PASS_RESPONSE", "null");
                }
            }

        });

        taskRequest.execute(new DoingTask(GenerateRequest.updateAccount(instance, instance.token)));

    }

    public void updateInformation(String name, String phoneNumber, String email)
    {
        final String name_temp = instance.getName();
        final String phone_temp = instance.phoneNumber;
        final String email_temp = instance.getEmail();
        instance.setName(name);
        instance.phoneNumber = phoneNumber;
        instance.setEmail(email);


        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();
                if (Sresponse != null) {
                    ResponseJSON parseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if (parseJSON.getCode() != 200) {
                        instance.setName(name_temp);
                        instance.phoneNumber = phone_temp;
                        instance.setEmail(email_temp);
                    }
                    Log.i("UPDATE_INFO_RESPONSE", parseJSON.getMessage());
                }
                else
                {
                    Log.i("UPDATE_INFO_RESPONSE", "null");
                }
            }
        });

        taskRequest.execute(new DoingTask(GenerateRequest.updateAccount(instance, instance.token)));


    }

    public void deleteAcount()
    {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();
                if (Sresponse != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    Log.i("DELETE_ACCOUNT_RESPONSE", responseJSON.getMessage());
                }
                else{
                    Log.i("DELETE_ACCOUNT_RESPONSE", "null");
                }
            }
        });

        taskRequest.execute(new DoingTask(GenerateRequest.deleteAccount(instance.username,instance.token)));

    }

    public void createRestaurant(final Restaurant restaurant)
    {
        addRestaurant(restaurant);

        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();

                if (Sresponse != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if(responseJSON.getCode() != 200)
                    {
                        instance.listRestaurant.remove(restaurant);
                    }
                    Log.i("CREATE_REST_RESPONSE", responseJSON.getMessage());
                }
                else{
                    Log.i("CREATE_REST_RESPONSE", "null");
                }

            }
        });

        int index = instance.listRestaurant.size() - 1;
        taskRequest.execute(new DoingTask(GenerateRequest.createRestaurant(instance.listRestaurant.get(index), instance.token)));
    }

}
