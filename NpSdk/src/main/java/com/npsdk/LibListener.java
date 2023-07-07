package com.npsdk;

import androidx.annotation.Nullable;
import com.npsdk.module.model.Bank;

import java.util.List;

public interface LibListener {
	public void getInfoSuccess(String jsonData);

	public void onError(int errorCode, String message);

	public void onLogoutSuccessful();

	public void onCloseSDK();
	public void onCallbackListener(String name, Object status, @Nullable Object params);

	public void backToAppFrom(String screen);
}
