package com.lee.spi.core.spring;

/**
 * ThreadContextBeanSelectionStrategy
 *
 * @author yanhuai lee
 */
public class ThreadContextBeanSelectionStrategy {

    public static ThreadLocal<String> BEAN_NAME_HOLDER = new ThreadLocal<>();

}
