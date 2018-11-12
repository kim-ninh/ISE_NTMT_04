package com.study.android.testorderserver;

public class Order {
    private String email_owner;
    private String email_guest;

    public Order() {
    }

    public Order(String email_owner, String email_guest) {
        this.email_owner = email_owner;
        this.email_guest = email_guest;
    }


    public String getEmail_owner() {
        return email_owner;
    }

    public void setEmail_owner(String email_owner) {
        this.email_owner = email_owner;
    }

    public String getEmail_guest() {
        return email_guest;
    }

    public void setEmail_guest(String email_guest) {
        this.email_guest = email_guest;
    }

    @Override
    public String toString() {
        String data = "{\"email_owner\":" + "\"" + email_owner + "\"" + ", \"email_guest\":" + "\"" + email_guest + "\"" + "}";
        return data;
    }
}
