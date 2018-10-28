package com.hcmus.dreamers.foodmap.Model;
<<<<<<< HEAD
<<<<<<< HEAD

import com.hcmus.dreamers.foodmap.Model.Catalog;
=======
>>>>>>> 1f75ee0703c52a89cb505e966fffdcd4f95bb295
=======

import com.hcmus.dreamers.foodmap.Model.Catalog;
>>>>>>> 4699d4b57b07c302afafa523ac3181478c538834

public class Dish {
    private String name;
    private int price;
    private String urlImage;
    private Catalog catalog;

    //
    public Dish() {

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
