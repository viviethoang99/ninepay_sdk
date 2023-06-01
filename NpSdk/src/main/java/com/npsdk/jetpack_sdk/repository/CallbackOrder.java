package com.npsdk.jetpack_sdk.repository;


import com.npsdk.jetpack_sdk.repository.model.ValidatePaymentModel;

public interface CallbackOrder {
	void onSuccess(ValidatePaymentModel data);
}
