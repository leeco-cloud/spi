package com.lee.spi.core.spring;

import com.lee.spi.core.cache.SpiCache;
import com.lee.spi.core.cache.SpiCacheLoader;
import com.lee.spi.core.meta.SpiMeta;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 服务订阅者进行BPP扫描 生成代理
 * @author yanhuai lee
 */
public class SpiBeanPostProcessor implements BeanPostProcessor, BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    public static final AtomicBoolean isSpringEnv = new AtomicBoolean(false);

    private final AtomicBoolean registerSpi = new AtomicBoolean(false);

    public static ApplicationContext applicationContext;

    public static ConfigurableListableBeanFactory configurableListableBeanFactory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpiBeanPostProcessor.applicationContext = applicationContext;
    }

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
            }
        }

        return bean;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        isSpringEnv.set(true);
        SpiBeanPostProcessor.configurableListableBeanFactory = beanFactory;
    }
}
