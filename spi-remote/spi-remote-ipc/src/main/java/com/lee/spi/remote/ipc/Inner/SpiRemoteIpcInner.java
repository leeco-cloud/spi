package com.lee.spi.remote.ipc.Inner;

import com.lee.spi.core.meta.RelationMeta;

import java.util.List;

/**
 * @author yanhuai lee
 */
public interface SpiRemoteIpcInner {

    List<RelationMeta> findRelationMetas();

}
