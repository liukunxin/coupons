package com.quincy.coupons_merchants.security;

/**
 * Created by Quincy on 2019/1/1.
 * @Description 用ThreadLocal去单独存储每一个线程携带的Token信息
 */
public class AccessContext {
    private static final ThreadLocal<String> token = new ThreadLocal<String>();

    public static String getToken() {
        return token.get();
    }

    public static void setToken(String tokenStr) {
        token.set(tokenStr);
    }

    public static void clearAccessKey() {
        token.remove();
    }
}
