package com.npsdk;

import com.npsdk.module.model.Bank;

import java.util.List;

public interface LibListener {
	
	public void onLoginSuccessful();

	public void onPaySuccessful();

	public void getInfoSuccess(String phone, String balance, String ekycStatus, List<Bank> listBank, String name);

	public void onError(int errorCode, String message);

	public void onLogoutSuccessful();

	public void onCloseSDK();
	public void onPaymentFailed();
}
