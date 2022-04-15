package com.penglecode.codeforce.common.exception;

/**
 * 应用业务异常
 *
 * @author pengpeng
 * @version 1.0
 */
public class ApplicationBizException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public ApplicationBizException(String code, String message) {
        super(code, message);
    }

    public ApplicationBizException(Throwable cause) {
        super(cause);
    }

    public ApplicationBizException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationBizException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

}
