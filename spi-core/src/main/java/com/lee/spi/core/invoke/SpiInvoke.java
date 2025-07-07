package com.lee.spi.core.invoke;

import com.lee.spi.core.cache.SpiCache;
import com.lee.spi.core.cache.SpiCacheLoader;
import com.lee.spi.core.exception.ErrorCode;
import com.lee.spi.core.exception.SpiRuntimeException;
import com.lee.spi.core.meta.IdentityMeta;

import java.util.List;

/**
 * spi调用入口
 * @author yanhuai lee
 */
public class SpiInvoke {

    public static IdentityInvoke identity(String code) {
        SpiCacheLoader.start();
        List<IdentityMeta> identityMetasCache = SpiCache.identityMetasCache;
        if (identityMetasCache == null) {
            throw new SpiRuntimeException(ErrorCode.UN_REGISTER_IDENTITY, code);
        }
        for (IdentityMeta identityMeta : identityMetasCache) {
            if (identityMeta.getCode().equals(code)) {
                return new IdentityInvoke(code);
            }
        }
        throw new SpiRuntimeException(ErrorCode.UN_REGISTER_IDENTITY, code);
    }

}
