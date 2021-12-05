package com.alidaodao.app.commons;

/**
 * @author Jack
 * ex or px
 */
public enum  EXPX {
    /**
     * ex 秒级
     */
    SECONDS("EX"),
    /**
     * px 毫秒级
     */
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
