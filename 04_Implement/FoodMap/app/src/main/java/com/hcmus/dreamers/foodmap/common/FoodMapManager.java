package com.hcmus.dreamers.foodmap.common;

import android.util.Log;

import com.hcmus.dreamers.foodmap.AsyncTask.DoingTask;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskRequest;
import com.hcmus.dreamers.foodmap.Model.Comment;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.Model.Owner;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;

import org.json.JSONException;

public class FoodMapManager {
    private boolean isLogin(){
        if (Owner.getInstance().getToken() == null)
            return false;
        return true;
    }

    public static void Login(String username, String password)
    {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();

                if(Sresponse != null) {
                    ResponseJSON parseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if(parseJSON.getCode() == 200) {
                        try {
                            Owner owner = Owner.getInstance();
                            owner = ParseJSON.parseOwnerFromCreateAccount(Sresponse);
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
        taskRequest.execute(new DoingTask(GenerateRequest.checkLogin(Owner.getInstance())));
    }

    public static void deleteAcount()
    {
        Owner instance = Owner.getInstance();
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

        taskRequest.execute(new DoingTask(GenerateRequest.deleteAccount(instance.getUsername(), instance.getToken())));
    }

    public static void createRestaurant(final Restaurant restaurant)
    {
        Owner instance = Owner.getInstance();
        instance.addRestaurant(restaurant);

        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();

                if (Sresponse != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if(responseJSON.getCode() != 200)
                    {
                        Owner.getInstance().getListRestaurant().remove(restaurant);
                    }
                    Log.i("CREATE_REST_RESPONSE", responseJSON.getMessage());
                }
                else{
                    Log.i("CREATE_REST_RESPONSE", "null");
                }

            }
        });

        int index = instance.getListRestaurant().size() - 1;
        taskRequest.execute(new DoingTask(GenerateRequest.createRestaurant(instance.getListRestaurant().get(index), instance.getToken())));
    }

    public static void forgotPassword(){

    }

    // lấy dữ liệu từ api và lưu xuống database
    public static void getRestaurant(TaskCompleteCallBack onTaskCompleteCallBack){

    }

    // dành cho owner
    public static void getOffer(String id_rest){

    }

    // dành cho guest
    public static void getDiscount(String id_rest){

    }

}

