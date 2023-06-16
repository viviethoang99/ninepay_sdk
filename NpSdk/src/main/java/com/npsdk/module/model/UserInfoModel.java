package com.npsdk.module.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserInfoModel {

    @SerializedName("phone")
    @Expose
    private String phone;
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
    public UserInfoModel() {
    }

    /**
     * @param balance
     * @param phone
     * @param status
     */
    public UserInfoModel(String phone, Integer status, Integer balance) {
        super();
        this.phone = phone;
        this.status = status;
        this.balance = balance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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