package com.penglecode.codeforce.common.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 通用响应DTO
 *
 * @author peng2.peng
 * @version 1.0.0
 */
@Schema(description="通用响应DTO")
public class DefaultResponse<T> extends BaseResponse<T> {

    protected DefaultResponse() {
    }

    protected DefaultResponse(boolean success, String code, String message, T data) {
        super(success, code, message, data);
    }

    protected DefaultResponse(String traceId, boolean success, String code, String message, T data) {
        super(traceId, success, code, message, data);
    }

    public static <T> Builder<T> ok() {
        return new Builder<>(null, true, "200", "OK");
    }

    public static <T> Builder<T> error() {
        return new Builder<>(null, false, "500", "Internal Server Error");
    }

    @Override
    public String toString() {
        return "{" + super.toString() + "}";
    }

    public static class Builder<T> {

        private String traceId;

        private final boolean success;

        private String code;

        private String message;

        private T data;

        Builder(String traceId, boolean success, String code, String message) {
            this.traceId = traceId;
            this.success = success;
            this.code = code;
            this.message = message;
        }

        public Builder<T> traceId(String traceId) {
            this.traceId = traceId;
            return this;
        }

        public Builder<T> code(String code) {
            this.code = code;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public DefaultResponse<T> build() {
            return new DefaultResponse<>(traceId, success, code, message, data);
        }

    }

}
