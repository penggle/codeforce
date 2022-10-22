package com.penglecode.codeforce.common.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 基础请求
 *
 * @author peng2.peng
 * @version 1.0.0
 */
public abstract class BaseRequest implements BaseDTO {

    @Schema(description="traceId(唯一,一般为UUID)")
    private String traceId;

    @Schema(description="是否级联操作(某些业务场景下需要)", defaultValue="false")
    private boolean cascade;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public boolean isCascade() {
        return cascade;
    }

    public void setCascade(boolean cascade) {
        this.cascade = cascade;
    }

}
