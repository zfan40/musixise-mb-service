package com.musixise.musixisebox.domain.result;

/**
 * Created by zhaowei on 2018/4/1.
 */

public class MusixiseResponse extends Response {

    private Object data;

    public MusixiseResponse(Object data) {
        this.data = data;
    }

    public static MusixiseResponse successResponse(Object msg) {
        return new MusixiseResponse(ExceptionMsg.SUCCESS, msg);
    }

    public static MusixiseResponse errorResponse(String msg) {
        return new MusixiseResponse(ExceptionMsg.FAILED, msg);
    }

    public MusixiseResponse(ExceptionMsg msg) {
        super(msg);
    }

    public MusixiseResponse(String rspCode, String rspMsg) {
        super(rspCode, rspMsg);
    }

    public MusixiseResponse(String rspCode, String rspMsg, Object data) {
        super(rspCode, rspMsg);
        this.data = data;
    }

    public MusixiseResponse(ExceptionMsg msg, Object data) {
        super(msg);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MusixiseResponse{" +
                "data=" + data +
                "} " + super.toString();
    }

}
