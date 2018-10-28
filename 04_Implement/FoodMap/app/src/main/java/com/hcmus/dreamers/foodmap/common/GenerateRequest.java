package com.hcmus.dreamers.foodmap.common;

import android.content.Context;
import android.location.Location;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hcmus.dreamers.foodmap.Model.Comment;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.Model.Owner;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.define.ConstantURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class GenerateRequest {

    public static okhttp3.Request checkLogin(final Owner owner){ //if login is successful then result is not equals null

        String url = ConstantURL.BASEURL + ConstantURL.LOGIN;
        Map<String, String> params = new HashMap<>();
        params.put("username", owner.getUsername());
        params.put("password", owner.getPassword());
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request createAccount(final Owner owner) throws IOException {
        String url = ConstantURL.BASEURL + ConstantURL.CREATEACCOUNT;
        Map<String, String> params = new HashMap<>();
        params.put("username", owner.getUsername());
        params.put("password", owner.getPassword());
        params.put("name", owner.getName());
        params.put("phone_number", owner.getPhoneNumber());
        params.put("email", owner.getEmail());
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;

    }

    public static okhttp3.Request comment(final String id_rest, final Comment comment, final String token){
        String url = ConstantURL.BASEURL + ConstantURL.COMMENT;
        Map<String, String> params = new HashMap<>();
        params.put("id_rest", String.valueOf(id_rest));
        params.put("comment", comment.getComment());
        if(!comment.getEmailGuest().equals(""))
            params.put("guest_email", comment.getEmailGuest());
        else
            params.put("owner_email", comment.getEmailOwner());
        params.put("token", token);
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request createDish(final int id_rest, final Dish dish, final String token){
        String url = ConstantURL.BASEURL + ConstantURL.CREATEDISH;
        Map<String, String> params = new HashMap<>();
        params.put("id_rest", String.valueOf(id_rest));
        params.put("name", dish.getName());
        params.put("price", String.valueOf(dish.getPrice()));
        params.put("url_image", dish.getUrlImage());
        params.put("id_catalog", String.valueOf(dish.getCatalog().getId()));
        params.put("token", token);
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request createRestaurant(final Restaurant restaurant,final String token){
        String url = ConstantURL.BASEURL + ConstantURL.CREATERESTAURANT;
        Map<String, String> params = new HashMap<>();
        params.put("owner_username", restaurant.getId_user());
        params.put("name", restaurant.getName());
        params.put("address", restaurant.getAddress());
        params.put("phone_number", restaurant.getPhoneNumber());
        params.put("describe_text", restaurant.getDescription());
        params.put("timeopen", transferDateToTime(restaurant.getTimeOpen()));
        params.put("timeclose", transferDateToTime(restaurant.getTimeClose()));
        params.put("lat", String.valueOf(restaurant.getLocation().getLatitude()));
        params.put("lon", String.valueOf(restaurant.getLocation().getLongitude()));
        params.put("token", token);
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request deleteDish(final int id_rest, final String name, final String token){
        String url = ConstantURL.BASEURL + ConstantURL.DELETEDISH;
        Map<String, String> params = new HashMap<>();
        params.put("id_rest", String.valueOf(id_rest));
        params.put("name", name);
        params.put("token", token);
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request deleteAccount(final String username, final String token){
        String url = ConstantURL.BASEURL + ConstantURL.DELETEACCOUNT;
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("token", token);
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request updateAccount(final Owner owner, final String token){
        String url = ConstantURL.BASEURL + ConstantURL.UPDATEACCOUNT;
        Map<String, String> params = new HashMap<>();
        params.put("username", owner.getUsername());
        params.put("token", token);
        params.put("password", owner.getPassword());
        params.put("name", owner.getName());
        params.put("phone_number", owner.getPhoneNumber());
        params.put("email", owner.getEmail());
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request updateDish(final int id_rest, final Dish dish, final String token){
        String url = ConstantURL.BASEURL + ConstantURL.UPDATEDISH;
        Map<String, String> params = new HashMap<>();
        params.put("id_rest", String.valueOf(id_rest));
        params.put("name", dish.getName());
        params.put("token", token);
        params.put("price", String.valueOf(dish.getPrice()));
        params.put("url_image", dish.getUrlImage());
        params.put("id_catalog", String.valueOf(dish.getCatalog().getId()));
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request updateLocation(final int id_rest, final Location location, final String token){

        String url = ConstantURL.BASEURL + ConstantURL.UPDATELOCAION;
        Map<String, String> params = new HashMap<>();
        params.put("id_rest", String.valueOf(id_rest));
        params.put("lat", String.valueOf(location.getLatitude()));
        params.put("lon", String.valueOf(location.getLongitude()));
        params.put("token", token);
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    private static String transferDateToTime(Date date){
        String result = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
        return result;
    }

    public static okhttp3.Request updateRestaurant(final Restaurant restaurant, final String token){
        String url = ConstantURL.BASEURL + ConstantURL.UPDATERESTAURANT;
        Map<String, String> params = new HashMap<>();
        params.put("id_rest", String.valueOf(restaurant.getId()));
        params.put("token", token);
        params.put("name", restaurant.getName());
        params.put("address", restaurant.getAddress());
        params.put("phone_number", restaurant.getPhoneNumber());
        params.put("describe_text", restaurant.getDescription());
        params.put("timeopen", transferDateToTime(restaurant.getTimeOpen()) );
        params.put("timeclose", transferDateToTime(restaurant.getTimeClose()));
        params.put("lat", String.valueOf(restaurant.getLocation().getLatitude()));
        params.put("lon", String.valueOf(restaurant.getLocation().getLongitude()));
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request upload(final int id_rest, final String name, final String data){
        String url = ConstantURL.BASEURL + ConstantURL.UPLOAD;
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("data", data);
        params.put("id", String.valueOf(id_rest));
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request deletePicture(final String urlImage, Context context){
        String url = ConstantURL.BASEURL + ConstantURL.DELETEPICTURE;
        Map<String, String> params = new HashMap<>();
        params.put("url", urlImage);
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request resetPassword(final String email){
        String url = ConstantURL.BASEURL + ConstantURL.RESETPASSWORD;
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request checkCode(final String email, final String codeCheck){
        String url = ConstantURL.BASEURL + ConstantURL.CHECKCODE;
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("code", codeCheck);
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request getComment(final int id_rest){
        String baseUrl = ConstantURL.BASEURL + ConstantURL.GETCOMMENT;
        Map<String, String> params = new HashMap<>();
        params.put("id_rest", String.valueOf(id_rest));
        String url = Utils.buildUrl(baseUrl, params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }


    public static okhttp3.Request getLocation(final int id_rest){
        String baseUrl = ConstantURL.BASEURL + ConstantURL.GETLOCATION;
        Map<String, String> params = new HashMap<>();
        params.put("id_rest", String.valueOf(id_rest));
        String url = Utils.buildUrl(baseUrl, params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request getRestaurant(){
        String baseUrl = ConstantURL.BASEURL + ConstantURL.GETRESTAURANT;
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(baseUrl)
                .get()
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request getCatalog(){
        String baseUrl = ConstantURL.BASEURL + ConstantURL.GETCATALOG;
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(baseUrl)
                .build();
        return request;
    }

}
