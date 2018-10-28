package com.hcmus.dreamers.foodmap.Model;
<<<<<<< HEAD
<<<<<<< HEAD

import com.google.gson.annotations.SerializedName;
import com.hcmus.dreamers.foodmap.Model.Dish;
=======
>>>>>>> 1f75ee0703c52a89cb505e966fffdcd4f95bb295
=======

>>>>>>> 4699d4b57b07c302afafa523ac3181478c538834

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Restaurant {
    private int id;
    private int id_user;
    private String name;
    private String phoneNumber;
    private String description;
    private String urlImage;
    private Date timeOpen;
    private Date timeClose;
    private GeoPoint location;
    private List<Dish> dishes;
    private List<Comment> comments;
    private String address;

    // bảng lưu thông tin người đánh giá
    // keyvalue: <email, star>
    private HashMap<String, Integer> ranks;
    //
    public Restaurant() {
        dishes = new ArrayList<Dish>();
        comments = new ArrayList<Comment>();
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public HashMap<String, Integer> getRanks() {
        return ranks;
    }

    public void setRanks(HashMap<String, Integer> ranks) {
        this.ranks = ranks;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Date getTimeOpen() {
        return timeOpen;
    }

    public void setTimeOpen(Date timeOpen) {
        this.timeOpen = timeOpen;
    }

    public Date getTimeClose() {
        return timeClose;
    }

    public void setTimeClose(Date timeClose) {
        this.timeClose = timeClose;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId_user() { return id_user; }

    public void setId_user(int id_user) { this.id_user = id_user; }
}
