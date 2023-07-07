package com.npsdk.jetpack_sdk.repository;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.npsdk.jetpack_sdk.base.api.BaseApiClient;
import com.npsdk.jetpack_sdk.repository.model.ValidatePaymentModel;
import com.npsdk.module.NPayLibrary;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CheckValidatePayment extends BaseApiClient {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler mainThread = new Handler(Looper.getMainLooper());

    public void check(Context context, String url, CallbackOrder callbackOrder) {
        executor.execute(() -> {
            Call<ValidatePaymentModel> call = apiService.check(url);
            enqueue(call, new Callback<ValidatePaymentModel>() {
                @Override
                public void onResponse(Call<ValidatePaymentModel> call, Response<ValidatePaymentModel> response) {
                    if (response.body() != null && response.code() == 200 && response.body().getErrorCode() == 0) {
                        updateUI(() -> {
                            callbackOrder.onSuccess(response.body());
                        });
                    } else {
                        NPayLibrary.getInstance().callbackError(1000, "Đã có lỗi xảy ra, code 1000");
                    }
                }

                @Override
                public void onFailure(Call<ValidatePaymentModel> call, Throwable t) {
                    NPayLibrary.getInstance().callbackError(1000, "Đã có lỗi xảy ra, code 1000");
                }
            });
        });

    }

    private void updateUI(Runnable runnable) {
        mainThread.post(runnable);
    }
}