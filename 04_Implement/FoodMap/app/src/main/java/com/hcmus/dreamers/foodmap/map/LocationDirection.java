package com.hcmus.dreamers.foodmap.map;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationDirection {
    @SerializedName("geometry")
    private List<double[]> listLocation;

    public LocationDirection(List<double[]> listLocation) {
        this.listLocation = listLocation;
    }

    public LocationDirection() {
    }

    public List<double[]> getListLocation() {
        return listLocation;
    }

    public void setListLocation(List<double[]> listLocation) {
        this.listLocation = listLocation;
    }
}
