package com.lee.spi.core.remote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ServiceLoader;

/**
 * spi remote代理执行器
 * @author yanhuai lee
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpiRemoteProxy implements InvocationHandler {

    private String interfaceName;
    private String code;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ServiceLoader<SpiRemoteApi> spiRemoteApis = ServiceLoader.load(SpiRemoteApi.class);
        Object result = null;
        for (SpiRemoteApi spiRemoteApi : spiRemoteApis) {
            result = spiRemoteApi.invokeRemoteSpi(interfaceName, method, args, code, 5000);
        }
        return result;
    }

}
