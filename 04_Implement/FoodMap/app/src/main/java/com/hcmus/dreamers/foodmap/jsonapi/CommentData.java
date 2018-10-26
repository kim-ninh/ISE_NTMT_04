package com.hcmus.dreamers.foodmap.jsonapi;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@JsonAdapter(CommentDeserializer.class)
public class CommentData {
    @SerializedName("date_time")
    private Date date_time;
    @SerializedName("guest_email")
    private String guest_email;
    @SerializedName("owner_email")
    private String owner_email;
    @SerializedName("comment")
    private String comment;

    public CommentData() {
    }

    public CommentData(Date date_time, String guest_email, String owner_email, String comment) {
        this.date_time = date_time;
        this.guest_email = guest_email;
        this.owner_email = owner_email;
        this.comment = comment;
    }

    public Date getDate_time() {
        return date_time;
    }

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    public String getGuest_email() {
        return guest_email;
    }

    public void setGuest_email(String guest_email) {
        this.guest_email = guest_email;
    }

    public String getOwner_email() {
        return owner_email;
    }

    public void setOwner_email(String owner_email) {
        this.owner_email = owner_email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}


class CommentDeserializer implements JsonDeserializer<CommentData> {
    @Override
    public CommentData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(jsonObject.get("date_time").getAsString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new CommentData(
                date,
                jsonObject.get("guest_email").getAsString(),
                jsonObject.get("owner_email").getAsString(),
                jsonObject.get("comment").getAsString()
        );
    }
}