package com.dongruan.graduation.networkdiskcommon.pojo;

import lombok.Data;

/**
 * @author duyubo
 */

@Data
public class SmsResult {
	private String code;
	private String count;
	private String create_date;
	private String mobile;
	private String msg;
	private String smsid;
	private String uid;
	public SmsResult() {
		super();
	}
}
