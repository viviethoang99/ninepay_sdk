package com.npsdk.jetpack_sdk.repository;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.npsdk.jetpack_sdk.base.api.BaseApiClient;
import com.npsdk.jetpack_sdk.base.api.EncryptServiceHelper;
import com.npsdk.jetpack_sdk.repository.model.VerifyPaymentModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VerifyPayment extends BaseApiClient {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler mainThread = new Handler(Looper.getMainLooper());

    public void create(Context context, String paymentId, String otp, CallbackVerifyPayment callback) {
        executor.execute(() -> {
            Call<String> call = apiService.verifyPayment(paymentId, otp);
            enqueue(call, new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 200 && response.body() != null) {
                        String objectDecrypt = EncryptServiceHelper.INSTANCE.decryptAesBase64(
                                response.body(),
                                EncryptServiceHelper.INSTANCE.getRandomkeyRaw()
                        );
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        VerifyPaymentModel verifyPaymentModel = gson.fromJson(objectDecrypt, VerifyPaymentModel.class);
                        updateUI(() -> {
                            if (verifyPaymentModel.getErrorCode() == 1) {
                                callback.onSuccess(verifyPaymentModel.getMessage());
                            } else {
                                callback.onSuccess(null);
                            }
                        });
                    } else {
                        Toast.makeText(context, "Đã có lỗi xảy ra, code 1004", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(context, "Đã có lỗi xảy ra, code 1004", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    private void updateUI(Runnable runnable) {
        mainThread.post(runnable);
    }
}