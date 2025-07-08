package com.lee.spi.core.invoke;

import com.lee.spi.core.cache.SpiCache;
import com.lee.spi.core.config.CommonConfig;
import com.lee.spi.core.exception.ErrorCode;
import com.lee.spi.core.exception.SpiRuntimeException;
import com.lee.spi.core.meta.RelationMeta;
import com.lee.spi.core.meta.SpiProviderMeta;
import com.lee.spi.core.proxy.SpiProxy;
import com.lee.spi.core.remote.SpiRemoteApi;
import com.lee.spi.core.util.EnvUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
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
    public void execute(Consumer<Object> action) throws Exception {
        List<T> instance = getInstance();
        for (T t : instance) {
            action.accept(t);
        }
    }

    /**
     * 有返回值
     */
    public <R> R executeGetResult(Function<T, R> action) throws Exception {
        List<T> instance = getInstance();
        R result = null;
        for (T t : instance) {
            result = action.apply(t);
        }
        return result;
    }

    private List<T> getInstance() throws Exception{
        List<T> result = new ArrayList<>();
        Exception exception = null;

        SpiProxy spiProxy = SpiCache.spiSpiProxyCache.get(spiInterface.getName());
        if (spiProxy == null) {
            throw new SpiRuntimeException(ErrorCode.NOT_FIND_SPI_PROVIDER, spiInterface.getName());
        }
        Map<String, SpiProviderMeta> spiProviderMetaMap = spiProxy.getSpiProxyMap();

        // 找本地spi业务实现
        try{
            SpiProviderMeta spiProviderMeta = finalLocalProvider(spiProviderMetaMap);
            // spring环境
            if (EnvUtils.isSpringEnvironment()){
                result.add((T) SpiCache.spiProviderInstanceBeanCache.get(spiProviderMeta.getClassName()));
            }else{
                // 非spring环境
                result.add((T) spiProviderMeta.getClassType().newInstance());
            }
        } catch (Exception e) {
            exception = e;
        }

        if (CollectionUtils.isEmpty(result)){
            // 本地实现没有找到 尝试寻找remote实现
            findRemoteSpiProvider(code, spiInterface.getName(), result);
            if (CollectionUtils.isEmpty(result)){
                throw exception;
            }
        }

        // 找到叠加的所有产品实现
        findAllProductProvider(spiProviderMetaMap, result);

        return result;
    }


    private SpiProviderMeta finalLocalProvider(Map<String, SpiProviderMeta> spiProviderMetaMap) {
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
        return spiProviderMeta;
    }

    private void findRemoteSpiProvider(String identity, String interfaceName, List<T> result) {
        ServiceLoader<SpiRemoteApi> spiRemoteApis = ServiceLoader.load(SpiRemoteApi.class);
        for (SpiRemoteApi spiRemoteApi : spiRemoteApis) {
            Object remoteSpiProvider = spiRemoteApi.findRemoteSpiProvider(interfaceName, identity);
            if (remoteSpiProvider != null) {
                result.add((T) remoteSpiProvider);
            }
        }
    }

    private List<RelationMeta> findRemoteProductRelation() {
        ServiceLoader<SpiRemoteApi> spiRemoteApis = ServiceLoader.load(SpiRemoteApi.class);
        for (SpiRemoteApi spiRemoteApi : spiRemoteApis) {
            List<RelationMeta> relationMetas = spiRemoteApi.findRemoteProductRelation();
            if (!CollectionUtils.isEmpty(relationMetas)) {
                return relationMetas;
            }
        }
        return null;
    }

    private void findAllProductProvider(Map<String, SpiProviderMeta> spiProviderMetaMap, List<T> result) throws InstantiationException, IllegalAccessException {
        List<RelationMeta> relationMetasCache = SpiCache.relationMetasCache;
        if (CollectionUtils.isEmpty(relationMetasCache)) {
            relationMetasCache = findRemoteProductRelation();
            if (CollectionUtils.isEmpty(relationMetasCache)){
                return;
            }
        }
        for (RelationMeta relationMeta : relationMetasCache) {
            if (!relationMeta.getIdentity().equals(code)) {
                continue;
            }
            List<String> products = relationMeta.getProducts();
            for (String product : products) {
                SpiProviderMeta spiProviderMetaProduct = spiProviderMetaMap.get(product);
                if (spiProviderMetaProduct != null){
                    // spring环境
                    if (EnvUtils.isSpringEnvironment()){
                        result.add((T) SpiCache.spiProviderInstanceBeanCache.get(spiProviderMetaProduct.getClassName()));
                    }else{
                        // 非spring环境
                        result.add((T) spiProviderMetaProduct.getClassType().newInstance());
                    }
                }else{
                    // 尝试从远端加载
                    findRemoteSpiProvider(product, spiInterface.getName(), result);
                }
            }
        }
    }

}
