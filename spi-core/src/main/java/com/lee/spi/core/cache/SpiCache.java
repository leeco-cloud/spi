package com.lee.spi.core.cache;

import com.lee.spi.core.meta.IdentityMeta;
import com.lee.spi.core.meta.ProductMeta;
import com.lee.spi.core.meta.RelationMeta;
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

    public static List<ProductMeta> productMetasCache;

    public static List<RelationMeta> relationMetasCache;

    public static List<SpiMeta> spiMetasCache;

    public static Set<String> spiProviderClassNames = new TreeSet<>();

    public static Map<String, Object> spiProviderInstanceBeanCache = new ConcurrentHashMap<>();

    public static Map<String, SpiProxy> spiSpiProxyCache = new ConcurrentHashMap<>();

    public static Map<String, Object> spiProxyInstanceCache = new ConcurrentHashMap<>();

}
