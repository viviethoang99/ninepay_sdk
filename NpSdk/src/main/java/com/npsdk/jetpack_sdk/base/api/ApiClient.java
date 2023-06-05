package com.npsdk.jetpack_sdk.base.api;

import com.npsdk.module.utils.Flavor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

public class ApiClient {

	private static Retrofit retrofit;

	public static Retrofit getClient() {
		if (retrofit == null) {
			OkHttpClient httpClient = getCommonClient();
			retrofit = new Retrofit.Builder()
					.baseUrl(Flavor.baseApi)
					.addConverterFactory(GsonConverterFactory.create())
					.client(httpClient)
					.build();
		}
		return retrofit;
	}

	private static OkHttpClient getCommonClient() {
		CookieManager cookieManager = new CookieManager();
		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		OkHttpClient httpClient = null;
		try {
			httpClient = new OkHttpClient.Builder()
					.addInterceptor(chain -> {
						Request.Builder builder = chain.request()
								.newBuilder()
								.addHeader("Merchant-Code", "sdk_test").addHeader("App-version-Code", "375");

						Request request = builder.build();
						return chain.proceed(request);
					})
					.connectTimeout(30, TimeUnit.SECONDS)
					.writeTimeout(30, TimeUnit.SECONDS)
					.readTimeout(30, TimeUnit.SECONDS)
					.addInterceptor(logging)
					.build();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return httpClient;
	}
}

