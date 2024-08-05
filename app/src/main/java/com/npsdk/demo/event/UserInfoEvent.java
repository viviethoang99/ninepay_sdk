package com.npsdk.demo.event;

import com.npsdk.module.model.UserInfo;

public class UserInfoEvent {
    private UserInfo userInfo;

    public UserInfoEvent(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
