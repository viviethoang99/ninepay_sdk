package com.npsdk.jetpack_sdk.repository;


import com.npsdk.jetpack_sdk.repository.model.CreateOrderCardModel;

public interface CallbackCreateOrder {
    void onSuccess(CreateOrderCardModel data);

}
