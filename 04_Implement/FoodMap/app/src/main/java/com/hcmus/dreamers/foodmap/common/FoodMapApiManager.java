package com.hcmus.dreamers.foodmap.common;

import com.hcmus.dreamers.foodmap.AsyncTask.DoingTask;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskRequest;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.Model.Guest;
import com.hcmus.dreamers.foodmap.Model.Owner;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;

import org.json.JSONException;

import java.text.ParseException;

public class FoodMapApiManager {

    public static final int SUCCESS = 0;
    public static final int PARSE_FAIL = 1;
    public static final int FAIL_INFO = 2;

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
                            Owner.setInstance(ParseJSON.parseOwnerFromCreateAccount(Sresponse));
                            taskCompleteCallBack.OnTaskComplete(SUCCESS);
                        } catch (JSONException e) {
                            taskCompleteCallBack.OnTaskComplete(PARSE_FAIL);
                        }
                    }
                    else if (parseJSON.getCode() == ConstantCODE.NOTFOUND) {
                        taskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                    }
                    else if (parseJSON.getCode() == ConstantCODE.NOTINTERNET){
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                    }
                }
            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.checkLogin(username, password)));
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
                    else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
                        taskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTINTERNET){
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                    }
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

                    if(responseJSON.getCode() == ConstantCODE.SUCCESS)
                    {
                        Owner.getInstance().getListRestaurant().remove(restaurant);
                        taskCompleteCallBack.OnTaskComplete(SUCCESS);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
                        taskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTINTERNET){
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                    }
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

                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){
                        taskCompleteCallBack.OnTaskComplete(SUCCESS);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
                        taskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTINTERNET){
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                    }
                }
            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.resetPassword(email)));
    }

    public static void checkCode(String email, String code, final TaskCompleteCallBack taskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();

                if (Sresponse != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){
                        try {
                            Owner.setInstance(ParseJSON.parseOwnerFromCreateAccount(Sresponse));
                            taskCompleteCallBack.OnTaskComplete(SUCCESS);
                        } catch (JSONException e) {
                            taskCompleteCallBack.OnTaskComplete(PARSE_FAIL);
                        }
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
                        taskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTINTERNET){
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                    }
                }
            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.checkCode(email, code)));
    }

    public static void updateAccount(final Owner owner, final TaskCompleteCallBack taskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String data = response.toString();
                if (data != null){
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(data);
                    if (responseJSON.getCode() == ConstantCODE.SUCCESS){
                        taskCompleteCallBack.OnTaskComplete(SUCCESS);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
                        taskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTINTERNET){
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                    }
                }
            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.updateAccount(owner)));
    }

    public static void createAccount(String username, String password, String name, String phoneNumber, String email, final TaskCompleteCallBack taskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();

                if (Sresponse != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){
                        taskCompleteCallBack.OnTaskComplete(SUCCESS);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
                        taskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTINTERNET){
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                    }
                }
            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.createAccount(username,password,name, phoneNumber, email)));
    }

    public static void addGuest(Guest guest, final TaskCompleteCallBack taskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();

                if (Sresponse != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){
                        taskCompleteCallBack.OnTaskComplete(SUCCESS);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
                        taskCompleteCallBack.OnTaskComplete(SUCCESS); // trường hợp đã tồn tại
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTINTERNET){
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                    }
                }
                else{
                    taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                }

            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.addGuest(guest)));
    }



    public static void deleteDish(int id_rest, final String dishName, final TaskCompleteCallBack taskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String resp = response.toString();

                if (resp != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(resp);
                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){
                        taskCompleteCallBack.OnTaskComplete(SUCCESS);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTFOUND); // not found on database
                        taskCompleteCallBack.OnTaskComplete(FAIL_INFO); // trường hợp đã tồn tại
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTINTERNET){
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                    }
                }
                else{
                    taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                }

            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.deleteDish(id_rest, dishName, Owner.getInstance().getToken())));
    }
    public static void addFavorite(String guest_email, int id_rest, final TaskCompleteCallBack taskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();

                if (Sresponse != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){
                        taskCompleteCallBack.OnTaskComplete(SUCCESS);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTFOUND); // not found on database
                        taskCompleteCallBack.OnTaskComplete(FAIL_INFO); // trường hợp đã tồn tại
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTINTERNET){
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                    }
                }
                else{
                    taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                }

            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.addFavorite(id_rest, guest_email)));
    }


    public static void updateDish(int id_rest, final Dish dish, final TaskCompleteCallBack taskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String resp = response.toString();

                if (resp != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(resp);
                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){
                        taskCompleteCallBack.OnTaskComplete(SUCCESS);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTFOUND); // not found on database
                        taskCompleteCallBack.OnTaskComplete(FAIL_INFO); // trường hợp đã tồn tại
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTINTERNET){
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                    }
                }
                else{
                    taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                }

            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.updateDish(id_rest, dish, Owner.getInstance().getToken())));
    }
    public static void deleteFavorite(String guest_email, int id_rest, final TaskCompleteCallBack taskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();

                if (Sresponse != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){
                        taskCompleteCallBack.OnTaskComplete(SUCCESS);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTFOUND); // not found on database
                        taskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTINTERNET){
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                    }
                }
                else{
                    taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                }

            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.deleteFavorite(id_rest, guest_email)));
    }

    public static void getFavorite(String guest_email, final TaskCompleteCallBack taskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();

                if (Sresponse != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){
                        try {
                            Guest.getInstance().setFavRestaurant(ParseJSON.parseFavorite(Sresponse));
                            taskCompleteCallBack.OnTaskComplete(SUCCESS);
                        } catch (JSONException e) {
                            taskCompleteCallBack.OnTaskComplete(PARSE_FAIL);
                            e.printStackTrace();
                        } catch (ParseException e) {
                            taskCompleteCallBack.OnTaskComplete(PARSE_FAIL);
                            e.printStackTrace();
                        }
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
                        taskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTINTERNET){
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                    }
                }
                else{
                    taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                }
            }
        });

        taskRequest.execute(new DoingTask(GenerateRequest.getFavorite(guest_email)));
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

