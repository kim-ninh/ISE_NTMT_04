package com.hcmus.dreamers.foodmap.common;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.facebook.AccessToken;
import com.hcmus.dreamers.foodmap.AsyncTask.DoingTask;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskCompleteCallBack;
import com.hcmus.dreamers.foodmap.AsyncTask.TaskRequest;
import com.hcmus.dreamers.foodmap.EditDishActivity;
import com.hcmus.dreamers.foodmap.Model.Comment;
import com.hcmus.dreamers.foodmap.Model.DetailAddress;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.Model.Guest;
import com.hcmus.dreamers.foodmap.Model.Offer;
import com.hcmus.dreamers.foodmap.Model.Owner;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.database.FoodMapManager;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;
import com.hcmus.dreamers.foodmap.jsonapi.ParseJSON;

import org.json.JSONException;

import java.io.File;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.List;

public class FoodMapApiManager {

    public static final int SUCCESS = -1;
    public static final int PARSE_FAIL = -2;
    public static final int FAIL_INFO = -3;

    public static boolean isLogin(){
        if (Owner.getInstance().getToken() == null)
            return false;
        return true;
    }


    public static boolean isGuestLogin(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        return isLoggedIn;
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
                            Owner.getInstance().setlistRestaurant(FoodMapManager.getRestaurants(Owner.getInstance().getUsername()));

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
        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();

                if (Sresponse != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if(responseJSON.getCode() == ConstantCODE.SUCCESS)
                    {
                        try {
                            int id = ParseJSON.parseIdRestaurant(Sresponse);
                            taskCompleteCallBack.OnTaskComplete(id);
                        } catch (JSONException e) {
                            taskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                        }

                    }
                    else {
                        taskCompleteCallBack.OnTaskComplete(FAIL_INFO);
                    }
                }
                else{
                    taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                }
            }
        });

        taskRequest.execute(new DoingTask(GenerateRequest.createRestaurant(restaurant, Owner.getInstance().getToken())));
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
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.SUCCESS);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTFOUND); // not found on database
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


    public static void addComment(int id_rest, Comment comment, String token, final TaskCompleteCallBack taskCompleteCallBack){
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
        taskRequest.execute(new DoingTask(GenerateRequest.comment(id_rest, comment, token)));
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
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.SUCCESS);
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


    public static void deleteRestaurant(final Restaurant restaurant, final TaskCompleteCallBack taskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String resp = response.toString();

                if (resp != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(resp);
                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){
                        Owner.getInstance().getListRestaurant().remove(restaurant);
                        taskCompleteCallBack.OnTaskComplete(SUCCESS);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
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
        taskRequest.execute(new DoingTask(GenerateRequest.deleteRestaurant(restaurant.getId(), Owner.getInstance().getToken())));
    }

    public static void updateRestaurant(final Restaurant rest, final TaskCompleteCallBack taskCompleteCallBack){
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
        taskRequest.execute(new DoingTask(GenerateRequest.updateRestaurant(rest, Owner.getInstance().getToken())));
    }

    //
    public static void getDetailAddressFromString(final String address, final TaskCompleteCallBack taskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String resp = response.toString();
                if (resp != null)
                {
                    try {
                        List<DetailAddress> detailAddresses = new ArrayList<DetailAddress>();
                        detailAddresses.clear();
                        detailAddresses.addAll(ParseJSON.parseDetailAddress(resp));

                        taskCompleteCallBack.OnTaskComplete(detailAddresses);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        taskCompleteCallBack.OnTaskComplete(null);
                    }
                }
                else{
                    taskCompleteCallBack.OnTaskComplete(null);
                }
            }
        });
        taskRequest.execute(new DoingTask(GenerateRequest.getAddressFromString(address)));
    }

    public static void uploadImage(Context context, int id_rest, String name, String url, final TaskCompleteCallBack taskCompleteCallBack){

        try {
            String convert = Base64Converter.binary2Base64(context, url);

            TaskRequest taskRequest = new TaskRequest();
            taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
                @Override
                public void OnTaskComplete(Object response) {
                    String sResponse = response.toString();
                    if (response != null){
                        ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(sResponse);
                        if (responseJSON.getCode() == ConstantCODE.SUCCESS){
                            try {
                                String urlImage = ParseJSON.parseUrlImage(sResponse);
                                taskCompleteCallBack.OnTaskComplete(urlImage);
                            } catch (JSONException e) {
                                taskCompleteCallBack.OnTaskComplete(null);
                            }
                        }
                        else{
                            taskCompleteCallBack.OnTaskComplete(null);
                        }
                    }
                    else{
                        taskCompleteCallBack.OnTaskComplete(null);
                    }
                }
            });
            taskRequest.execute(new DoingTask(GenerateRequest.upload(id_rest, name, convert)));

        } catch (Exception e) {
            e.printStackTrace();
            taskCompleteCallBack.OnTaskComplete(null);
        }
    }

    // lấy dữ liệu từ api và lưu xuống database
    public static void getRestaurant(final Context context, final TaskCompleteCallBack taskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();

                if (Sresponse != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){
                        try {
                            FoodMapManager.setRestaurants(context, ParseJSON.parseRestaurant(Sresponse));
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
        taskRequest.execute(new DoingTask(GenerateRequest.getRestaurant()));
    }

    // lấy danh sách catalog
    public static void getCatalog(final Context context, final TaskCompleteCallBack taskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String Sresponse = response.toString();

                if (Sresponse != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(Sresponse);

                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){
                        try {
                            FoodMapManager.setCatalogs(context, ParseJSON.parseCatalog(Sresponse));
                            taskCompleteCallBack.OnTaskComplete(SUCCESS);
                        } catch (JSONException e) {
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
        taskRequest.execute(new DoingTask(GenerateRequest.getCatalog()));
    }

    // dành cho guest
    public static void getDiscount(String id_rest, TaskCompleteCallBack onTaskCompleteCallBack){

    }


    public static void uploadImage(int restID,
                                   String imageName,
                                   String base64Data,
                                   final TaskCompleteCallBack taskCompleteCallBack){

        TaskRequest uploadingTask = new TaskRequest();

        // Implement call back
        uploadingTask.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String jsonResponseString = response.toString();

                if (jsonResponseString != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(jsonResponseString);

                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){

                        // Get the public URL and send back to caller
                        String publicImageURL = "";

                        try{
                            publicImageURL = ParseJSON.parseUrlImage(jsonResponseString);
                        }catch (Exception e)
                        {
                            // This line should never be run
                            Log.d("ParseJSON", e.getMessage());
                        }

                        // Send back to caller
                        taskCompleteCallBack.OnTaskComplete(publicImageURL);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
                        taskCompleteCallBack.OnTaskComplete("NOT FOUND");
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTINTERNET){
                        taskCompleteCallBack.OnTaskComplete("NO INTERNET");
                    }
                }
                else{
                    taskCompleteCallBack.OnTaskComplete("NO INTERNET");
                }
            }
        });

        uploadingTask.execute(new DoingTask(GenerateRequest.upload(restID, imageName, base64Data)));
    }

    public static void uploadImage(Context context,
                                   int restID,
                                   Uri imageUri,
                                   final TaskCompleteCallBack taskCompleteCallBack)
    {
        File imageFile = new File(imageUri.getPath());
        String encodedData = "";

        // Mã hóa hình theo base64
        try
        {
            encodedData = Base64Converter.encodeToBase64(context, imageUri);
        }catch (Exception e)
        {
            Log.d("ConvertBase64",e.getMessage());
        }

        uploadImage(restID, imageFile.getName(),encodedData,taskCompleteCallBack);
    }

    public static void deleteImage(String imageURL, final TaskCompleteCallBack taskCompleteCallBack)
    {
        TaskRequest deletingTask = new TaskRequest();

        deletingTask.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String jsonResponseString = response.toString();

                if (jsonResponseString != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(jsonResponseString);

                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.SUCCESS);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTFOUND);
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

        deletingTask.execute(new DoingTask(GenerateRequest.deletePicture(imageURL)));
    }



    public static void getOffer(int id_rest, final TaskCompleteCallBack taskCompleteCallBack)
    {
        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                taskCompleteCallBack.OnTaskComplete(response.toString());
            }
        });

        taskRequest.execute(new DoingTask(GenerateRequest.getOffer(id_rest)));
    }


    public static void addDish(int id_rest, Dish dish, final TaskCompleteCallBack taskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String jsonResponseString = response.toString();

                if (jsonResponseString != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(jsonResponseString);

                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){
                        taskCompleteCallBack.OnTaskComplete(SUCCESS);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTINTERNET){
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                    }
                    else {
                        taskCompleteCallBack.OnTaskComplete(PARSE_FAIL);
                    }
                }
                else{
                    taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                }
            }
        });

        taskRequest.execute(new DoingTask(GenerateRequest.addDish(id_rest, dish, Owner.getInstance().getToken())));
    }

    public static void addRank(String guestEmail, int restID, int star, final TaskCompleteCallBack taskCompleteCallBack)
    {
        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String JsonObjectString = response.toString();

                if (JsonObjectString != null) {
                    try {
                        ResponseJSON responseJSON = ParseJSON.parseFromAllResponse(response.toString());

                        if(responseJSON.getCode() == ConstantCODE.SUCCESS){
                            taskCompleteCallBack.OnTaskComplete(ConstantCODE.SUCCESS);
                        }
                        else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
                            taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTFOUND);
                        }
                        else if (responseJSON.getCode() == ConstantCODE.INVALIDREQUEST) {
                            taskCompleteCallBack.OnTaskComplete(ConstantCODE.INVALIDREQUEST);
                        }
                        else if (responseJSON.getCode() == ConstantCODE.NOTINTERNET){
                            taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    taskCompleteCallBack.OnTaskComplete(ConstantCODE.NOTINTERNET);
                }
            }
        });

        taskRequest.execute(new DoingTask(GenerateRequest.addRank(guestEmail, restID, star)));
    }

    public static void addCheckin(int id_rest, String guest_email, final TaskCompleteCallBack taskCompleteCallBack){
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
        taskRequest.execute(new DoingTask(GenerateRequest.addCheckin(id_rest, guest_email)));
    }


    public static void addOrder(final Offer offer, int id_discount, final TaskCompleteCallBack taskCompleteCallBack){
        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setOnCompleteCallBack(new TaskCompleteCallBack() {
            @Override
            public void OnTaskComplete(Object response) {
                String resp = response.toString();

                if (resp != null) {
                    ResponseJSON responseJSON = ParseJSON.fromStringToResponeJSON(resp);
                    if(responseJSON.getCode() == ConstantCODE.SUCCESS){
                        taskCompleteCallBack.OnTaskComplete(ConstantCODE.SUCCESS);
                    }
                    else if (responseJSON.getCode() == ConstantCODE.NOTFOUND) {
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
        taskRequest.execute(new DoingTask(GenerateRequest.addOffer(offer.getGuestEmail(), offer.getTotal(), id_discount)));
    }


}

