package com.lee.spi.core.invoke;

import com.lee.spi.core.cache.SpiCache;
import com.lee.spi.core.cache.SpiCacheLoader;
import com.lee.spi.core.meta.SpiProviderMeta;
import com.lee.spi.core.proxy.SpiProxy;
import com.lee.spi.core.util.EnvUtils;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 目标执行器
 * @author yanhuai lee
 */
public class ExecuteInvoke<T> {

    private final String code;
    private final Class<T> spiInterface;

    ExecuteInvoke(String code, Class<T> spiInterface) {
        this.code = code;
        this.spiInterface = spiInterface;
    }

    /**
     * 无返回值
     */
    public void execute(Consumer<T> action) throws Exception {
        T instance = getInstance();
        action.accept(instance);
    }

    /**
     * 有返回值
     */
    public <R> R executeGetResult(Function<T, R> action) throws Exception {
        T instance = getInstance();
        return action.apply(instance);
    }

    private <T> T getInstance() throws Exception{
        SpiCacheLoader.start();

        SpiProxy spiProxy = SpiCache.spiSpiProxyCache.get(spiInterface.getName());
        if (spiProxy == null) {
            throw new RuntimeException("未找到spi实现: " + spiInterface.getName());
        }
        Map<String, SpiProviderMeta> spiProviderMetaMap = spiProxy.getSpiProxyMap();
        if (spiProviderMetaMap == null || spiProviderMetaMap.isEmpty()) {
            throw new RuntimeException("未找到spi具体实现: " + spiInterface.getName());
        }
        SpiProviderMeta spiProviderMeta = spiProviderMetaMap.get(code);
        if (spiProviderMeta == null) {
            throw new RuntimeException("业务身份: " + code + ", 未找到spi具体实现: " + spiInterface.getName());
        }
        // spring环境
        if (EnvUtils.isSpringEnvironment()){
            return (T) SpiCache.spiProviderInstanceBeanCache.get(spiProviderMeta.getClassName());
        }
        // 非spring环境
        return (T) spiProviderMeta.getClassType().newInstance();
    }
}
