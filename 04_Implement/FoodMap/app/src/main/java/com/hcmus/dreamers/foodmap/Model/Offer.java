package com.hcmus.dreamers.foodmap.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Offer implements Serializable{
    @SerializedName("id")
    private int id;
    @SerializedName("namedish")
    private String nameDish;
    @SerializedName("discount_percent")
    private int discountPercent;
    @SerializedName("guest_email")
    private String guestEmail;
    @SerializedName("total")
    private int total;
    private transient int status;

    public Offer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Offer(String nameDish, int discountPercent, String guestEmail, int total, int status) {
        this.nameDish = nameDish;
        this.discountPercent = discountPercent;
        this.guestEmail = guestEmail;
        this.total = total;
        this.status = status;
    }

    public Offer(String nameDish, int discountPercent, String guestEmail, int total) {
        this.nameDish = nameDish;
        this.discountPercent = discountPercent;
        this.guestEmail = guestEmail;
        this.total = total;
    }

    public String getNameDish() {
        return nameDish;
    }

    public void setNameDish(String nameDish) {
        this.nameDish = nameDish;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
