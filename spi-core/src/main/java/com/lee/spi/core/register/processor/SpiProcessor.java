package com.lee.spi.core.register.processor;

import com.lee.spi.core.annotation.Spi;

import java.lang.annotation.Annotation;

/**
 * Spi注解编译期执行器
 * @author yanhuai lee
 * @see com.lee.spi.core.annotation.Spi
 */
public class SpiProcessor extends CommonProcessor {

    @Override
    public Class<? extends Annotation> spiAnnotation() {
        return Spi.class;
    }

}