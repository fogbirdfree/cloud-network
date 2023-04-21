package com.dongruan.graduation.networkdiskcommon.exception;

/**
 * 日期格式化异常
 *
 * @author: duyubo
 */
@SuppressWarnings("serial")
public class DateFormatException extends RuntimeException {

	public DateFormatException() {
		super();
	}
	
	public DateFormatException(Exception e) {
		super(e);
	}

	public DateFormatException(String msg) {
		super(msg);
	}

	public DateFormatException(String msg, Exception e) {
		super(msg, e);
	}

}
