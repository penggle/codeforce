package com.penglecode.codeforce.common.model;

/**
 * 基础请求
 *
 * @author peng2.peng
 * @version 1.0.0
 */
public abstract class BaseRequest implements BaseDTO {

    private String traceId;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

}
