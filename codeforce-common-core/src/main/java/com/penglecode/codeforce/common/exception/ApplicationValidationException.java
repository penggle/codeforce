package com.penglecode.codeforce.common.exception;

/**
 * 应用数据校验异常
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class ApplicationValidationException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public ApplicationValidationException(String code, String message) {
        super(code, message);
    }

    public ApplicationValidationException(Throwable cause) {
        super(cause);
    }

    public ApplicationValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationValidationException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

}
