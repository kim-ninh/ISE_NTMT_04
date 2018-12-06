package com.hcmus.dreamers.foodmap.Model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.hcmus.dreamers.foodmap.serializer.OrderSerializer;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@JsonAdapter(OrderSerializer.class)
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
    @SerializedName("status")
    private int status;
    @SerializedName("date_order")
    private Date dateOrder;

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

    public Date getDateOrder() {
        return dateOrder;
    }

    public Offer(int id, String nameDish, int discountPercent, String guestEmail, int total, int status, Date dateOrder) {
        this.id = id;
        this.nameDish = nameDish;
        this.discountPercent = discountPercent;
        this.guestEmail = guestEmail;
        this.total = total;
        this.status = status;
        this.dateOrder = dateOrder;
    }

    public void setDateOrder(Date dateOrder) {
        this.dateOrder = dateOrder;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Offer)) return false;
        Offer offer = (Offer) o;
        return getId() == offer.getId();
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getDateString(){
        return dateOrder.getYear() + "-" + dateOrder.getMonth() + "-" + dateOrder.getDate() + " " + dateOrder.getHours() + ":" + dateOrder.getMinutes() + ":" + dateOrder.getSeconds();
    }

    public boolean compareDateOrder(Date date){
        if(this.dateOrder.getYear() == date.getYear() && this.dateOrder.getMonth() == date.getMonth() && this.dateOrder.getDate() == date.getDate()){
            return true;
        }
        return false;
    }
}

