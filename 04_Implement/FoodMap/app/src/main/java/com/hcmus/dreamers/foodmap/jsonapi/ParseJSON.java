package com.hcmus.dreamers.foodmap.jsonapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hcmus.dreamers.foodmap.Model.Catalog;
import com.hcmus.dreamers.foodmap.Model.Comment;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.Model.Guest;
import com.hcmus.dreamers.foodmap.Model.Owner;
import com.hcmus.dreamers.foodmap.Model.Restaurant;
import com.hcmus.dreamers.foodmap.Model.User;
import com.hcmus.dreamers.foodmap.common.ResponseJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
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

    public static List<Comment> parseComment(String response) throws JSONException, ParseException {
        List<Comment> listComments = new ArrayList<>();
        JSONObject object = new JSONObject(response);
        JSONArray array = object.getJSONArray("data");
        int length = array.length();
        for(int i = 0; i < length; i++){
            JSONObject o = array.getJSONObject(i);
            Comment comment = new Comment();
            comment.setDateTime(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(o.getString("date_time")));
            comment.setEmailGuest(o.getString("guest_email"));
            comment.setEmailOwner(o.getString("owner_email"));
            comment.setComment(o.getString("comment"));
            listComments.add(comment);
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


    private static HashMap<String, Integer> parseRanks(JSONArray array) throws JSONException {
        HashMap<String, Integer> hashMap = new HashMap<>();
        int length = array.length();
        for(int i = 0; i < length; i++){
            JSONObject o = array.getJSONObject(i);
            hashMap.put(o.getString("email"),o.getInt("star"));
        }
        return hashMap;
    }


    private static List<Comment> parseComment(JSONArray array) throws JSONException, ParseException {
        List<Comment> list = new ArrayList<>();
        int length = array.length();
        for(int i=0;i<length;i++){
            JSONObject o = array.getJSONObject(i);
            Comment comment = new Comment();
            comment.setDateTime(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(o.getString("date_time")));
            comment.setEmailGuest(o.getString("guest_email"));
            comment.setEmailOwner(o.getString("owner_email"));
            comment.setComment(o.getString("comment"));
            list.add(comment);
        }
        return list;
    }

    private static List<Dish> parseDish(JSONArray array) throws JSONException {
        List<Dish> list = new ArrayList<>();
        int length = array.length();
        for(int i=0;i<length;i++){
            JSONObject o = array.getJSONObject(i);
            Dish dish = new Dish();
            dish.setName(o.getString("name"));
            dish.setPrice(o.getInt("price"));
            dish.setUrlImage(o.getString("url_image"));
            Catalog catalog = new Catalog();
            catalog.setId(o.getInt("id_catalog"));
            dish.setCatalog(catalog);
            list.add(dish);
        }
        return list;
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
            rest.setId_user((o.getString("id_user")));
            rest.setName(o.getString("name"));
            rest.setAddress(o.getString("address"));
            rest.setPhoneNumber(o.getString("phone_number"));
            rest.setDescription(o.getString("describe_text"));
            rest.setUrlImage(o.getString("url_image"));
            rest.setTimeOpen(new SimpleDateFormat("HH:mm").parse(o.getString("time_open")));
            rest.setTimeClose(new SimpleDateFormat("HH:mm").parse(o.getString("time_close")));
            JSONObject locate = o.getJSONObject("location");
            rest.setLocation(new GeoPoint(locate.getDouble("lat"), locate.getDouble("lon")));
            rest.setRanks(parseRanks(o.getJSONArray("ranks")));
            rest.setComments(parseComment(o.getJSONArray("comments")));
            rest.setDishes(parseDish(o.getJSONArray("dishs")));
            listRestaurants.add(rest);
        }
        return listRestaurants;
    }


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
