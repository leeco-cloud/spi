package com.lee.spi.remote.rpc.api;

import com.lee.spi.core.meta.RelationMeta;
import com.lee.spi.core.remote.SpiRemoteApi;
import com.lee.spi.core.remote.SpiRemoteProxy;
import com.lee.spi.core.util.ProxyUtils;
import org.apache.dubbo.config.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * spi remote rpc 临时实现
 * // todo 待完善
 * @author yanhuai lee
 */
public class SpiRemoteRpcImpl implements SpiRemoteApi {

    private static final ApplicationConfig consumerApplication = new ApplicationConfig();
    private static final ApplicationConfig providerApplication = new ApplicationConfig();
    private static final RegistryConfig registry = new RegistryConfig();

    @Override
    public Object findRemoteSpiProvider(String interfaceName, String code) {
        SpiRemoteProxy proxy = new SpiRemoteProxy(interfaceName, code);
        Object remoteProxy;
        try {
            remoteProxy = ProxyUtils.getRemoteProxy(Class.forName(interfaceName), proxy);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return remoteProxy;
    }

    @Override
    public Object invokeRemoteSpi(String interfaceName, Method method, Object[] args, String code, Integer timeout) {
        Class<?> interfaceClass;
        try {
            interfaceClass = Class.forName(interfaceName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        consumerApplication.setName("SPI-consumer");
        registry.setAddress("zookeeper://127.0.0.1:2181");

        ReferenceConfig reference = new ReferenceConfig<>();
        reference.setApplication(consumerApplication);
        reference.setRegistry(registry);
        reference.setInterface(interfaceClass);
        reference.setId(interfaceName + "#" + code);
        reference.setVersion("1.0.0");
        reference.setTimeout(timeout);

        Object spiProviderProxy = reference.get();

        try {
            return method.invoke(spiProviderProxy, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerRemoteSpiProvider(String interfaceName, String code, Object spiProvider) {
        Class<?> interfaceClass;
        try {
            interfaceClass = Class.forName(interfaceName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        providerApplication.setName("SPI-provider");
        registry.setAddress("zookeeper://127.0.0.1:2181");

        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("dubbo");
        protocol.setPort(20880);

        ServiceConfig service = new ServiceConfig<>();
        service.setApplication(providerApplication);
        service.setRegistry(registry);
        service.setProtocol(protocol);
        service.setInterface(interfaceClass);
        service.setRef(spiProvider);
        service.setId(interfaceName + "#" + code);
        service.setVersion("1.0.0");

        service.export();
    }

    @Override
    public List<RelationMeta> findRemoteProductRelation() {
        // todo
        return null;
    }

}
