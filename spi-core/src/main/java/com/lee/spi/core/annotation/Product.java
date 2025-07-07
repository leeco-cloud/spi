package com.lee.spi.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 产品定义
 * @author yanhuai lee
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface Product {

    /**
     * 产品身份唯一code
     */
    String code();

    /**
     * 产品身份名
     */
    String name() default "";

    /**
     * 产品身份描述
     */
    String desc() default "";

    /**
     * 优先级 数值越低优先级越高
     */
    int priority() default 1000;

}
