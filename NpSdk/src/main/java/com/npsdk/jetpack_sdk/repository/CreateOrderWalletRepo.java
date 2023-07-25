package com.npsdk.jetpack_sdk.repository;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.npsdk.jetpack_sdk.base.api.BaseApiClient;
import com.npsdk.jetpack_sdk.repository.model.CreateOrderCardModel;
import com.npsdk.jetpack_sdk.repository.model.CreateOrderParamsWallet;
import com.npsdk.module.NPayLibrary;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateOrderWalletRepo extends BaseApiClient {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler mainThread = new Handler(Looper.getMainLooper());

    public void create(Context context, CreateOrderParamsWallet param, CallbackCreateOrder callbackCreateOrder) {
        executor.execute(() -> {
            Call<CreateOrderCardModel> call = apiService.createOrderCardWallet(param.getUrl(), param.getMethod(), param.getAmount());
            enqueue(call, new Callback<CreateOrderCardModel>() {
                @Override
                public void onResponse(Call<CreateOrderCardModel> call, Response<CreateOrderCardModel> response) {
                    if (response.code() == 200 && response.body() != null) {
                        updateUI(() -> {
                            callbackCreateOrder.onSuccess(response.body());
                        });
                    } else {
                        NPayLibrary.getInstance().callbackError(1003, "Đã có lỗi xảy ra, code 1003");
                    }
                }

                @Override
                public void onFailure(Call<CreateOrderCardModel> call, Throwable t) {
                    NPayLibrary.getInstance().callbackError(1003, "Đã có lỗi xảy ra, code 1003");
                }
            });
        });

    }

    private void updateUI(Runnable runnable) {
        mainThread.post(runnable);
    }
}