package com.hcmus.dreamers.foodmap.Model;

import java.io.Serializable;
import java.util.Date;

public class Discount implements Serializable{
    private int id;
    private int id_rest;
    private String nameDish;
    private int discountPercent;
    private Date timeStart;
    private Date timeEnd;

    public Discount(int id, int id_rest, String nameDish, int discountPercent, String guestEmail, Date timeStart, Date timeEnd) {
        this.id = id;
        this.id_rest = id_rest;
        this.nameDish = nameDish;
        this.discountPercent = discountPercent;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public Discount() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_rest() {
        return id_rest;
    }

    public void setId_rest(int id_rest) {
        this.id_rest = id_rest;
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

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }
}
