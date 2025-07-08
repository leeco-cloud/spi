package com.lee.spi.remote.ipc.api;

import com.lee.ipc.common.api.ClientApi;
import com.lee.ipc.common.api.ServerApi;
import com.lee.ipc.common.communication.server.ServiceBean;
import com.lee.ipc.common.serialization.common.SerializerType;
import com.lee.spi.core.meta.RelationMeta;
import com.lee.spi.core.remote.SpiRemoteApi;
import com.lee.spi.core.remote.SpiRemoteProxy;
import com.lee.spi.core.util.ProxyUtils;
import com.lee.spi.remote.ipc.Inner.SpiRemoteIpcInner;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * spi remote ipc 实现
 *
 * @author yanhuai lee
 */
public class SpiRemoteIpcImpl implements SpiRemoteApi {

    private final Map<String, Object> spiRemoteProxyMap = new HashMap<>();

    @Override
    public Object findRemoteSpiProvider(String interfaceName, String code) {
        List<ServiceBean> serviceBeans = ClientApi.allIpcService();
        if (serviceBeans != null && !serviceBeans.isEmpty()) {
            for (ServiceBean serviceBean : serviceBeans) {
                if (serviceBean.getServiceInterface().getName().equals(interfaceName) && serviceBean.getVersion().equals(code)) {
                    String serviceUniqueKey = serviceBean.getServiceUniqueKey();
                    Object spiRemoteProxy = spiRemoteProxyMap.get(serviceUniqueKey);
                    if (spiRemoteProxy != null) {
                        return spiRemoteProxy;
                    }
                    SpiRemoteProxy proxy = new SpiRemoteProxy(interfaceName, code);
                    Object remoteProxy;
                    try {
                        remoteProxy = ProxyUtils.getRemoteProxy(Class.forName(interfaceName), proxy);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    spiRemoteProxyMap.put(serviceUniqueKey, remoteProxy);
                    return remoteProxy;
                }
            }
        }
        return null;
    }

    @Override
    public Object invokeRemoteSpi(String interfaceName, Method method, Object[] args, String code, Integer timeout) {
        Class<?> interfaceClass;
        try {
            interfaceClass = Class.forName(interfaceName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ClientApi.invoke(interfaceClass, method, args, code, SerializerType.FURY, timeout);
    }

    @Override
    public void registerRemoteSpiProvider(String interfaceName, String code, Object spiProvider) {
        Class<?> interfaceClass;
        try {
            interfaceClass = Class.forName(interfaceName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ServerApi.registerServer(interfaceClass, spiProvider, code, SerializerType.FURY);
    }

    @Override
    public List<RelationMeta> findRemoteProductRelation() {
        List<RelationMeta> result = new ArrayList<>();
        Set<String> containerNames = new HashSet<>();
        List<ServiceBean> serviceBeans = ClientApi.allIpcService();
        if (serviceBeans != null && !serviceBeans.isEmpty()) {
            for (ServiceBean serviceBean : serviceBeans) {
                String containerName = serviceBean.getContainerName();
                containerNames.add(containerName);
            }
        }
        for (String containerName : containerNames) {
            try {
                Method findRelationMetas = SpiRemoteIpcInner.class.getMethod("findRelationMetas");
                Object invokedByContainer = ClientApi.invokeByContainer(containerName, SpiRemoteIpcInner.class, findRelationMetas, null, "1.0.0", SerializerType.FURY, 5000);
                if (invokedByContainer != null) {
                    List<RelationMeta> relationMetaList = (List<RelationMeta>) invokedByContainer;
                    result.addAll(relationMetaList);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (!CollectionUtils.isEmpty(result)) {
            // 根据identity合并product结果
            return mergeRelationMetas(result);
        }
        return result;
    }

    private List<RelationMeta> mergeRelationMetas(List<RelationMeta> list) {
        // 使用LinkedHashMap保持插入顺序，LinkedHashSet保持产品顺序
        Map<String, Set<String>> mergedMap = new LinkedHashMap<>();

        for (RelationMeta meta : list) {
            String identity = meta.getIdentity();
            // 跳过identity为空的对象（根据需求可选）
            if (identity == null) continue;

            List<String> products = meta.getProducts();
            // 处理空列表：若products为null则视为空列表
            Set<String> productSet = (products == null) ? new LinkedHashSet<>() : new LinkedHashSet<>(products);

            // 合并相同identity的products（自动去重）
            mergedMap.merge(identity, productSet, (existingSet, newSet) ->
                    {
                        existingSet.addAll(newSet);
                        return existingSet;
                    }
            );
        }

        // 转换Map为RelationMeta对象列表
        return mergedMap.entrySet().stream().map(entry ->
                new RelationMeta(entry.getKey(), new ArrayList<>(entry.getValue())))
                .collect(Collectors.toList());
    }

}
