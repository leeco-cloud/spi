package com.lee.spi.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * spi实现定义
 * @author yanhuai lee
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface SpiProvider {

    /**
     * 当前spi实现对应的业务身份
     */
    String identityCode();

    /**
     * spi实现描述
     */
    String desc() default "";

}
