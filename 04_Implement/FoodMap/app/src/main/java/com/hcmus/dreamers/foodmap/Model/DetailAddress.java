/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmus.dreamers.foodmap.Model;

import com.google.gson.annotations.SerializedName;

import org.osmdroid.util.GeoPoint;

import java.util.List;

/**
 *
 * @author phuc
 */
public class DetailAddress {
    @SerializedName("osm_type")
    private String osm_type;
    @SerializedName("extent")
    private List<Double> extent;
    @SerializedName("country")
    private String country;
    @SerializedName("osm_key")
    private String osm_key;
    @SerializedName("osm_value")
    private String osm_value;
    @SerializedName("name")
    private String name;
    @SerializedName("street")
    private String street;
    @SerializedName("city")
    private String city;
    @SerializedName("coordinates")
    private GeoPoint point;



    public DetailAddress(String osm_type, List<Double> extent, String country, String osm_key, String osm_value, String name, String street, String city, GeoPoint point) {
        this.osm_type = osm_type;
        this.extent = extent;
        this.country = country;
        this.osm_key = osm_key;
        this.osm_value = osm_value;
        this.name = name;
        this.street = street;
        this.city = city;
        this.point = point;
    }


    public DetailAddress() {
    }

    public GeoPoint getPoint() {
        return point;
    }

    public void setPoint(GeoPoint point) {
        this.point = point;
    }

    public String getOsm_type() {
        return osm_type;
    }

    public void setOsm_type(String osm_type) {
        this.osm_type = osm_type;
    }

    public List<Double> getExtent() {
        return extent;
    }

    public void setExtent(List<Double> extent) {
        this.extent = extent;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOsm_key() {
        return osm_key;
    }

    public void setOsm_key(String osm_key) {
        this.osm_key = osm_key;
    }

    public String getOsm_value() {
        return osm_value;
    }

    public void setOsm_value(String osm_value) {
        this.osm_value = osm_value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        StringBuilder address = new StringBuilder();

        if (name != null){
            address.append(name);
            address.append(", ");
        }

        if (street != null){
            address.append(street);
            address.append(", ");
        }

        if (city != null){
            address.append(city);
            address.append(", ");
        }

        address.append(country);
        return address.toString();
    }
}
