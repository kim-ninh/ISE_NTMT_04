package com.hcmus.dreamers.foodmap.Model;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Restaurant implements Serializable {
    private int id;
    private String ownerUsername;
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
    //so luong guest da yeu thich
    private int nFavorites;
    // bảng lưu thông tin người đánh giá
    // keyvalue: <email, star>
    private HashMap<String, Integer> ranks;

    private int num_checkin;

    //
    public Restaurant() {
        num_checkin = 0;
        dishes = new ArrayList<Dish>();
        comments = new ArrayList<Comment>();
        ranks = new HashMap<String, Integer>();
    }
	
    public Restaurant(int id_rest,
                      String ownerUsername,
                      String name,
                      String address,
                      String phoneNumber,
                      String description,
                      String urlImage,
                      Date timeOpen,
                      Date timeClose,
                      GeoPoint location,
                      int num_checkin) {
        this.id = id_rest;
        this.ownerUsername = ownerUsername;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.urlImage = urlImage;
        this.timeOpen = timeOpen;
        this.timeClose = timeClose;
        this.location = location;
        this.num_checkin = num_checkin;
	}

    public int getNum_checkin() {
        return num_checkin;
    }

    public void setNum_checkin(int num_checkin) {
        this.num_checkin = num_checkin;
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

    public String getOwnerUsername() { return ownerUsername; }

    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }

    public int getnFavorites() {
        return nFavorites;
    }

    public void setnFavorites(int nFavorites) {
        this.nFavorites = nFavorites;
    }

    // -1 là chưa đánh giá
    public int findRank(String email){
        if (ranks.containsValue(email))
            return ranks.get(email);
        return -1;
    }

}
