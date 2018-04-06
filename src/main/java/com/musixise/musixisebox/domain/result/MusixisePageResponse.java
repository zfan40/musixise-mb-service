package com.musixise.musixisebox.domain.result;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhaowei on 2018/4/6.
 */
public class MusixisePageResponse<T> extends MusixiseResponse {

    @ApiModelProperty(value = "记录总数", example = "9999")
    private int total;

    @ApiModelProperty(value = "每页显示数量", example = "10")
    private int size;

    @ApiModelProperty(value = "当前页码", example = "1")
    private int curren;

    public int getTotal() {
        return total;
    }

    public MusixisePageResponse(T data, int total, int size, int curren) {
        super(ExceptionMsg.SUCCESS, data);
        this.total = total;
        this.size = size;
        this.curren = curren;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurren() {
        return curren;
    }

    public void setCurren(int curren) {
        this.curren = curren;
    }

}
