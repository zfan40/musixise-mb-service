package com.musixise.musixisebox.domain.result;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhaowei on 2018/4/6.
 */
public class MusixisePageResponse<T> extends Response {

    protected MusixisePage data;

    public MusixisePageResponse(T data, int page, int size, int current) {
        this.data = new MusixisePage<>(data, page, size, current);
    }

    public MusixisePage getData() {
        return data;
    }

    public void setData(MusixisePage data) {
        this.data = data;
    }

    public class MusixisePage<T> {

        @ApiModelProperty(value = "列表")
        private T list;

        @ApiModelProperty(value = "记录总数", example = "9999", position = 1)
        private int total;

        @ApiModelProperty(value = "每页显示数量", example = "10", position = 2)
        private int size;

        @ApiModelProperty(value = "当前页码", example = "1",position = 3)
        private int curren;

        public MusixisePage(T list) {
            this.list = list;
        }

        public MusixisePage(T list, int total, int size, int curren) {
            this.list = list;
            this.total = total;
            this.size = size;
            this.curren = curren;
        }

        public T getList() {
            return list;
        }

        public void setList(T list) {
            this.list = list;
        }

        public int getTotal() {
            return total;
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
}
