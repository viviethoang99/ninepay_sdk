package com.npsdk.jetpack_sdk.repository;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.npsdk.jetpack_sdk.base.api.BaseApiClient;
import com.npsdk.jetpack_sdk.repository.model.ListBankModel;
import com.npsdk.module.NPayLibrary;
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
                    if (response.code() == 200 && response.body() != null) {
                        updateUI(() -> {
                            callbackListBank.onSuccess(response.body());
                        });
                    } else {
                        NPayLibrary.getInstance().callbackError(1001, "Đã có lỗi xảy ra, code 1001");
                    }
                }

                @Override
                public void onFailure(Call<ListBankModel> call, Throwable t) {
                    NPayLibrary.getInstance().callbackError(1001, "Đã có lỗi xảy ra, code 1001");
                }
            });
        });

    }

    private void updateUI(Runnable runnable) {
        mainThread.post(runnable);
    }
}