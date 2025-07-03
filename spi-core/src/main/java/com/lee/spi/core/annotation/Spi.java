package com.lee.spi.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * spi定义
 * @author yanhuai lee
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Spi {

    /**
     * SPI 唯一code 默认是方法签名
     */
    String code() default "";

    /**
     * SPI 名称
     */
    String name() default "";

    /**
     * SPI 描述
     */
    String desc() default "";

    int priority() default 1000;

}
