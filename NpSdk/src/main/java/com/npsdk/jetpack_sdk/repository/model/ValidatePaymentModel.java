package com.npsdk.jetpack_sdk.repository.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.npsdk.jetpack_sdk.repository.model.validate_payment.Data;


public class ValidatePaymentModel {

	@SerializedName("server_time")
	@Expose
	private Integer serverTime;
	@SerializedName("status")
	@Expose
	private Integer status;
	@SerializedName("message")
	@Expose
	private String message;
	@SerializedName("error_code")
	@Expose
	private Integer errorCode;
	@SerializedName("data")
	@Expose
	private Data data;

	public Integer getServerTime() {
		return serverTime;
	}

	public void setServerTime(Integer serverTime) {
		this.serverTime = serverTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

}