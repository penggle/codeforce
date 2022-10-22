package com.penglecode.codeforce.common.support;

/**
 * 默认的ErrorCode实现
 *
 * 整个错误码定义，是基于HTTP状态码之上。对于整个系统中的各个微服务的特定化错误码定义约定如下：
 *  1、自定义的错误码都是以某个HTTP状态码为前缀，后补三位数字凑成6位数字的错误码，也就是说，错误码分两种：
 *      (1)、原始的3位HTTP状态码错误码，例如ERR403("403", "请求未授权")
 *      (2)、基于原始的3位HTTP状态码扩展的错误码，例如ERR500001("500001", "用户账户状态异常")
 *  2、参数校验错误的自定义错误码必须归属在ERR40X(即HTTP-40X)下面，即参数校验错误码必须以ERR40X开头命名，code值必须以40X开头。
 *    考虑到若为每个参数校验都定义专有的校验错误码那太多了并不现实，我们可以为核心数据定义专有错误码，
 *    例如：ERR400001("400001", "用户ID不能为空"),ERR404002("404002", "用户账户不存在"),ERR409001("409001", "用户名已存在");
 *    其余一般性校验或者干脆都以ERR400代替一切参数校验类错误码也是可以的
 *  3、业务类错误码，都统一归属到ERR500下，即以HTTP-500作为前缀，例如ERR500001("500001", "用户账户余额不足")
 *  4、登录认证校验类归属到ERR401下
 *  5、操作权限校验类归属到ERR403下
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class CustomErrorCode implements ErrorCode {

    private final String code;

    private final String message;

    public CustomErrorCode(String code, String message) {
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