package com.hcmus.dreamers.foodmap.map;

import com.google.gson.annotations.SerializedName;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
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

    public List<GeoPoint> toPoint(){
        List<GeoPoint> result = new ArrayList<>();
        int n = listLocation.size();

        for (int i = 0; i < n; i++){
            result.add(new GeoPoint(listLocation.get(i)[1], listLocation.get(i)[0]));
        }

        return result;
    }
}
