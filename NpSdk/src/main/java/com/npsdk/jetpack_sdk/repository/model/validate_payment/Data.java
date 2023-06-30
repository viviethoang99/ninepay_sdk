package com.npsdk.jetpack_sdk.repository.model.validate_payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("payment_data")
    @Expose
    private List<PaymentData> listPaymentData;
    @SerializedName("merchant_info")
    @Expose
    private MerchantInfo merchantInfo;

    @SerializedName("policy_link")
    @Expose
    private String policyLink;

    @SerializedName("methods")
    @Expose
    private List<Methods> methods;
    @SerializedName("allowed_credit_card_brand")
    @Expose
    private List<String> allowedCreditCardBrand;

    @SerializedName("fee_data")
    @Expose
    private FeeData feeData;

    @SerializedName("amount")
    @Expose
    private Integer amount;

    public List<PaymentData> getListPaymentData() {
        return listPaymentData;
    }

    public void setPaymentData(List<PaymentData> paymentData) {
        this.listPaymentData = paymentData;
    }

    public MerchantInfo getMerchantInfo() {
        return merchantInfo;
    }

    public String getPolicyLink() {
        return policyLink;
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

    public FeeData getFeeData() {
        return feeData;
    }

    public void setFeeData(FeeData feeData) {
        this.feeData = feeData;
    }

    public Integer getAmount() {
        return amount;
    }

}