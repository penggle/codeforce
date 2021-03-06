package com.penglecode.codeforce.common.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 通用结果DTO
 *
 * @param <T>
 * @author pengpeng
 * @version 1.0
 */
@Schema(description="通用结果DTO")
public class Result<T> implements BaseDTO {

	private static final long serialVersionUID = 1L;

	/** 成功与否 */
	@Schema(description="是否成功")
    private boolean success;

    /** 响应结果所属应用的Code(在结果输出出去时由框架拦截设置) */
	@Schema(description="所属应用Code")
    private String app;

    /** 结果代码 */
	@Schema(description="结果代码")
    private String code;
    
    /** 结果消息 */
	@Schema(description="结果消息")
    private String message;
    
    /** 结果数据 */
	@Schema(description="结果数据")
    private T data;

	protected Result() {
	}

	protected Result(boolean success, String app, String code, String message, T data) {
		this.success = success;
		this.app = app;
		this.code = code;
		this.message = message;
		this.data = data;
	}

	protected Result(Result<T> result) {
		this.success = result.isSuccess();
		this.app = result.getApp();
		this.code = result.getCode();
		this.message = result.getMessage();
		this.data = result.getData();
	}

	public boolean isSuccess() {
		return success;
	}

	protected void setSuccess(boolean success) {
		this.success = success;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getCode() {
		return code;
	}

	protected void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	protected void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}
	
	protected void setData(T data) {
		this.data = data;
	}

	public static Builder success() {
		return new Builder(Boolean.TRUE, null, "200", "OK");
	}
	
	public static Builder failure() {
		return new Builder(Boolean.FALSE, null, "500", "Internal Server Error");
	}
	
	@Override
	public String toString() {
		return "Result [success=" + success + ", app=" + app + ", code=" + code + ", message="
				+ message + ", data=" + data + "]";
	}

	public Map<String,Object> asMap() {
		Map<String,Object> map = new LinkedHashMap<>();
		map.put("app", app);
		map.put("success", success);
		map.put("code", code);
		map.put("message", message);
		map.put("data", null);
		return map;
	}
	
	public static class Builder {
		
	    private final boolean success;

	    private String app;

	    private String code;
	    
	    private String message;
	    
	    private Object data;

		Builder(boolean success, String app, String code, String message) {
			this.success = success;
			this.app = app;
			this.code = code;
			this.message = message;
		}

		public Builder app(String app) {
			this.app = app;
			return this;
		}

		public Builder code(String code) {
			this.code = code;
			return this;
		}
		
		public Builder message(String message) {
			this.message = message;
			return this;
		}
		
		public Builder data(Object data) {
			this.data = data;
			return this;
		}
		
		@SuppressWarnings("unchecked")
		public <T> Result<T> build() {
			return new Result<>(success, app, code, message, (T) data);
		}
	    
	}
	
}
