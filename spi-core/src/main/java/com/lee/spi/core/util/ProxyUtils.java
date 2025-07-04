package com.lee.spi.core.util;

import com.lee.spi.core.proxy.SpiProxy;

import java.lang.reflect.Proxy;

/**
 * 代理工具类
 * @author yanhuai lee
 */
public class ProxyUtils {

    public static Object getProxy(Class<?> serviceInterface, SpiProxy spiProxy){
        return Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface},
                spiProxy);
    }

}
