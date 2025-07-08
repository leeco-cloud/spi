package com.lee.spi.core.proxy;

import com.lee.spi.core.cache.SpiCache;
import com.lee.spi.core.config.CommonConfig;
import com.lee.spi.core.exception.ErrorCode;
import com.lee.spi.core.exception.SpiRuntimeException;
import com.lee.spi.core.meta.RelationMeta;
import com.lee.spi.core.meta.SpiMeta;
import com.lee.spi.core.meta.SpiProviderMeta;
import com.lee.spi.core.remote.SpiRemoteApi;
import com.lee.spi.core.spring.BizSession;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

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
        String identity = BizSession.IDENTITY.get();
        if (StringUtils.isBlank(identity)){
            // 当前业务身份为空，则赋值默认实现的缓存key
            identity = String.format(CommonConfig.defaultIdentityCode, spiMeta.getInterfaceName());
        }

        List<Object> allBean = new ArrayList<>();
        finalSpiProviderRunningBean(identity, allBean);
        findAllProductRunningBean(identity, allBean);

        Object result = null;
        for (Object instance : allBean) {
            result = method.invoke(instance, args);
        }
        return result;
    }

    /**
     * 找到SPI实现
     */
    private void finalSpiProviderRunningBean(String identity, List<Object> allBean) {
        if (!spiProxyMap.containsKey(identity)) {
            identity = String.format(CommonConfig.defaultIdentityCode, spiMeta.getInterfaceName());
        }

        SpiProviderMeta spiProviderMeta = spiProxyMap.get(identity);
        if (spiProviderMeta != null) {
            Object bean = SpiCache.spiProviderInstanceBeanCache.get(spiProviderMeta.getClassName());
            allBean.add(bean);
            return;
        }
        // 本地实现没有找到 尝试寻找remote实现
        findRemoteSpiProvider(identity, spiMeta.getInterfaceName(), allBean);
    }

    /**
     * 查询远端SPI实现
     */
    private void findRemoteSpiProvider(String identity, String interfaceName, List<Object> allBean) {
        ServiceLoader<SpiRemoteApi> spiRemoteApis = ServiceLoader.load(SpiRemoteApi.class);
        for (SpiRemoteApi spiRemoteApi : spiRemoteApis) {
            Object remoteSpiProvider = spiRemoteApi.findRemoteSpiProvider(interfaceName, identity);
            if (remoteSpiProvider != null) {
                allBean.add(remoteSpiProvider);
            }
        }
        if (CollectionUtils.isEmpty(allBean)) {
            throw new SpiRuntimeException(ErrorCode.NOT_FIND_SPI_PROVIDER, spiMeta.getInterfaceName());
        }
    }

    /**
     * 找到叠加的所有产品实现
     */
    private void findAllProductRunningBean(String identity, List<Object> allBean) {
        List<RelationMeta> relationMetasCache = SpiCache.relationMetasCache;
        if (CollectionUtils.isEmpty(relationMetasCache)) {
            relationMetasCache = findRemoteProductRelation();
            if (CollectionUtils.isEmpty(relationMetasCache)){
                return;
            }
        }
        for (RelationMeta relationMeta : relationMetasCache) {
            if (!relationMeta.getIdentity().equals(identity)) {
                continue;
            }
            List<String> products = relationMeta.getProducts();
            for (String product : products) {
                SpiProviderMeta spiProviderMeta = spiProxyMap.get(product);
                if (spiProviderMeta == null){
                    // 尝试从远端加载
                    findRemoteSpiProvider(product, spiMeta.getInterfaceName(), allBean);
                }else{
                    Object bean = SpiCache.spiProviderInstanceBeanCache.get(spiProviderMeta.getClassName());
                    if (bean != null) {
                        allBean.add(bean);
                    }
                }
            }
        }
    }

    /**
     * 从远端拉取产品叠加规则
     */
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

    /**
     * 处理Object方法
     */
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
