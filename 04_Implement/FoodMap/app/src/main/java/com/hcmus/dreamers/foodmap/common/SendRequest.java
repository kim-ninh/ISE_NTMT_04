package com.hcmus.dreamers.foodmap.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SendRequest {
    public static String send(Request request) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        return  sb.toString();
    }
}
