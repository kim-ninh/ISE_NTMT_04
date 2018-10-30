package com.hcmus.dreamers.foodmap.common;

import android.util.Log;

import com.hcmus.dreamers.foodmap.AsyncTask.DoingTask;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskRequest;
import com.hcmus.dreamers.foodmap.Model.Owner;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;

import org.json.JSONException;

public class FoodMapApiManager {

    public static final int SUCCESS = 0;
    public static final int PARSE_FAIL = 1;
    public static final int FAIL_INFO = 2;
    public static final int REQUEST_FAIL = 3;

    private boolean isLogin(){
        if (Owner.getInstance().getToken() == null)
            return false;
        return true;
    }

    public static void Login(String username, String password, final TaskCompleteCallBack taskCompleteCallBack)
    {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();

                if(Sresponse != null) {
                    ResponseJSON parseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if(parseJSON.getCode() == ConstantCODE.SUCCESS) {
                        try {
                            Owner owner = Owner.getInstance();
                            owner = ParseJSON.parseOwnerFromCreateAccount(Sresponse);
                            taskCompleteCallBack.OnTaskComplete(SUCCESS);
                        } catch (JSONException e) {
                            taskCompleteCallBack.OnTaskComplete(PARSE_FAIL);
                        }
                    }
                    else
                    {
                        taskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                    }
                }
                else
                {
                    taskCompleteCallBack.OnTaskComplete(REQUEST_FAIL);
                }
            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.checkLogin(Owner.getInstance())));
    }

    public static void deleteAcount(final TaskCompleteCallBack taskCompleteCallBack)
    {
        Owner instance = Owner.getInstance();
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();
                if (Sresponse != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);
                    if (responseJSON.getCode() == ConstantCODE.SUCCESS)
                    {
                        taskCompleteCallBack.OnTaskComplete(SUCCESS);
                    }
                    else{
                        taskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                    }
                }
                else{
                    taskCompleteCallBack.OnTaskComplete(REQUEST_FAIL);
                }
            }
        });

        taskRequest.execute(new DoingTask(GenerateRequest.deleteAccount(instance.getUsername(), instance.getToken())));
    }

    public static void createRestaurant(final Restaurant restaurant,final TaskCompleteCallBack taskCompleteCallBack)
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

                    if(responseJSON.getCode() != ConstantCODE.SUCCESS)
                    {
                        Owner.getInstance().getListRestaurant().remove(restaurant);
                        taskCompleteCallBack.OnTaskComplete(SUCCESS);
                    }
                    else{
                        taskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                    }
                }
                else{
                    taskCompleteCallBack.OnTaskComplete(REQUEST_FAIL);
                }

            }
        });

        int index = instance.getListRestaurant().size() - 1;
        taskRequest.execute(new DoingTask(GenerateRequest.createRestaurant(instance.getListRestaurant().get(index), instance.getToken())));
    }

    public static void forgotPassword(String email, final TaskCompleteCallBack taskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();

                if (Sresponse != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if(responseJSON.getCode() != ConstantCODE.SUCCESS){
                        taskCompleteCallBack.OnTaskComplete(SUCCESS);
                    }
                    else {
                        taskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                    }
                }
                else{
                    taskCompleteCallBack.OnTaskComplete(REQUEST_FAIL);
                }

            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.resetPassword(email)));
    }

    // lấy dữ liệu từ api và lưu xuống database
    public static void getRestaurant(TaskCompleteCallBack onTaskCompleteCallBack){

    }

    // dành cho owner
    public static void getOffer(String id_rest, TaskCompleteCallBack onTaskCompleteCallBack){

    }

    // dành cho guest
    public static void getDiscount(String id_rest, TaskCompleteCallBack onTaskCompleteCallBack){

    }

}

