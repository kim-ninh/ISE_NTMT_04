package com.hcmus.dreamers.foodmap.Model;
import com.hcmus.dreamers.foodmap.Model.Catalog;

import java.io.Serializable;

public class Dish implements Serializable {
    private String name;
    private int price;
    private String urlImage;
    private Catalog catalog;

    //
    public Dish() {
        this.name = "";
        this.price = 0;
        this.urlImage = "";
        this.catalog = new Catalog(1,"Cơm");
    }

    public Dish(String name, int price, String urlImage, Catalog catalog) {
        this.name = name;
        this.price = price;
        this.urlImage = urlImage;
        this.catalog = catalog;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }



}
