package com.lee.spi.core.cache;


import com.lee.spi.core.config.CommonConfig;
import com.lee.spi.core.exception.ErrorCode;
import com.lee.spi.core.exception.SpiRuntimeException;
import com.lee.spi.core.loader.*;
import com.lee.spi.core.meta.SpiMeta;
import com.lee.spi.core.meta.SpiProviderMeta;
import com.lee.spi.core.proxy.SpiProxy;
import com.lee.spi.core.util.ProxyUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

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

    public static void start() {
        if (!init.get()){
            init();
        }
        init.set(true);
    }

    private static void init(){
        try{
            SpiCache.identityMetasCache = IdentityLoader.load();
            SpiCache.productMetasCache = ProductLoader.load();
            SpiCache.relationMetasCache = RelationLoader.load();
            SpiCache.spiMetasCache = SpiLoader.load();
            if (SpiCache.spiMetasCache != null) {
                for (SpiMeta spiMeta : SpiCache.spiMetasCache) {
                    SpiProxy spiProxy = SpiCache.spiSpiProxyCache.get(spiMeta.getInterfaceName());
                    if (spiProxy == null){
                        spiProxy = new SpiProxy();
                    }
                    String interfaceName = spiMeta.getInterfaceName();
                    Class<?> interfaceType = Class.forName(interfaceName);
                    List<SpiProviderMeta> spiProviderMetas = SpiProviderLoader.load(interfaceType);
                    boolean existDefaultProvider = false;
                    for (SpiProviderMeta spiProviderMeta : spiProviderMetas) {

                        String identityCode = spiProviderMeta.getCode();

                        Boolean isDefault = spiProviderMeta.getIsDefault();
                        if (BooleanUtils.isTrue(isDefault)){
                            if (existDefaultProvider){
                                throw new SpiRuntimeException(ErrorCode.REPEAT_DEFAULT_PROVIDER, spiProviderMeta.getClassName() + " : 请勿重复定义默认实现");
                            }
                            identityCode = String.format(CommonConfig.defaultIdentityCode, spiMeta.getInterfaceName());
                            existDefaultProvider = true;
                        }else{
                            if (StringUtils.isBlank(identityCode)){
                                throw new RuntimeException(spiProviderMeta.getClassName() + " : 未定义 identity code");
                            }
                        }

                        Map<String, SpiProviderMeta> proxyMap = spiProxy.getSpiProxyMap();
                        if (proxyMap == null){
                            proxyMap = new HashMap<>();
                            spiProxy.setSpiProxyMap(proxyMap);
                        }

                        SpiProviderMeta oldSpiProvider = proxyMap.get(identityCode);
                        if (oldSpiProvider != null){
                            throw new SpiRuntimeException(ErrorCode.MUCH_IDENTICAL_IDENTITY, interfaceName ,identityCode);
                        }

                        proxyMap.put(identityCode, spiProviderMeta);

                        spiProxy.setSpiMeta(spiMeta);
                        spiProxy.setSpiProxyMap(proxyMap);

                        Object proxy = ProxyUtils.getProxy(interfaceType, spiProxy);

                        SpiCache.spiProxyInstanceCache.put(spiMeta.getInterfaceName(), proxy);
                        SpiCache.spiSpiProxyCache.put(spiMeta.getInterfaceName(), spiProxy);
                        SpiCache.spiProviderClassNames.add(spiProviderMeta.getClassName());
                    }
                }
            }
        } catch (Exception e) {
            throw new SpiRuntimeException(e);
        }
    }

}
