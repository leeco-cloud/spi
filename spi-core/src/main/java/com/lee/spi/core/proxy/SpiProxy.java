package com.lee.spi.core.proxy;

import com.lee.spi.core.meta.SpiMeta;
import com.lee.spi.core.meta.SpiProviderMeta;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * spi代理执行器
 * @author yanhuai lee
 */
@Getter
@Setter
public class SpiProxy implements InvocationHandler {

    private SpiMeta spiMeta;

    private Map<String, SpiProviderMeta> spiProxyMap;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }

}
