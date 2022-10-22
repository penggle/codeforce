package com.penglecode.codeforce.common.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author peng2.peng
 * @version 1.0.0
 */
public abstract class BaseResponse<T> implements BaseDTO {

    /** traceId */
    @Schema(description="traceId")
    private String traceId;

    /** 成功与否 */
    @Schema(description="是否成功")
    private boolean success;

    /** 结果代码 */
    @Schema(description="结果代码")
    private String code;

    /** 结果消息 */
    @Schema(description="结果消息")
    private String message;

    /** 结果数据 */
    @Schema(description="结果数据")
    private T data;

    protected BaseResponse() {
    }

    protected BaseResponse(boolean success, String code, String message, T data) {
        this(null, success, code, message, data);
    }

    protected BaseResponse(String traceId, boolean success, String code, String message, T data) {
        this.traceId = traceId;
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Map<String,Object> asMap() {
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("traceId", getTraceId());
        map.put("success", isSuccess());
        map.put("code", getCode());
        map.put("message", getMessage());
        map.put("data", getData());
        return map;
    }

    @Override
    public String toString() {
        return "traceId=" + traceId + ", success=" + success + ", code=" + code + ", message=" + message + ", data=" + data;
    }

}
