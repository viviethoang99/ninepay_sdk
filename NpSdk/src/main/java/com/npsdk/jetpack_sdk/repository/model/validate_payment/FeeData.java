package com.npsdk.jetpack_sdk.repository.model.validate_payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeeData {

    @SerializedName("ATM_CARD")
    @Expose
    private Integer atmCard;
    @SerializedName("CREDIT_CARD")
    @Expose
    private List<CreditCard> creditCard;
    @SerializedName("COLLECTION")
    @Expose
    private Integer collection;
    @SerializedName("WALLET")
    @Expose
    private Integer wallet;
    @SerializedName("QR_PAY")
    @Expose
    private Integer qrPay;
    @SerializedName("BUY_NOW_PAY_LATER")
    @Expose
    private Integer buyNowPayLater;

    public Integer getAtmCard() {
        return atmCard;
    }

    public void setAtmCard(Integer atmCard) {
        this.atmCard = atmCard;
    }

    public List<CreditCard> getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(List<CreditCard> creditCard) {
        this.creditCard = creditCard;
    }

    public Integer getCollection() {
        return collection;
    }

    public void setCollection(Integer collection) {
        this.collection = collection;
    }

    public Integer getWallet() {
        return wallet;
    }

    public void setWallet(Integer wallet) {
        this.wallet = wallet;
    }

    public Integer getQrPay() {
        return qrPay;
    }

    public void setQrPay(Integer qrPay) {
        this.qrPay = qrPay;
    }

    public Integer getBuyNowPayLater() {
        return buyNowPayLater;
    }

    public void setBuyNowPayLater(Integer buyNowPayLater) {
        this.buyNowPayLater = buyNowPayLater;
    }

}