package com.hcmus.dreamers.foodmap.common;

import android.net.Uri;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class Utils {
    public static String buildParameter(Map<String, String> params){
        Uri.Builder builder = new Uri.Builder();
        for(String key: params.keySet()){
            builder.appendQueryParameter(key, params.get(key).toString());
    }
        return builder.build().getEncodedQuery();
    }
}
