package com.alidaodao.app.commons;

/**
 * @author Jack
 * expire enum
 */
public class Expire {
    /**
     * 10秒钟
     */
    public static Expire s10 = new Expire(10);
    /**
     * 1分钟
     */
    public static Expire m1 = new Expire(60);
    /**
     * 3分钟
     */
    public static Expire m3 = new Expire(60 * 3);
    /**
     * 5分钟
     */
    public static Expire m5 = new Expire(60 * 5);
    /**
     * 10分钟
     */
    public static Expire m10 = new Expire(60 * 10);
    /**
     * 30分钟
     */
    public static Expire m30 = new Expire(60 * 30);
    /**
     * 31分钟
     */
    public static Expire m31 = new Expire(60 * 31);
    /**
     * 1小时
     */
    public static Expire h1 = new Expire(60 * 60);
    /**
     * 2 小时
     */
    public static Expire h2 = new Expire(2 * 60 * 60);
    /**
     * 6 小时
     */
    public static Expire h6 = new Expire(6 * 60 * 60);
    /**
     * 2小时1分钟
     */
    public static Expire h2PlusM1 = new Expire(121 * 60);
    /**
     * 1天
     */
    public static Expire d1 = new Expire(60 * 60 * 24);
    /**
     * 3天
     */
    public static Expire d3 = new Expire(60 * 60 * 24 * 3);
    /**
     * 7天
     */
    public static Expire d7 = new Expire(60 * 60 * 24 * 7);
    /**
     * 30天
     */
    public static Expire d30 = new Expire(60 * 60 * 24 * 30);

    private final long time;
    private final EXPX expx;

    private Expire(long time) {
        this(time, EXPX.SECONDS);
    }

    private Expire(long time, EXPX expx) {
        this.time = time;
        this.expx = expx;
    }

    public EXPX getEXPX() {
        return expx;
    }

    public long getTime() {
        return time;
    }

}
