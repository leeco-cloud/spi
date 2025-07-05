package com.lee.spi.core.spring;

import com.lee.spi.core.cache.SpiCache;
import com.lee.spi.core.cache.SpiCacheLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 服务订阅者进行BPP扫描 生成代理
 * @author yanhuai lee
 */
public class SpiProviderBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        SpiCacheLoader.init();

        for (String spiProviderClassName : SpiCache.spiProviderClassNames) {
            if (bean.getClass().getName().equals(spiProviderClassName)) {
                SpiCache.spiProviderInstanceBeanCache.put(spiProviderClassName, bean);
            }
        }

        return bean;
    }
}
