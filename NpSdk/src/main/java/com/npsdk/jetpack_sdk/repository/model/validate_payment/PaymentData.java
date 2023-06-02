package com.npsdk.jetpack_sdk.repository.model.validate_payment;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentData {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("value")
    @Expose
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}


