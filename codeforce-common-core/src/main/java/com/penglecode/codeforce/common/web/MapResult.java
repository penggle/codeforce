package com.penglecode.codeforce.common.web;

import com.penglecode.codeforce.common.model.BaseDTO;
import com.penglecode.codeforce.common.model.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * 对应{@link Result}的Map形式，用于全局异常处理
 *
 * @author pengpeng
 * @version 1.0
 */
public final class MapResult extends HashMap<String,Object> implements BaseDTO {

    private static final long serialVersionUID = 1L;

    public MapResult() {
    }

    public MapResult(Map<? extends String, ?> m) {
        super(m);
    }

    public Boolean getSuccess() {
        return (Boolean) get("success");
    }

    public void setSuccess(Boolean success) {
        put("success", success);
    }

    public String getApp() {
        return (String) get("app");
    }

    public void setApp(String app) {
        put("app", app);
    }

    public String getCode() {
        return (String) get("code");
    }

    public void setCode(String code) {
        put("code", code);
    }

    public String getMessage() {
        return (String) get("message");
    }

    public void setMessage(String message) {
        put("message", message);
    }

    public Object getData() {
        return get("data");
    }

    public void setData(Object data) {
        put("data", data);
    }

}
