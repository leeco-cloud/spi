package com.lee.spi.core.spring;

/**
 * 业务身份
 */
public class BizSession {

    public static ThreadLocal<String> IDENTITY = new ThreadLocal<>();

}
