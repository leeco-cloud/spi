package com.lee.spi.core.exception;

import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;

@Getter
public enum ErrorCode {

    LOAD_CONFIG_ERROR(1001, "加载配置失败:{}"),

    UN_REGISTER_IDENTITY(2001, "未定义的业务身份: {}"),
    UN_REGISTER_PRODUCT(2002, "未定义的产品身份: {}"),

    REPEAT_DEFAULT_PROVIDER(2003, "重复定义默认实现: {}"),
    MUCH_IDENTICAL_IDENTITY(2004, "SPI：{} 存在多个相同身份code: {}"),
    NOT_FIND_SPI_PROVIDER(2005, "未找到SPI实现:{}"),
    NOT_FIND_SPI_PROVIDER_BY_IDENTITY(2006, "业务身份：{} : 未找到SPI实现:{}"),
    NOT_FIND_SPI_PROVIDER_BY_PRODUCT(2007, "产品身份：{} : 未找到SPI实现:{}");


    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String format() {
        return "[" + code + "]" + message;
    }

    public String format(Object... args) {
        return "[" + code + "]" + MessageFormatter.arrayFormat(message, args).getMessage();
    }

}
