package com.npsdk.jetpack_sdk.base.api;

import com.npsdk.module.NPayLibrary;
import com.npsdk.module.utils.Constants;
import com.npsdk.module.utils.Flavor;
import com.npsdk.module.utils.Preference;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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
					.addConverterFactory(ScalarsConverterFactory.create())
					.addConverterFactory(GsonConverterFactory.create())
					.client(httpClient)
					.build();
		}
		return retrofit;
	}

	private static String getToken() {
		String token = Preference.getString(NPayLibrary.getInstance().activity, NPayLibrary.getInstance().sdkConfig.getEnv() + Constants.ACCESS_TOKEN);
		if (token != null) token = "Bearer "+token;
		return token;
	}

	private static OkHttpClient getCommonClient() {
		CookieManager cookieManager = new CookieManager();
		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		OkHttpClient httpClient = null;
		try {
			String Rke = EncryptServiceHelper.INSTANCE.getRandomkeyEncrypt();
			httpClient = new OkHttpClient.Builder()
					.addInterceptor(chain -> {
						Request.Builder builder = chain.request()
								.newBuilder()
								.addHeader("Merchant-Code", NPayLibrary.getInstance().sdkConfig.getMerchantCode())
								.addHeader("App-version-Code", "400")
								.addHeader("App-Type", "SDK")
								.addHeader("Authorization", getToken())
								.addHeader("Rke", Rke);

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

