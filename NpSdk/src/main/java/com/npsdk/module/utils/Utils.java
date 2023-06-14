package com.npsdk.module.utils;

import android.util.Log;

public class Utils {
    public static String convertUrlToOrderId(String url) {
        String orderId = last(url.split("/")).trim();
        Log.d("TAG", "orderId: " + orderId);
        return orderId;
    }

    private static <T> T last(T[] array) {
        return array[array.length - 1];
    }

    public static String getUrlActionShop(String action) {
        String path = null;
        switch (action) {
            case Actions.SHOP:
                return "/hoa-don-thanh-toan?is_start=1";
            case Actions.BILLING_DIEN:
                path = "/hoa-don/hoa-don-tien-dien";
                break;
            case Actions.BILLING_TRUYEN_HINH:
                path = "/hoa-don/hoa-don-truyen-hinh";
                break;
            case Actions.BILLING_DIEN_THOAI:
                path = "/hoa-don/hoa-don-dien-thoai-co-dinh";
                break;
            case Actions.BILLING_INTERNET:
                path = "/hoa-don/hoa-don-internet";
                break;
            case Actions.BILLING_NUOC:
                path = "/hoa-don/hoa-don-nuoc";
                break;
            case Actions.BILLING_BAO_HIEM:
                path = "/hoa-don/hoa-don-bao-hiem";
                break;
            case Actions.BILLING_TAI_CHINH:
                path = "/hoa-don/hoa-don-tai-chinh";
                break;
            case Actions.BILLING_TRA_SAU:
                path = "/hoa-don/hoa-don-tra-sau";
                break;
            case Actions.BILLING_TIN_DUNG:
                path = "/hoa-don/hoa-don-the-tin-dung";
                break;
            case Actions.BILLING_HOC_PHI:
                path = "/hoa-don/hoa-don-hoc-phi";
                break;
            case Actions.BILLING_TRA_GOP:
                path = "/hoa-don/hoa-don-tra-gop";
                break;
            case Actions.BILLING_VE_TAU_XE:
                path = "/hoa-don/hoa-don-ve-xe";
                break;
            case Actions.BILLING_VETC:
                path = "/hoa-don/hoa-don-duong-bo";
                break;
            case Actions.TOPUP:
                path = "/dich-vu-dien-thoai?code=topup_phone&is_start=1";
                break;
            case Actions.PHONE_CARD:
                path = "/dich-vu-dien-thoai?code=phone_card&is_start=1";
                break;
            case Actions.DATA_CARD:
                path = "/dich-vu-dien-thoai?code=topup_data&is_start=1";
                break;
            case Actions.GAME:
                path = "/mua-the-game?code=game_card&is_start=1";
                break;
            case Actions.SERVICE_CARD:
                path = "/dich-vu-thanh-toan?is_start=1";
                break;
        }

        return Flavor.baseShop + path;
    }
}
