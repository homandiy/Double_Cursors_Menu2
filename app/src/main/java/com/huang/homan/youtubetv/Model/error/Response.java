package com.huang.homan.youtubetv.Model.error;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("code")
	private int code;

	@SerializedName("message")
	private String message;

	@SerializedName("errors")
	private List<ErrorsItem> errors;

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setErrors(List<ErrorsItem> errors){
		this.errors = errors;
	}

	public List<ErrorsItem> getErrors(){
		return errors;
	}

	@Override
 	public String toString(){
		return
			"SearchData{" +
			"code = '" + code + '\'' +
			",message = '" + message + '\'' +
			",errors = '" + errors + '\'' +
			"}";
		}
}