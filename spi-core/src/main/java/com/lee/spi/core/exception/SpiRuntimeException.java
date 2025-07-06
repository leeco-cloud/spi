package com.lee.spi.core.exception;

import lombok.Getter;

/**
 * 运行时异常
 * @author yanhuai lee
 */
@Getter
public class SpiRuntimeException extends RuntimeException {

    public SpiRuntimeException(Integer errorCode, String message) {
        super(message);
    }

    public SpiRuntimeException(Throwable throwable) {
        super(throwable);
    }

    public SpiRuntimeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public SpiRuntimeException(ErrorCode errorCode, Object... args) {
        super(errorCode.format(args));
    }

    public SpiRuntimeException(Throwable throwable, ErrorCode errorCode) {
        super(errorCode.getMessage(), throwable);
    }

    public SpiRuntimeException(Throwable throwable, ErrorCode errorCode, Object... args) {
        super(errorCode.format(args), throwable);
    }

}
