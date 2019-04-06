package com.huang.homan.youtubetv.Model.error;

import com.google.gson.annotations.SerializedName;

public class ErrorsItem{

	@SerializedName("reason")
	private String reason;

	@SerializedName("domain")
	private String domain;

	@SerializedName("message")
	private String message;

	public void setReason(String reason){
		this.reason = reason;
	}

	public String getReason(){
		return reason;
	}

	public void setDomain(String domain){
		this.domain = domain;
	}

	public String getDomain(){
		return domain;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return
			"ErrorsItem{" +
			"reason = '" + reason + '\'' +
			",domain = '" + domain + '\'' +
			",message = '" + message + '\'' +
			"}";
		}
}