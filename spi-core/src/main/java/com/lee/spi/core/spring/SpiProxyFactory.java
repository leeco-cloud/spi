package com.lee.spi.core.spring;

import com.lee.spi.core.cache.SpiCache;
import org.springframework.beans.factory.FactoryBean;

/**
 * SpiProxyFactoryBean
 * @author yanhuai lee
 */
public class SpiProxyFactory implements FactoryBean<Object> {

    private final Class<?> spiInterface;

    public SpiProxyFactory(Class<?> spiInterface) {
        this.spiInterface = spiInterface;
    }

    @Override
    public Object getObject() {
        // 获取动态代理
        return SpiCache.spiProxyInstanceCache.get(spiInterface.getName());
    }

    @Override
    public Class<?> getObjectType() {
        return spiInterface;
    }
}
