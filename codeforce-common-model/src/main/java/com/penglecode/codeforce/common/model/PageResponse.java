package com.penglecode.codeforce.common.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

/**
 * 通用分页响应DTO
 *
 * @author peng2.peng
 * @version 1.0.0
 */
@Schema(description="通用分页响应DTO")
public class PageResponse<T> extends BaseResponse<List<T>> {

    /** 分页数据 */
    @Schema(description="分页数据")
    private Page page;

    public PageResponse() {
    }

    public PageResponse(boolean success, String code, String message, List<T> data, Page page) {
        super(success, code, message, data);
        this.page = page;
    }

    public PageResponse(String traceId, boolean success, String code, String message, List<T> data, Page page) {
        super(traceId, success, code, message, data);
        this.page = page;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public static <T> Builder<T> ok() {
        return new Builder<>(null, true, "200", "OK");
    }

    public static <T> Builder<T> error() {
        return new Builder<>(null, false, "500", "Internal Server Error");
    }

    @Override
    public String toString() {
        return "{" + super.toString() + ", page=" + page + "}";
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> map = super.asMap();
        map.put("page", page);
        return map;
    }

    public static class Builder<T> {

        private String traceId;

        private final boolean success;

        private String code;

        private String message;

        private List<T> data;

        private Page page;

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

        public Builder<T> data(List<T> data) {
            this.data = data;
            return this;
        }

        public Builder<T> page(Page page) {
            this.page = page;
            return this;
        }

        public PageResponse<T> build() {
            return new PageResponse<>(traceId, success, code, message, data, page);
        }

    }
    
}
