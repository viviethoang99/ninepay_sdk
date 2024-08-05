package com.npsdk.demo.util;

import android.util.Pair;

import com.npsdk.demo.R;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<ServiceModel> getServices() {
        List<ServiceModel> services = new ArrayList<>();
        services.add(new ServiceModel("Thanh toán", R.drawable.ic_payment, ServiceEnum.Payment));
        services.add(new ServiceModel("Nạp tiền", R.drawable.ic_deposit, ServiceEnum.Deposit));
        services.add(new ServiceModel("Chuyển tiền", R.drawable.ic_transfer, ServiceEnum.Transfer));
        services.add(new ServiceModel("Rút tiền", R.drawable.ic_withdraw, ServiceEnum.Withdrawal));
        services.add(new ServiceModel("Nạp tiền \n điện thoại", R.drawable.ic_deposit_phone, ServiceEnum.TopUpPhone));
        services.add(new ServiceModel("Thanh toán \n hóa đơn", R.drawable.ic_payment_bill, ServiceEnum.BillPayment));
        services.add(new ServiceModel("Thanh toán \n qua cổng", R.drawable.ic_payment_gate, ServiceEnum.PaymentGate));
        return services;
    }

}
