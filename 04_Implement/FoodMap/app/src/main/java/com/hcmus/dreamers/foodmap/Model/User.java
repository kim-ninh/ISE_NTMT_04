package com.hcmus.dreamers.foodmap.Model;
<<<<<<< HEAD

import com.google.gson.annotations.SerializedName;
=======
>>>>>>> 1f75ee0703c52a89cb505e966fffdcd4f95bb295

public abstract class User {
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;

    public User(){
        name = "";
        email = "";
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
