package com.musixise.musixisebox.api.enums;

public enum CategoryEnum {

    MOTHER_EVENT(1);

    private int vale;

    CategoryEnum(int vale) {
        this.vale = vale;
    }

    public int getVale() {
        return vale;
    }

    public void setVale(int vale) {
        this.vale = vale;
    }
}
