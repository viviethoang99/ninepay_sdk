package com.npsdk.jetpack_sdk.repository;


import android.os.Handler;
import android.os.Looper;
import com.npsdk.jetpack_sdk.DataOrder;
import com.npsdk.jetpack_sdk.base.api.BaseApiClient;
import com.npsdk.jetpack_sdk.repository.model.MerchantModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GetInfoMerchant extends BaseApiClient {

	ExecutorService executor = Executors.newSingleThreadExecutor();
	Handler mainThread = new Handler(Looper.getMainLooper());

	public void get() {
		executor.execute(() -> {
			Call<MerchantModel> call = apiService.getMerchant();
			enqueue(call, new Callback<MerchantModel>() {
				@Override
				public void onResponse(Call<MerchantModel> call, Response<MerchantModel> response) {
					if (response.body() != null && response.code() == 200 && response.body().getErrorCode() == 0) {
						updateUI(() -> {
							DataOrder.Companion.setMerchantInfo(response.body().getData().getMerchantInfo());
						});
					}
				}

				@Override
				public void onFailure(Call<MerchantModel> call, Throwable t) {
					System.out.println(t.getMessage());
				}
			});
		});

	}

	private void updateUI(Runnable runnable) {
		mainThread.post(runnable);
	}
}