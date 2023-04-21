package com.dongruan.graduation.networkdiskcommon.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * @author duyubo
 */

@Data
public class RestResult<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private int respCode = 1;

	private String respMsg = "成功！";

	private T respData;

	private Map<String, Object> respMap;

	private String signature;

	private String deviceType;

	private String dataCode;

	@SuppressWarnings("unchecked")
	public RestResult(String errorMsg) {
		this.respMsg = errorMsg;
		this.respCode = 0;
		this.respData = (T) new Object();
		this.respMap = new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	public RestResult() {
		this.respData = (T) "";
		this.respMap = new HashMap<>();
	}

	public void success(T object) {
		this.respCode = 1;
		this.respMsg = "1";
		this.respData = object;
		this.respMap = new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	public void error() {
		this.respCode = 0;
		this.respMsg = "0";
		this.respData = (T) "";
		this.respMap = new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	public void error(String message) {
		this.respCode = 0;
		this.respMsg = message;
		this.respData = (T) "";
		this.respMap = new HashMap<>();
	}

}
