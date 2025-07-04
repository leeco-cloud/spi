package com.lee.spi.core.annotation;

import org.springframework.stereotype.Component;

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
@Component
public @interface SpiProvider {

    /**
     * 当前spi实现对应的业务身份
     */
    String identityCode();

    /**
     * spi实现描述
     */
    String desc() default "";

    /**
     * 是否是默认实现
     * true : 当对应的SPI找不到具体实现时候 则执行当前默认实现
     */
    boolean isDefault() default false;

}
