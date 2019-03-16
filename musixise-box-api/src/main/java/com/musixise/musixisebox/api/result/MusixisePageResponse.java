package com.musixise.musixisebox.api.result;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhaowei on 2018/4/6.
 */
public class MusixisePageResponse<T> extends Response {

    protected MusixisePage data;

    public MusixisePageResponse(T data, long total, int size, int current) {
        this.data = new MusixisePage<>(data, total, size, current);
    }

    public MusixisePageResponse(T data) {
        this.data = new MusixisePage<>(data);
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
        private long total;

        @ApiModelProperty(value = "每页显示数量", example = "10", position = 2)
        private int size;

        @ApiModelProperty(value = "当前页码", example = "1",position = 3)
        private int current;

        public MusixisePage(T list) {
            this.list = list;
        }

        public MusixisePage(T list, long total, int size, int curren) {
            this.list = list;
            this.total = total;
            this.size = size;
            this.current = curren;
        }

        public T getList() {
            return list;
        }

        public void setList(T list) {
            this.list = list;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int curren) {
            this.current = curren;
        }
    }
}
