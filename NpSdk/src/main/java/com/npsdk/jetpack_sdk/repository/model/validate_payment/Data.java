package com.npsdk.jetpack_sdk.repository.model.validate_payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

	@SerializedName("payment_data")
	@Expose
	private PaymentData paymentData;
	@SerializedName("merchant_info")
	@Expose
	private MerchantInfo merchantInfo;
	@SerializedName("methods")
	@Expose
	private List<Methods> methods;
	@SerializedName("allowed_credit_card_brand")
	@Expose
	private List<String> allowedCreditCardBrand;

	public PaymentData getPaymentData() {
		return paymentData;
	}

	public void setPaymentData(PaymentData paymentData) {
		this.paymentData = paymentData;
	}

	public MerchantInfo getMerchantInfo() {
		return merchantInfo;
	}

	public void setMerchantInfo(MerchantInfo merchantInfo) {
		this.merchantInfo = merchantInfo;
	}

	public List<Methods> getMethods() {
		return methods;
	}



	public List<String> getAllowedCreditCardBrand() {
		return allowedCreditCardBrand;
	}

	public void setAllowedCreditCardBrand(List<String> allowedCreditCardBrand) {
		this.allowedCreditCardBrand = allowedCreditCardBrand;
	}

}