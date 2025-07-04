package com.lee.spi.core.spring;

import com.lee.spi.core.cache.SpiCache;
import com.lee.spi.core.cache.SpiCacheLoader;
import com.lee.spi.core.meta.SpiMeta;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务订阅者进行BPP扫描 生成代理
 * @author yanhuai lee
 */
@Component
public class SpiBeanPostProcessor implements InstantiationAwareBeanPostProcessor, ApplicationContextAware {

    public static ApplicationContext applicationContext;

    @Override
    public PropertyValues postProcessProperties(
            @NonNull PropertyValues pvs,
            @NonNull Object bean,
            @NonNull String beanName) throws BeansException {

        ReflectionUtils.doWithFields(bean.getClass(), field -> {
            if (field.getType().isInterface()) {
                SpiCacheLoader.init();
                List<SpiMeta> spiMetasCache = SpiCache.spiMetasCache;
                if (spiMetasCache != null) {
                    List<SpiMeta> collect = spiMetasCache.stream().filter(spiMeta -> spiMeta.getInterfaceName().equals(field.getType().getName())).collect(Collectors.toList());
                    if (!collect.isEmpty()) {
                        Object proxy = SpiCache.spiProxyInstanceCache.get(field.getType().getName());
                        field.setAccessible(true);
                        field.set(bean, proxy);
                    }
                }
            }
        });
        return pvs;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpiBeanPostProcessor.applicationContext = applicationContext;
    }

}
