package com.lee.spi.core.cache;


import com.lee.spi.core.loader.IdentityLoader;
import com.lee.spi.core.loader.SpiLoader;
import com.lee.spi.core.loader.SpiProviderLoader;
import com.lee.spi.core.meta.SpiMeta;
import com.lee.spi.core.meta.SpiProviderMeta;
import com.lee.spi.core.proxy.SpiProxy;
import com.lee.spi.core.util.ProxyUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * spi缓存加载器
 * @author yanhuai lee
 */
public class SpiCacheLoader {

    private static final AtomicBoolean init = new AtomicBoolean(false);

    public static void init(){
        if (init.get()){
            return;
        }
        try{
            SpiCache.identityMetasCache = IdentityLoader.load();
            SpiCache.spiMetasCache = SpiLoader.load();
            if (SpiCache.spiMetasCache != null) {
                for (SpiMeta spiMeta : SpiCache.spiMetasCache) {
                    SpiProxy spiProxy = SpiCache.spiProviderMetasCache.get(spiMeta);
                    if (spiProxy == null){
                        spiProxy = new SpiProxy();
                    }
                    String interfaceName = spiMeta.getInterfaceName();
                    Class<?> interfaceType = Class.forName(interfaceName);
                    List<SpiProviderMeta> spiProviderMetas = SpiProviderLoader.load(interfaceType);
                    for (SpiProviderMeta spiProviderMeta : spiProviderMetas) {

                        String identityCode = spiProviderMeta.getIdentityCode();

                        Map<String, SpiProviderMeta> proxyMap = spiProxy.getSpiProxyMap();
                        if (proxyMap == null){
                            proxyMap = new HashMap<>();
                            spiProxy.setSpiProxyMap(proxyMap);
                        }

                        SpiProviderMeta oldSpiProvider = proxyMap.get(identityCode);
                        if (oldSpiProvider != null){
                            throw new RuntimeException("SPI：" + interfaceName + " 存在多个相同身份code: " + identityCode);
                        }

                        proxyMap.put(identityCode, spiProviderMeta);

                        spiProxy.setSpiMeta(spiMeta);
                        spiProxy.setSpiProxyMap(proxyMap);

                        Object proxy = ProxyUtils.getProxy(interfaceType, spiProxy);
                        SpiCache.spiProxyInstanceCache.put(spiMeta.getInterfaceName(), proxy);
                        SpiCache.cache.put(spiMeta.getInterfaceName(), spiProxy);
                        SpiCache.spiProviderMetasCache.put(spiMeta, spiProxy);

                        SpiCache.spiProviderClassNames.add(spiProviderMeta.getClassName());
                    }
                }
            }
            init.set(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
