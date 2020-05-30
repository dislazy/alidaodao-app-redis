package com.alidaodao.app.commons;

public enum  EXPX {
    SECONDS("EX"),
    MILLISECONDS("PX");

    private String expx;

    EXPX(String expx) {
        this.expx = expx;
    }

    public String getString() {
        return expx;
    }

    public byte[] getBytes() {
        return expx.getBytes();
    }
}
