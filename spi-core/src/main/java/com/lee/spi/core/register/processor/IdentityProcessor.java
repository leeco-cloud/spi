package com.lee.spi.core.register.processor;

import com.lee.spi.core.annotation.Identity;

import java.lang.annotation.Annotation;

/**
 * Identity注解编译期执行器
 * @author yanhuai lee
 * @see com.lee.spi.core.annotation.Identity
 */
public class IdentityProcessor extends CommonProcessor {

    @Override
    public Class<? extends Annotation> spiAnnotation() {
        return Identity.class;
    }

}