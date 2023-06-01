package com.npsdk.jetpack_sdk.base.api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public abstract class BaseApiClient {

	protected ApiService apiService;

	public BaseApiClient() {
		Retrofit retrofit = ApiClient.getClient();
		apiService = retrofit.create(ApiService.class);
	}

	protected <T> void enqueue(Call<T> call, Callback<T> callback) {
		call.enqueue(callback);
	}

}
