package com.npsdk.jetpack_sdk.repository;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.npsdk.jetpack_sdk.base.api.BaseApiClient;
import com.npsdk.jetpack_sdk.repository.model.ListBankModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetListBank extends BaseApiClient {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler mainThread = new Handler(Looper.getMainLooper());

    public void get(Context context, CallbackListBank callbackListBank) {
        executor.execute(() -> {
            Call<ListBankModel> call = apiService.getListBanks();
            enqueue(call, new Callback<ListBankModel>() {
                @Override
                public void onResponse(Call<ListBankModel> call, Response<ListBankModel> response) {
                    if (response.body() != null) {
                        updateUI(() -> {
                            callbackListBank.onSuccess(response.body());
                        });
                    } else {
                        System.out.println("SERVER ERROR");
                    }
                }

                @Override
                public void onFailure(Call<ListBankModel> call, Throwable t) {
                    Toast.makeText(context, "Đã có lỗi xảy ra, code 1001", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    private void updateUI(Runnable runnable) {
        mainThread.post(runnable);
    }
}