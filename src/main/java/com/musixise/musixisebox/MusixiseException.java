package com.musixise.musixisebox;

/**
 * Created by zhaowei on 2018/4/1.
 */
public class MusixiseException extends RuntimeException {

    private String errorCode;

    public MusixiseException(String message) {
        super(message);
    }

    public MusixiseException(String errorCode, String message) {
        super(message);
        this.setErrorCode(errorCode);
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
