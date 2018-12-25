package com.hcmus.dreamers.foodmap_admin.common;

import com.hcmus.dreamers.foodmap_admin.asynctask.DoingTask;
import com.hcmus.dreamers.foodmap_admin.asynctask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap_admin.asynctask.TaskRequest;
import com.hcmus.dreamers.foodmap_admin.define.ConstantCODE;
import com.hcmus.dreamers.foodmap_admin.json.ParseJSON;
import com.hcmus.dreamers.foodmap_admin.model.Admin;

import org.json.JSONException;

import java.text.ParseException;

public class FoodMapApi {
    public static final int SUCCESS = -1;
    public static final int FAIL_INFO = -2;

    public static void login(String username, String password, final TaskCompleteCallBack onTaskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String strResponse = (String)response;
                ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(strResponse);
                if (responseJSON.getCode() == 200){
                    onTaskCompleteCallBack.OnTaskComplete(SUCCESS);
                }
                else if (responseJSON.getCode() == 404){
                    onTaskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                }
                else {
                    onTaskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                }
            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.checkLogin(username, password)));
    }


    public static void getPreRestaurant(String username, String password, final TaskCompleteCallBack onTaskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String strResponse = (String)response;
                ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(strResponse);
                if (responseJSON.getCode() == 200){
                    try {
                        Admin.getInstance().getRestaurantList().clear();
                        Admin.getInstance().getRestaurantList().addAll(ParseJSON.parseRestaurant(strResponse));
                        onTaskCompleteCallBack.OnTaskComplete(SUCCESS);
                    } catch (JSONException e) {
                        onTaskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                    } catch (ParseException e) {
                        onTaskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                    }
                }
                else if (responseJSON.getCode() == 404){
                    onTaskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                }
                else {
                    onTaskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                }
            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.getPreRestaurant(username, password)));
    }

    public static void checkOKRestaurant(String username, String password, int id_rest, final TaskCompleteCallBack onTaskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String strResponse = (String)response;
                ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(strResponse);
                if (responseJSON.getCode() == 200){
                    onTaskCompleteCallBack.OnTaskComplete(SUCCESS);
                }
                else if (responseJSON.getCode() == 404){
                    onTaskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                }
                else {
                    onTaskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                }
            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.checkOKRestaurnat(username, password, id_rest)));
    }

    public static void checkFailRestaurant(String username, String password, int id_rest, String note, final TaskCompleteCallBack onTaskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String strResponse = (String)response;
                ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(strResponse);
                if (responseJSON.getCode() == 200){
                    onTaskCompleteCallBack.OnTaskComplete(SUCCESS);
                }
                else if (responseJSON.getCode() == 404){
                    onTaskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                }
                else {
                    onTaskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                }
            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.checkFailRestaurant(username, password, id_rest, note)));
    }

}
