package com.penglecode.codeforce.common.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * @author pengpeng
 * @version 1.0
 */
public class NestedApplicationException extends NestedRuntimeException {

    private static final long serialVersionUID = 1L;

    public NestedApplicationException(String msg) {
        super(msg);
    }

    public NestedApplicationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
