package com.npsdk;

import com.npsdk.module.model.UserInfo;

public abstract class NineLibListener implements LibListener {
    public void getInfoSuccess(UserInfo userInfo) {
    }

    public void onError(int errorCode, String message) {
    }

    public void onLogoutSuccessful() {
    }

    public void onCloseSDK() {
    }

    public void sdkDidComplete(String name, Object status, Object params) {
    }

    public void backToAppFrom(String screen) {
    }

}
