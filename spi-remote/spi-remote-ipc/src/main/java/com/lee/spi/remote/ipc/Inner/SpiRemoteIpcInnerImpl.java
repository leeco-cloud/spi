package com.lee.spi.remote.ipc.Inner;

import com.lee.ipc.common.annotation.IpcProvider;
import com.lee.spi.core.cache.SpiCache;
import com.lee.spi.core.meta.RelationMeta;

import java.util.List;

/**
 * @author yanhuai lee
 */
@IpcProvider
public class SpiRemoteIpcInnerImpl implements SpiRemoteIpcInner {

    @Override
    public List<RelationMeta> findRelationMetas() {
        return SpiCache.relationMetasCache;
    }

}
