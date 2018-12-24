package com.hcmus.dreamers.foodmap_admin.json;


import com.google.gson.Gson;
import com.hcmus.dreamers.foodmap_admin.common.ResponseJSON;
import com.hcmus.dreamers.foodmap_admin.model.Catalog;
import com.hcmus.dreamers.foodmap_admin.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ParseJSON {
    private static Gson gson = new Gson();

    public static ResponseJSON fromStringToResponeJSON(String data){
        return gson.fromJson(data, ResponseJSON.class);
    }


    public static ResponseJSON parseFromAllResponse(String response) throws JSONException {// use to get ResponseJSON from all response
        JSONObject object = new JSONObject(response);
        ResponseJSON responseJSON = new ResponseJSON(Integer.parseInt(object.getString("status")), object.getString("message"));
        return responseJSON;
    }

    public static String getTokenFromCreateAccount(String response) throws JSONException {
        JSONObject object = new JSONObject(response);
        JSONObject data = object.getJSONObject("data");
        String token = data.getString("token");
        return token;
    }


    public static List<Restaurant> parseLocation(String response) throws JSONException {
        List<Restaurant> listRestaurants = new ArrayList<>();
        JSONObject object = new JSONObject(response);
        JSONArray array = object.getJSONArray("data");
        int length = array.length();
        for(int i = 0; i < length; i++){
            JSONObject o = array.getJSONObject(i);
            Restaurant rest = new Restaurant();
            rest.setId(o.getInt("id_rest"));
            rest.setLocation(new GeoPoint(o.getDouble("lat"), o.getDouble("lon")));
            listRestaurants.add(rest);
        }
        return listRestaurants;
    }


    public static List<Restaurant> parseRestaurant(String response) throws JSONException, ParseException {
        List<Restaurant> listRestaurants = new ArrayList<>();
        JSONObject object = new JSONObject(response);
        JSONArray array = object.getJSONArray("data");
        int length = array.length();
        for(int i = 0; i < length; i++){
            JSONObject o = array.getJSONObject(i);
            Restaurant rest = new Restaurant();
            rest.setId(o.getInt("id"));
            rest.setOwnerUsername((o.getString("owner_username")));

            rest.setName(o.getString("name"));
            rest.setAddress(o.getString("address"));
            rest.setPhoneNumber(o.getString("phone_number"));
            rest.setDescription(o.getString("describe_text"));
            rest.setUrlImage(o.getString("url_image"));
            rest.setTimeOpen(new SimpleDateFormat("HH:mm:ss").parse(o.getString("time_open")));
            rest.setTimeClose(new SimpleDateFormat("HH:mm:ss").parse(o.getString("time_close")));
            JSONObject locate = o.getJSONObject("location");
            rest.setLocation(new GeoPoint(locate.getDouble("lat"), locate.getDouble("lon")));
            listRestaurants.add(rest);
        }
        return listRestaurants;
    }


    public static String parseUrlImage(String response) throws JSONException {
        JSONObject object = new JSONObject(response);
        String url = object.getString("url");
        return url;
    }

    public static int parseIdRestaurant(String response) throws JSONException {
        JSONObject object = new JSONObject(response);
        int id_rest = object.getInt("id");
        return id_rest;
    }

    public static List<Catalog> parseCatalog(String response) throws JSONException {
        List<Catalog>  listCatalogs = new ArrayList<>();
        JSONObject object = new JSONObject(response);
        JSONArray array = object.getJSONArray("data");
        int length = array.length();
        for(int i=0;i<length;i++){
            JSONObject o = array.getJSONObject(i);
            Catalog catalog = gson.fromJson(o.toString(), Catalog.class);
            listCatalogs.add(catalog);
        }
        return listCatalogs;
    }


}
