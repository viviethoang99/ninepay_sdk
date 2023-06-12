package com.npsdk.jetpack_sdk.repository.model.validate_payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditCard {

    @SerializedName("MASTER")
    @Expose
    private Master master;
    @SerializedName("VISA")
    @Expose
    private Visa visa;
    @SerializedName("JCB")
    @Expose
    private Jcb jcb;
    @SerializedName("AMEX")
    @Expose
    private Amex amex;

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public Visa getVisa() {
        return visa;
    }

    public void setVisa(Visa visa) {
        this.visa = visa;
    }

    public Jcb getJcb() {
        return jcb;
    }

    public void setJcb(Jcb jcb) {
        this.jcb = jcb;
    }

    public Amex getAmex() {
        return amex;
    }

    public void setAmex(Amex amex) {
        this.amex = amex;
    }

}