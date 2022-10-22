package com.penglecode.codeforce.common.support;

import com.penglecode.codeforce.common.consts.ApplicationConstants;
import com.penglecode.codeforce.common.exception.ApplicationException;
import com.penglecode.codeforce.common.util.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

/**
 * 错误码定义接口
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
public interface ErrorCode {

    /**
     * 默认通用返回成功结果码
     */
    String DEFAULT_RESULT_CODE_OK = "200";

    /**
     * 默认通用返回错误结果码
     */
    String DEFAULT_RESULT_CODE_ERR = "500";

    /**
     * 错误码
     * @return
     */
    String getCode();

    /**
     * 错误信息
     * @param overrides   - 如果不为空，则使用该消息
     * @return
     */
    String getMessage(String... overrides);

    /**
     * HTTP状态码
     * @return
     */
    default HttpStatus getStatus() {
        return resolve(getCode());
    }

    /**
     * 默认的解析HTTP状态码的实现
     * @param code
     * @return
     */
    static HttpStatus resolve(String code) {
        HttpStatus status = null;
        try {
            status = HttpStatus.resolve(Integer.parseInt(code.substring(0, 3)));
        } catch (Exception e) {
            //ignored
        }
        return status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status;
    }

    /**
     * 根据异常解析其错误码
     * @param ex            - 异常对象
     * @param status        - HTTP状态码(可选)
     * @return
     */
    static ErrorCode resolve(Throwable ex, HttpStatus status) {
        return resolve(ex, status, "message.request.failed");
    }

    /**
     * 根据异常解析其错误码
     * @param ex            - 异常对象
     * @param status        - HTTP状态码(可选)
     * @param messageCode   - 统一错误提示消息的i18n代码
     * @return
     */
    static ErrorCode resolve(Throwable ex, HttpStatus status, String messageCode) {
        boolean found = false;
        Throwable cause = ex;
        String code = GlobalErrorCode.ERR.getCode();
        String message = GlobalErrorCode.ERR.getMessage();
        if(status != null) {
            ErrorCode errorCode = GlobalErrorCode.getErrorCode(String.valueOf(status.value()), null);
            if(errorCode != null) {
                code = errorCode.getCode();
                message = errorCode.getMessage();
            }
        }
        while(cause != null){
            if (cause instanceof ApplicationException) { // 已知的异常信息
                code = ((ApplicationException) cause).getCode();
                message = cause.getMessage();
                if(StringUtils.containsChineseChar(message)){
                    found = true;
                    break;
                }
            }
            cause = cause.getCause();
        }
        if(!found){
            Throwable target = ExceptionUtils.getRootCause(ex);
            message = target.getMessage();
            if(!StringUtils.containsChineseChar(message)){
                message = ApplicationConstants.DEFAULT_MESSAGE_SOURCE_ACCESSOR.get().getMessage(messageCode, new Object[] {message}, message); // 未知的异常消息,需要转换成统一的,以增强用户体验
            }
        }
        return new CustomErrorCode(code, message);
    }

}
