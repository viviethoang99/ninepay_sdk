package com.npsdk.jetpack_sdk.base.api;

import com.npsdk.jetpack_sdk.repository.model.CreateOrderCardModel;
import com.npsdk.jetpack_sdk.repository.model.ListBankModel;
import com.npsdk.jetpack_sdk.repository.model.ValidatePaymentModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("/sdk/v1/paygate-whitelabel/validate-order")
    Call<ValidatePaymentModel> check(@Field("data") String url);

    @FormUrlEncoded
    @POST("/sdk/v1/paygate-whitelabel/create-order")
    Call<CreateOrderCardModel> createOrderCardInter(@Field("data") String url, @Field("card_number") String cardNumber, @Field("card_name") String cardName, @Field("expire_month") String expireMonth, @Field("expire_year") String expireYear, @Field("cvc") String cvc, @Field("amount") String amount, @Field("method") String method);

    @FormUrlEncoded
    @POST("/sdk/v1/paygate-whitelabel/create-order")
    Call<CreateOrderCardModel> createOrderCardInland(@Field("data") String url, @Field("card_number") String cardNumber, @Field("card_name") String cardName, @Field("expire_month") String expireMonth, @Field("expire_year") String expireYear, @Field("amount") String amount, @Field("method") String method);

    @FormUrlEncoded
    @POST("/sdk/v1/paygate-whitelabel/create-order")
    Call<CreateOrderCardModel> createOrderCardWallet(@Field("data") String url, @Field("method") String method);

    @GET("/sdk/v1/paygate-whitelabel/banks-support")
    Call<ListBankModel> getListBanks();

    @FormUrlEncoded
    @POST("/sdk/v1/wallet/payment")
    Call<String> createPayment(@Field("type") String type, @Field("order_id") String orderId);


    @FormUrlEncoded
    @POST("/sdk/v1/payment/verifyOtp")
    Call<String> verifyPayment(@Field("payment_id") String paymentId, @Field("otp") String otp);
}
