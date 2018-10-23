package com.hcmus.dreamers.foodmap.common;

import com.google.gson.annotations.SerializedName;

public class ResponseJSON {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseJSON() {
    }

    public int getCode() {
        return code;
    }

    public ResponseJSON(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
