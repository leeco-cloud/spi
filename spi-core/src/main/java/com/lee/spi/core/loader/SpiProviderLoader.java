package com.lee.spi.core.loader;

import com.lee.spi.core.annotation.SpiProvider;
import com.lee.spi.core.meta.SpiMeta;
import com.lee.spi.core.meta.SpiProviderMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * spi实现元数据信息上报
 * @author yanhuai lee
 */
public class SpiProviderLoader {

    public static <T> List<SpiProviderMeta> load(Class<T> spiInterface) {
        List<SpiProviderMeta> data = new ArrayList<>();
        List<SpiMeta> spiMetas = SpiLoader.load();
        if (spiMetas == null || spiMetas.isEmpty()) {
            return data;
        }
        ServiceLoader<T> spiImplObjects = ServiceLoader.load(spiInterface);
        for (Object spiImplObj : spiImplObjects) {
            SpiProvider annotation = spiImplObj.getClass().getAnnotation(SpiProvider.class);
            if (annotation != null) {
                SpiProviderMeta spiProviderMeta = new SpiProviderMeta(
                        spiImplObj.getClass().getName(), spiImplObj.getClass(),
                        spiInterface.getName(), spiInterface, annotation.identityCode(), annotation.desc(), annotation.isDefault()
                );
                data.add(spiProviderMeta);
            }
        }
        return data;
    }

}
