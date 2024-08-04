package com.npsdk.demo.util;

import android.util.Pair;

import com.npsdk.demo.R;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<Pair<String, Integer>> getServices() {
        List<Pair<String, Integer>> services = new ArrayList<>();
        services.add(new Pair<>("Thanh toán", R.drawable.ic_payment));
        services.add(new Pair<>("Nạp tiền", R.drawable.ic_deposit));
        services.add(new Pair<>("Chuyển tiền", R.drawable.ic_transfer));
        services.add(new Pair<>("Rút tiền", R.drawable.ic_withdraw));
        services.add(new Pair<>("Nạp tiền \n điện thoại", R.drawable.ic_deposit_phone));
        services.add(new Pair<>("Thanh toán \n hóa đơn", R.drawable.ic_payment_bill));
        services.add(new Pair<>("Thanh toán \n qua cổng", R.drawable.ic_payment_gate));
        return services;
    }

}
