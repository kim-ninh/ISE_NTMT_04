package com.hcmus.dreamers.foodmap_admin.common;

import com.hcmus.dreamers.foodmap_admin.define.ConstantURL;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class GenerateRequest {

    public static okhttp3.Request checkLogin(String username, String password) { //if login is successful then result is not equals null
        String url = ConstantURL.BASEURL + ConstantURL.LOGIN;
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request getPreRestaurant(String username, String password) { //if login is successful then result is not equals null
        String url = ConstantURL.BASEURL + ConstantURL.PRERESTAURANT;
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request checkOKRestaurnat(String username, String password, int id_rest) { //if login is successful then result is not equals null
        String url = ConstantURL.BASEURL + ConstantURL.ProcessRestaurant;
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("id_rest", String.valueOf(id_rest));
        params.put("status", "0");
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

    public static okhttp3.Request checkFailRestaurant(String username, String password, int id_rest, String note) { //if login is successful then result is not equals null
        String url = ConstantURL.BASEURL + ConstantURL.ProcessRestaurant;
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("id_rest", String.valueOf(id_rest));
        params.put("status", "1");
        params.put("note", note);
        RequestBody bodyRequest = Utils.buildParameter(params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(bodyRequest)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        return request;
    }

}
