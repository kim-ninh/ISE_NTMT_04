package com.hcmus.dreamers.foodmap.jsonapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hcmus.dreamers.foodmap.Model.Catalog;
import com.hcmus.dreamers.foodmap.Model.Comment;
import com.hcmus.dreamers.foodmap.Model.Owner;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.common.ResponseJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

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

    public static Owner parseOwnerFromCreateAccount(String response) throws JSONException {
        JSONObject object = new JSONObject(response);
        Owner owner = gson.fromJson(object.getString("data"), Owner.class);
        return owner;
    }

    public static List<CommentData> parseCommentData(String response) throws JSONException {
        List<CommentData> listComments = new ArrayList<>();
        JSONObject object = new JSONObject(response);
        JSONArray array = object.getJSONArray("data");
        int length = array.length();
        for(int i = 0; i < length; i++){
            JSONObject o = array.getJSONObject(i);
            CommentData commentData = gson.fromJson(o.toString(), CommentData.class);
            listComments.add(commentData);
        }
        return listComments;
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


//    public static List<Restaurant> parseRestaurant(String response) throws JSONException {
//        List<Restaurant> listRestaurants = new ArrayList<>();
//        JSONObject object = new JSONObject(response);
//        JSONArray array = object.getJSONArray("data");
//        int length = array.length();
//        for(int i = 0; i < length; i++){
//            JSONObject o = array.getJSONObject(i);
//            Restaurant rest = new Restaurant();
//            rest.setId(o.getInt("id"));
//            rest.set
//            rest.setLocation(new GeoPoint(o.getDouble("lat"), o.getDouble("lon")));
//            listRestaurants.add(rest);
//        }
//        return listRestaurants;
//    }


    public static String parseUrlImage(String response) throws JSONException {
        JSONObject object = new JSONObject(response);
        String url = object.getString("url");
        return url;
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
