package com.hcmus.dreamers.foodmap.Model;

import java.util.Date;

public class Comment {
    private Date dateTime;
    private String comment;
    private com.hcmus.dreamers.foodmap.Model.User user;

    public Comment() {
    }

    public Comment(Date dateTime, String comment, com.hcmus.dreamers.foodmap.Model.User user) {
        this.dateTime = dateTime;
        this.comment = comment;
        this.user = user;
    }

    public com.hcmus.dreamers.foodmap.Model.User getUser() {
        return user;
    }

    public void setUser(com.hcmus.dreamers.foodmap.Model.User user) {
        this.user = user;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
