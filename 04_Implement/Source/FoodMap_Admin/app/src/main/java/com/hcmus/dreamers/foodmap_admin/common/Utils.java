package com.hcmus.dreamers.foodmap_admin.common;

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

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class Utils {
    public static RequestBody buildParameter(Map<String, String> params){
//        Uri.Builder builder = new Uri.Builder();
//        for(String key: params.keySet()){
//            builder.appendQueryParameter(key, params.get(key).toString());
//    }
//        return builder.build().getEncodedQuery();
        FormBody.Builder builder = new FormBody.Builder();

        // Add Params to Builder
        for ( Map.Entry<String, String> entry : params.entrySet() ) {
            if (entry.getValue() != null)
                builder.add( entry.getKey(), entry.getValue() );
        }
        RequestBody requestBody = builder.build();
        return requestBody;
    }


    public static String buildUrl(final String url, Map<String, String> params){
        Uri.Builder builder = new Uri.Builder();
        for(String key: params.keySet()){
            builder.appendQueryParameter(key, params.get(key).toString());
        }
        String query = builder.build().getEncodedQuery();
        StringBuffer buffer = new StringBuffer();
        buffer.append(url);
        buffer.append("?");
        buffer.append(query);
        return buffer.toString();
    }

}
