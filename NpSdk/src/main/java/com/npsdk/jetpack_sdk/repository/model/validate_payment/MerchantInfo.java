package com.npsdk.jetpack_sdk.repository.model.validate_payment;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MerchantInfo {

	@SerializedName("merchant_id")
	@Expose
	private Integer merchantId;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("logo")
	@Expose
	private String logo;
	@SerializedName("bin_locale_allow")
	@Expose
	private Integer binLocaleAllow;

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Integer getBinLocaleAllow() {
		return binLocaleAllow;
	}

	public void setBinLocaleAllow(Integer binLocaleAllow) {
		this.binLocaleAllow = binLocaleAllow;
	}

}
