package com.lee.spi.core.spring;

import com.lee.spi.core.annotation.Spi;
import com.lee.spi.core.annotation.SpiProvider;
import com.lee.spi.core.cache.SpiCache;
import com.lee.spi.core.cache.SpiCacheLoader;
import com.lee.spi.core.meta.SpiMeta;
import com.lee.spi.core.remote.SpiRemoteApi;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 服务订阅者进行BPP扫描 生成代理
 * @author yanhuai lee
 */
public class SpiBeanPostProcessor implements BeanPostProcessor, BeanDefinitionRegistryPostProcessor {

    public static final AtomicBoolean isSpringEnv = new AtomicBoolean(false);

    private final AtomicBoolean registerSpi = new AtomicBoolean(false);

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        try{
            if (!registerSpi.get()) {
                SpiCacheLoader.start();
                List<SpiMeta> spiMetasCache = SpiCache.spiMetasCache;
                if (spiMetasCache != null) {
                    if (!spiMetasCache.isEmpty()) {
                        for (SpiMeta spiMeta : spiMetasCache) {
                            BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(SpiProxyFactory.class);
                            builder.addConstructorArgValue(Class.forName(spiMeta.getInterfaceName()));
                            builder.setScope(BeanDefinition.SCOPE_SINGLETON);
                            builder.setPrimary(true);
                            // 注册spi代理到BeanDefinition
                            registry.registerBeanDefinition(spiMeta.getInterfaceName() + "$Spi_Proxy", builder.getBeanDefinition());
                        }
                    }
                }
            }
            registerSpi.set(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        for (String spiProviderClassName : SpiCache.spiProviderClassNames) {
            if (bean.getClass().getName().equals(spiProviderClassName)) {
                SpiCache.spiProviderInstanceBeanCache.put(spiProviderClassName, bean);
                ServiceLoader<SpiRemoteApi> spiRemoteApis = ServiceLoader.load(SpiRemoteApi.class);
                for (SpiRemoteApi spiRemoteApi : spiRemoteApis) {
                    SpiProvider annotation = bean.getClass().getAnnotation(SpiProvider.class);
                    String code = annotation.code();
                    Set<String> spiInterfaces = SpiCache.spiProviderSpiCache.get(spiProviderClassName);
                    if (spiInterfaces != null && !spiInterfaces.isEmpty()) {
                        for (String spiInterface : spiInterfaces) {
                            spiRemoteApi.registerRemoteSpiProvider(spiInterface, code, bean);
                        }
                    }
                }
            }
        }
        return bean;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        isSpringEnv.set(true);
    }
}
