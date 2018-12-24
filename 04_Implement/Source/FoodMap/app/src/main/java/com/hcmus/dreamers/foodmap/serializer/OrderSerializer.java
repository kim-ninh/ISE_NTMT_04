package com.hcmus.dreamers.foodmap.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.hcmus.dreamers.foodmap.Model.Offer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderSerializer implements JsonSerializer<Offer>, JsonDeserializer<Offer> {
    @Override
    public JsonElement serialize(Offer src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("id", -1);
        object.addProperty("namedish", src.getNameDish());
        object.addProperty("discount_percent", src.getDiscountPercent());
        object.addProperty("guest_email", src.getGuestEmail());
        object.addProperty("total", src.getTotal());
        object.addProperty("status", src.getStatus());
        object.addProperty("date_order", src.getDateString());
        return object;
    }

    @Override
    public Offer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int id = jsonObject.get("id").getAsInt();
        String namedish = jsonObject.get("namedish").getAsString();
        int discount_percent = jsonObject.get("discount_percent").getAsInt();
        String guestEmail = jsonObject.get("guest_email").getAsString();
        int total = jsonObject.get("total").getAsInt();
        int status = jsonObject.get("status").getAsInt();
        String date = jsonObject.get("date_order").getAsString();
        Date date_order = null;
        try {
            date_order = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {

        }
        Offer offer = new Offer(id, namedish, discount_percent, guestEmail, total, status, date_order);
        return offer;
    }
}
