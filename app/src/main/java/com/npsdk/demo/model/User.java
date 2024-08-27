package com.npsdk.demo.model;

import com.google.gson.Gson;

public class User {
    private String phone;
    private String name;
    private Integer status;
    private Integer balance;

    public User() {
    }

    public User(String phone, String name, Integer status, Integer balance) {
        this.phone = phone;
        this.name = name;
        this.status = status;
        this.balance = balance;
    }

    public static User fromJson(String jsonData) {
        return new Gson().fromJson(jsonData, User.class);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
