package com.lee.spi.core.remote;

import com.lee.spi.core.meta.RelationMeta;

import java.lang.reflect.Method;
import java.util.List;

/**
 * SPI远程API
 * @author yanhuai lee
 */
public interface SpiRemoteApi {

    Object findRemoteSpiProvider(String interfaceName, String code);

    Object invokeRemoteSpi(String interfaceName, Method method, Object[] args, String code, Integer timeout);

    void registerRemoteSpiProvider(String interfaceName, String code, Object spiProvider);

    List<RelationMeta> findRemoteProductRelation();
}
