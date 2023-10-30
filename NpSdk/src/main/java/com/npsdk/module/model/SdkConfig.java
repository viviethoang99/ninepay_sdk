package com.npsdk.module.model;

import android.content.Context;

import com.npsdk.module.utils.DeviceUtils;

import java.io.Serializable;

public class SdkConfig implements Serializable {

    private final String merchantCode;
    private final String secretKey;
    private final String uid;
    private final String env;
    private String brandColor;

    protected SdkConfig(Builder builder) {
        merchantCode = builder.mMerchantCode;
        secretKey = builder.mSecretkey;
        uid = builder.mUid;
        env = builder.mEnv;
        brandColor = builder.mBrandColor;
    }

    public String getMerchantCode() {
        if (merchantCode == null) return "";
        return merchantCode;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getUid() {
        return uid;
    }

    public String getEnv() {
        return env;
    }

    public String getBrandColor() {
        return brandColor;
    }



    public static class Builder {
        private String mMerchantCode;
        private String mSecretkey;
        private String mUid;
        private String mEnv;
        private String mBrandColor;
        private Context context;
        public Builder(Context context) {
            this.context = context;
        }

        public Builder(String merchantCode, String secretkey, String uid, String env, String brandColor) {
            mMerchantCode = merchantCode;
            mSecretkey = secretkey;
            mUid = uid;
            mEnv = env;
            mBrandColor = brandColor;
        }

        public Builder merchantCode(String merchantCode) {
            mMerchantCode = merchantCode;
            return this;
        }

        public Builder secretKey(String key) {
            mSecretkey = key;
            return this;
        }

        public Builder uid(String uid) {
            if (uid == null || uid.isEmpty()) {
                uid = DeviceUtils.getAndroidID(context);
            }
            mUid = uid;
            return this;
        }

        public Builder env(String env) {
            mEnv = env;
            return this;
        }


        public Builder brandColor(String brandColor) {
            mBrandColor = brandColor;
            return this;
        }

        public SdkConfig build() {
            return new SdkConfig(this);
        }
    }

}

