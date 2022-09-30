package com.penglecode.codeforce.common.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * 通用分页查询结果DTO
 * 
 * @param <T>
 * @author pengpeng
 * @version 1.0.0
 */
@Schema(description="通用分页查询结果DTO")
public class PageResult<T> extends Result<T> {

	private static final long serialVersionUID = 1L;
	
	/** 当存在分页查询时此值为总记录数 */
	@Schema(description="总记录数", defaultValue="0")
	private int totalRowCount;

	protected PageResult() {}

	protected PageResult(Result<T> result, int totalRowCount) {
		super(result);
		this.totalRowCount = totalRowCount;
	}

	public int getTotalRowCount() {
		return totalRowCount;
	}

	protected void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

	public static PageBuilder success() {
		return new PageBuilder(Boolean.TRUE, null, "200", "OK");
	}

	public static PageBuilder failure() {
		return new PageBuilder(Boolean.FALSE, null, "500", "Internal Server Error");
	}

	@Override
	public String toString() {
		return "PagedResult [success=" + isSuccess() + ", app=" + getApp() + ", code=" + getCode() + ", message="
				+ getMessage() + ", data=" + getData() + ", totalRowCount=" + getTotalRowCount() + "]";
	}

	@Override
	public Map<String, Object> asMap() {
		Map<String, Object> map = super.asMap();
		map.put("totalRowCount", totalRowCount);
		return map;
	}

	public static class PageBuilder extends Builder {

		private int totalRowCount = 0;

		PageBuilder(boolean success, String app, String code, String message) {
			super(success, app, code, message);
		}

		public PageBuilder totalRowCount(int totalRowCount) {
			this.totalRowCount = totalRowCount;
			return this;
		}

		@Override
		public PageBuilder app(String app) {
			return (PageBuilder) super.app(app);
		}

		@Override
		public PageBuilder code(String code) {
			return (PageBuilder) super.code(code);
		}

		@Override
		public PageBuilder message(String message) {
			return (PageBuilder) super.message(message);
		}

		@Override
		public PageBuilder data(Object data) {
			return (PageBuilder) super.data(data);
		}

		@Override
		public <T> PageResult<T> build() {
			return new PageResult<>(super.build(), totalRowCount);
		}
		
	}
	
}
