package com.lee.spi.core.proxy;

import com.lee.spi.core.cache.SpiCache;
import com.lee.spi.core.meta.SpiMeta;
import com.lee.spi.core.meta.SpiProviderMeta;
import com.lee.spi.core.spring.ThreadContextBeanSelectionStrategy;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * spi代理执行器
 * @author yanhuai lee
 */
public class SpiProxy implements InvocationHandler {

    @Getter
    @Setter
    private SpiMeta spiMeta;

    @Getter
    @Setter
    private Map<String, SpiProviderMeta> spiProxyMap;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 处理Object类的基础方法
        if (method.getDeclaringClass() == Object.class) {
            return handleObjectMethod(proxy, method, args);
        }

        String identity = ThreadContextBeanSelectionStrategy.BEAN_NAME_HOLDER.get();
        if (spiProxyMap.containsKey(identity)) {
            SpiProviderMeta spiProviderMeta = spiProxyMap.get(identity);
            Object bean = SpiCache.spiProviderInstanceBeanCache.get(spiProviderMeta.getClassName());
            return method.invoke(bean, args);
        }

        throw new RuntimeException("未找到spi具体实现: " + spiMeta.getInterfaceName());
    }

    private Object handleObjectMethod(Object proxy, Method method, Object[] args) {
        String methodName = method.getName();
        switch (methodName) {
            case "equals":
                return proxy == args[0]; // 比较代理对象本身
            case "hashCode":
                return System.identityHashCode(proxy); // 代理对象的hashCode
            case "toString":
                return "SpiProxy@" + Integer.toHexString(System.identityHashCode(proxy)) +
                        " for " + spiMeta.getInterfaceName();
            default:
                throw new UnsupportedOperationException("Unexpected Object method: " + methodName);
        }
    }

}
