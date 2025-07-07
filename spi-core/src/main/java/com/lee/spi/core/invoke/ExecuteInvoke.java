package com.lee.spi.core.invoke;

import com.lee.spi.core.cache.SpiCache;
import com.lee.spi.core.config.CommonConfig;
import com.lee.spi.core.exception.ErrorCode;
import com.lee.spi.core.exception.SpiRuntimeException;
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

        SpiProxy spiProxy = SpiCache.spiSpiProxyCache.get(spiInterface.getName());
        if (spiProxy == null) {
            throw new SpiRuntimeException(ErrorCode.NOT_FIND_SPI_PROVIDER, spiInterface.getName());
        }
        Map<String, SpiProviderMeta> spiProviderMetaMap = spiProxy.getSpiProxyMap();
        if (spiProviderMetaMap == null || spiProviderMetaMap.isEmpty()) {
            throw new SpiRuntimeException(ErrorCode.NOT_FIND_SPI_PROVIDER, spiInterface.getName());
        }
        SpiProviderMeta spiProviderMeta = spiProviderMetaMap.get(code);
        if (spiProviderMeta == null) {
            String defaultCode = String.format(CommonConfig.defaultIdentityCode, spiInterface.getName());
            spiProviderMeta = spiProviderMetaMap.get(defaultCode);
            if (spiProviderMeta == null) {
                throw new SpiRuntimeException(ErrorCode.NOT_FIND_SPI_PROVIDER_BY_IDENTITY, code, spiInterface.getName());
            }
        }
        // spring环境
        if (EnvUtils.isSpringEnvironment()){
            return (T) SpiCache.spiProviderInstanceBeanCache.get(spiProviderMeta.getClassName());
        }
        // 非spring环境
        return (T) spiProviderMeta.getClassType().newInstance();
    }
}
