package com.hcmus.dreamers.foodmap.jsonapi;

import com.google.gson.Gson;
import com.hcmus.dreamers.foodmap.common.ResponseJSON;

public class ParseJSON {
    private static Gson gson = new Gson();
    public static ResponseJSON fromStringToResponeJSON(String data){
        return gson.fromJson(data, ResponseJSON.class);
    }


}
