package com.lee.spi.core.cache;

import com.lee.spi.core.meta.IdentityMeta;
import com.lee.spi.core.meta.SpiMeta;
import com.lee.spi.core.proxy.SpiProxy;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * spi元数据缓存
 * @author yanhuai lee
 */
public class SpiCache {

    public static List<IdentityMeta> identityMetasCache;

    public static List<SpiMeta> spiMetasCache;

    public static Set<String> spiProviderClassNames = new TreeSet<>();

    public static Map<SpiMeta, SpiProxy> spiProviderMetasCache = new ConcurrentHashMap<>();

    public static Map<String, SpiProxy> cache = new ConcurrentHashMap<>();

    public static Map<String, Object> spiProxyInstanceCache = new ConcurrentHashMap<>();

}
