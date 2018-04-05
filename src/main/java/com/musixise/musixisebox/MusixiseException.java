package com.musixise.musixisebox;

import com.musixise.musixisebox.domain.result.ExceptionMsg;

/**
 * Created by zhaowei on 2018/4/1.
 */
public class MusixiseException extends RuntimeException {

    private String errorCode;

    public MusixiseException(String message) {
        super(message);
        this.setErrorCode(ExceptionMsg.FAILED.getCode());
    }

    public MusixiseException(String errorCode, String message) {
        super(message);
        this.setErrorCode(errorCode);
    }

    public MusixiseException(ExceptionMsg exceptionMsg, String message) {
        super(message);
        this.setErrorCode(exceptionMsg.getCode());
    }


    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
