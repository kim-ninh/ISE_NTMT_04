package com.hcmus.dreamers.foodmap.AsyncTask;

import com.hcmus.dreamers.foodmap.common.ResponseJSON;
import com.hcmus.dreamers.foodmap.common.SendRequest;
import com.hcmus.dreamers.foodmap.define.ConstantCODE;

import java.io.IOException;

import okhttp3.Request;

public class DoingTask {
    private Request request;

    public DoingTask(Request request) {
        this.request = request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Object doInBackground(){
        String response;
        try {
            response = SendRequest.send(request);
        } catch (IOException e) {
            ResponseJSON responseJSON = new ResponseJSON(ConstantCODE.NOTINTERNET, "NOT INTERNET");
            response = responseJSON.toString();
        }
        return response;
    }
}
