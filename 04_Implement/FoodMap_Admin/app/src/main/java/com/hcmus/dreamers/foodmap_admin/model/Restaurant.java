package com.hcmus.dreamers.foodmap_admin.model;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.Date;

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
    private String address;

    //
    public Restaurant() {
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
                      GeoPoint location) {
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
}
