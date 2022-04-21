package com.penglecode.codeforce.common.support;

/**
 * 默认的ErrorCode实现
 *
 * @author pengpeng
 * @version 1.0
 */
public class DefaultErrorCode implements ErrorCode {

    private final String code;

    private final String message;

    public DefaultErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 错误信息
     * @param overrides   - 如果不为空，则使用该消息
     * @return
     */
    @Override
    public String getMessage(String... overrides) {
        if(overrides != null && overrides.length == 1 && overrides[0] != null && !"".equals(overrides[0].trim())) {
            return overrides[0].trim();
        }
        return getMessage();
    }
}