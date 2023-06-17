package com.npsdk.module.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RefreshTokenModel {

    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;

    @SerializedName("public_key")
    @Expose
    private String publicKey;

    /**
     * No args constructor for use in serialization
     *
     */
    public RefreshTokenModel() {
    }

    public RefreshTokenModel(String accessToken, String refreshToken) {
        super();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}