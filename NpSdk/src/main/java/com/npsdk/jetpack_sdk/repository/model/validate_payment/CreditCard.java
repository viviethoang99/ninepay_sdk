package com.npsdk.jetpack_sdk.repository.model.validate_payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditCard {

    @SerializedName("CARD_BRAND")
    @Expose
    private String cardBrand;
    @SerializedName("IN_LAND")
    @Expose
    private Integer inLand;
    @SerializedName("OUT_LAND")
    @Expose
    private Integer outLand;

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public Integer getInLand() {
        return inLand;
    }

    public void setInLand(Integer inLand) {
        this.inLand = inLand;
    }

    public Integer getOutLand() {
        return outLand;
    }

    public void setOutLand(Integer outLand) {
        this.outLand = outLand;
    }

}