package com.npsdk.jetpack_sdk.repository.model.validate_payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeeData {

    @SerializedName("ATM_CARD")
    @Expose
    private String atmCard;
    @SerializedName("CREDIT_CARD")
    @Expose
    private CreditCard creditCard;
    @SerializedName("COLLECTION")
    @Expose
    private String collection;
    @SerializedName("WALLET")
    @Expose
    private String wallet;
    @SerializedName("QR_PAY")
    @Expose
    private String qrPay;
    @SerializedName("BUY_NOW_PAY_LATER")
    @Expose
    private String buyNowPayLater;

    public String getAtmCard() {
        return atmCard;
    }

    public void setAtmCard(String atmCard) {
        this.atmCard = atmCard;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getQrPay() {
        return qrPay;
    }

    public void setQrPay(String qrPay) {
        this.qrPay = qrPay;
    }

    public String getBuyNowPayLater() {
        return buyNowPayLater;
    }

    public void setBuyNowPayLater(String buyNowPayLater) {
        this.buyNowPayLater = buyNowPayLater;
    }

}