package com.musixise.musixisebox.api.exception;


import com.musixise.musixisebox.api.enums.ExceptionMsg;

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
