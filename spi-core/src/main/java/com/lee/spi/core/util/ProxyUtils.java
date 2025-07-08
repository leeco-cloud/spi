package com.lee.spi.core.util;

import com.lee.spi.core.proxy.SpiProxy;
import com.lee.spi.core.remote.SpiRemoteApi;
import com.lee.spi.core.remote.SpiRemoteProxy;

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

    public static Object getRemoteProxy(Class<?> serviceInterface, SpiRemoteProxy spiProxy){
        return Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface},
                spiProxy);
    }

    public static void main(String[] args) {
        SpiRemoteApi spiRemoteApi = (SpiRemoteApi) ProxyUtils.getProxy(SpiRemoteApi.class, new SpiProxy());
        System.out.println(spiRemoteApi);
    }

}
