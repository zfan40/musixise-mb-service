package com.musixise.musixisebox.enums;

/**
 * Created by zhaowei on 2018/4/15.
 */
public enum FILE_TYPE {
    IMAGE("图片", 1),
    AUDIO("音频",2 );

    private String name;
    private int val;

    FILE_TYPE(String name, int val) {
        this.name = name;
        this.val = val;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
}
