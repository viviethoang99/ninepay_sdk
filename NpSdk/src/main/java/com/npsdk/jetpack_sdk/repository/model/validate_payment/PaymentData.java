package com.npsdk.jetpack_sdk.repository.model.validate_payment;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentData {

	@SerializedName("merchantKey")
	@Expose
	private String merchantKey;
	@SerializedName("time")
	@Expose
	private Integer time;
	@SerializedName("invoice_no")
	@Expose
	private String invoiceNo;
	@SerializedName("amount")
	@Expose
	private Integer amount;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("return_url")
	@Expose
	private String returnUrl;
	@SerializedName("back_url")
	@Expose
	private String backUrl;
	@SerializedName("method")
	@Expose
	private String method;

	public String getMerchantKey() {
		return merchantKey;
	}

	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}


