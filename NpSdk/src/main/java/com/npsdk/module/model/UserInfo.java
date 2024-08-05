package com.npsdk.module.model;

import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserInfo {

    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("balance")
    @Expose
    private Integer balance;

    @SerializedName("banks")
    @Expose
    private List<Bank> banks;
    /**
     * No args constructor for use in serialization
     */
    public UserInfo() {
    }

    public UserInfo(String phone, @Nullable String name, Integer status, Integer balance) {
        super();
        this.phone = phone;
        this.name = name;
        this.status = status;
        this.balance = balance;
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

    public List<Bank> getBanks() {
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }


}