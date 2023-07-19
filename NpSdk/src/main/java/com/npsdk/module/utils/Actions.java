package com.npsdk.module.utils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class Actions {
    public static final String OPEN_WALLET = "home";
    public static final String LOGIN = Flavor.baseUrl + "/v1/dang-nhap";
    public static final String HISTORY = Flavor.baseUrl + "/v1/lich-su-chuyen-tien";
    public static final String TRANSFER = Flavor.baseUrl + "/v1/chuyen-tien";
    public static final String DEPOSIT = Flavor.baseUrl + "/v1/nap-tien";
    public static final String QR = Flavor.baseUrl + "/v1/scan_qr_code";

    // DANH MUC HOA DON
    public static final String SHOP = "shop";
    public static final String BILLING_DIEN = "BILLING_DIEN";
    public static final String BILLING_TRUYEN_HINH = "BILLING_TRUYEN_HINH";
    public static final String BILLING_DIEN_THOAI = "BILLING_DIEN_THOAI";
    public static final String BILLING_INTERNET = "BILLING_INTERNET";
    public static final String BILLING_NUOC = "BILLING_NUOC";
    public static final String BILLING_BAO_HIEM = "BILLING_BAO_HIEM";
    public static final String BILLING_TAI_CHINH = "BILLING_TAI_CHINH";
    public static final String BILLING_TRA_SAU = "BILLING_TRA_SAU";
    public static final String BILLING_TIN_DUNG = "BILLING_TIN_DUNG";
    public static final String BILLING_HOC_PHI = "BILLING_HOC_PHI";
    public static final String BILLING_TRA_GOP = "BILLING_TRA_GOP";
    public static final String BILLING_VE_TAU_XE = "BILLING_VE_TAU_XE";
    public static final String BILLING_VETC = "BILLING_VETC";
    public static final String TOPUP = "TOPUP";
    public static final String PHONE_CARD = "PHONE_CARD";
    public static final String DATA_CARD = "DATA_CARD";
    public static final String GAME = "GAME";
    public static final String SERVICE_CARD = "SERVICE_CARD";


    public static final ArrayList<String> listAllServices() {

        ArrayList<String> listTemp = new ArrayList<String>();
        listTemp.addAll(Arrays.asList(
                SHOP, BILLING_DIEN, BILLING_TRUYEN_HINH, BILLING_DIEN_THOAI,
                BILLING_INTERNET, BILLING_NUOC, BILLING_BAO_HIEM, BILLING_TAI_CHINH,
                BILLING_TRA_SAU, BILLING_HOC_PHI,
                BILLING_TRA_GOP, BILLING_VE_TAU_XE, BILLING_VETC, TOPUP,
                DATA_CARD, PHONE_CARD, GAME, SERVICE_CARD));
        return listTemp;
    }

    public static String forgotPassword(@Nullable String phone) {
        if (phone == null) phone = "";
        return Flavor.baseUrl + "/v1/quen-mat-khau?phone=" + phone;
    }
}
