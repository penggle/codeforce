package com.penglecode.codeforce.common.codegen.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * 代码自动生成运行时异常
 *
 * @author pengpeng
 * @version 1.0.0
 * @created 2021/1/25 14:48
 */
public class CodegenRuntimeException extends NestedRuntimeException {

    private static final long serialVersionUID = 1L;

    public CodegenRuntimeException(String message) {
        super(message);
    }

    public CodegenRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
