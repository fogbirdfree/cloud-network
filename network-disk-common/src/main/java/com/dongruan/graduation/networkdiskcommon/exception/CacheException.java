package com.dongruan.graduation.networkdiskcommon.exception;

import java.util.Arrays;

/**
 * 缓存操作异常
 *
 * @author: duyubo
 */
@SuppressWarnings("serial")
public class CacheException extends RuntimeException {

    private Integer code = 500;

    private Exception ex;

    public CacheException() {
        super();
    }

    public CacheException(Exception e) {
        super(e);
    }

    public CacheException(String msg) {
        super(msg);
    }

    public CacheException(int code, String msg, Exception e) {
        super(msg, e);
        this.code = code;
    }

    public CacheException(String msg, Exception e) {
        super(msg, e);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }

    @Override
    public String toString(){
        return ex.getMessage() +" ------- "+ Arrays.toString(ex.getStackTrace());
    }

}