package com.lee.spi.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 身份定义
 * @author yanhuai lee
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface Identity {

    /**
     * 业务身份唯一code
     */
    String code();

    /**
     * 业务身份名
     */
    String name() default "";

    /**
     * 业务身份描述
     */
    String desc() default "";

    int priority() default 1000;

}
