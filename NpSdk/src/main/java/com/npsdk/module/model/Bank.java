package com.npsdk.module.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bank {

    @SerializedName("b_code")
    @Expose
    private String bCode;
    @SerializedName("b_name")
    @Expose
    private String bName;
    @SerializedName("b_fullname")
    @Expose
    private String bFullname;
    @SerializedName("b_type")
    @Expose
    private Integer bType;
    @SerializedName("b_account")
    @Expose
    private String bAccount;
    @SerializedName("b_token")
    @Expose
    private String bToken;
    @SerializedName("b_logo")
    @Expose
    private String bLogo;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("b_account_name")
    @Expose
    private String bAccountName;

    public String getbCode() {
        return bCode;
    }

    public void setbCode(String bCode) {
        this.bCode = bCode;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbFullname() {
        return bFullname;
    }

    public void setbFullname(String bFullname) {
        this.bFullname = bFullname;
    }

    public Integer getbType() {
        return bType;
    }

    public void setbType(Integer bType) {
        this.bType = bType;
    }

    public String getbAccount() {
        return bAccount;
    }

    public void setbAccount(String bAccount) {
        this.bAccount = bAccount;
    }

    public String getbToken() {
        return bToken;
    }

    public void setbToken(String bToken) {
        this.bToken = bToken;
    }

    public String getbLogo() {
        return bLogo;
    }

    public void setbLogo(String bLogo) {
        this.bLogo = bLogo;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getbAccountName() {
        return bAccountName;
    }

    public void setbAccountName(String bAccountName) {
        this.bAccountName = bAccountName;
    }

}