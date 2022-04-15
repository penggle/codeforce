package com.penglecode.codeforce.mybatistiny.exception;

/**
 * Mybatis类型处理异常
 *
 * @author pengpeng
 * @version 1.0
 */
public class TypeHandleException extends RuntimeException {

    public TypeHandleException() {
    }

    public TypeHandleException(String message) {
        super(message);
    }

    public TypeHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeHandleException(Throwable cause) {
        super(cause);
    }

}
