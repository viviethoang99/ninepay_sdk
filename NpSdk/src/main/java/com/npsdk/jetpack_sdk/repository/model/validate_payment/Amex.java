package com.npsdk.jetpack_sdk.repository.model.validate_payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Amex {

    @SerializedName("IN_LAND")
    @Expose
    private String inLand;
    @SerializedName("OUT_LAND")
    @Expose
    private String outLand;

    public String getInLand() {
        return inLand;
    }

    public void setInLand(String inLand) {
        this.inLand = inLand;
    }

    public String getOutLand() {
        return outLand;
    }

    public void setOutLand(String outLand) {
        this.outLand = outLand;
    }

}