package com.lee.spi.remote.rpc.api;

import com.lee.spi.core.meta.RelationMeta;
import com.lee.spi.core.remote.SpiRemoteApi;

import java.lang.reflect.Method;
import java.util.List;

/**
 * spi remote rpc 实现
 * @author yanhuai lee
 */
public class SpiRemoteRpcImpl implements SpiRemoteApi {

    @Override
    public Object findRemoteSpiProvider(String interfaceName, String code) {
        // todo
        return null;
    }

    @Override
    public Object invokeRemoteSpi(String interfaceName, Method method, Object[] args, String code, Integer timeout) {
        // todo
        return null;
    }

    @Override
    public void registerRemoteSpiProvider(String interfaceName, String code, Object spiProvider) {
        // todo
    }

    @Override
    public List<RelationMeta> findRemoteProductRelation() {
        // todo
        return null;
    }

}
