package com.npsdk.jetpack_sdk.repository;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.npsdk.jetpack_sdk.base.api.BaseApiClient;
import com.npsdk.jetpack_sdk.base.api.EncryptServiceHelper;
import com.npsdk.module.model.UserInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetUserInfo extends BaseApiClient {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler mainThread = new Handler(Looper.getMainLooper());

    public void get(Context context) {
        executor.execute(() -> {
            Call<String> call = apiService.getUserInfo();
            enqueue(call, new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.body() != null) {
                        updateUI(() -> {
                            String objectDecrypt = EncryptServiceHelper.INSTANCE.decryptAesBase64(
                                    response.body(),
                                    EncryptServiceHelper.INSTANCE.getRandomkeyRaw()
                            );
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            UserInfoResponse userInfoResponse = gson.fromJson(objectDecrypt, UserInfoResponse.class);
                            System.out.println("My phone: " + userInfoResponse.getData().getPhone());
                        });
                    } else {
                        System.out.println("SERVER ERROR");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(context, "Đã có lỗi xảy ra, code 1001", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    private void updateUI(Runnable runnable) {
        mainThread.post(runnable);
    }
}