package com.hcmus.dreamers.foodmap.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.hcmus.dreamers.foodmap.Model.Offer;

import java.lang.reflect.Type;

public class OrderSerializer implements JsonSerializer<Offer> {
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
}
