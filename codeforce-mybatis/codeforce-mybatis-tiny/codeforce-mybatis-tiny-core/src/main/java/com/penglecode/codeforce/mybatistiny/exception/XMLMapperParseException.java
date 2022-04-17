package com.penglecode.codeforce.mybatistiny.exception;

/**
 * Mybatis XML-Mapper解析异常
 *
 * @author pengpeng
 * @version 1.0
 */
public class XMLMapperParseException extends RuntimeException {

    public XMLMapperParseException(String message) {
        super(message);
    }

    public XMLMapperParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public XMLMapperParseException(Throwable cause) {
        super(cause);
    }

}
