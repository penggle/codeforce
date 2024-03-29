package com.penglecode.codeforce.common.web.springmvc.support;

import com.penglecode.codeforce.common.model.BaseResponse;
import com.penglecode.codeforce.common.support.ErrorCode;
import com.penglecode.codeforce.common.web.MapResponse;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;

/**
 * 所有HttpMessageConverter的代理Wrapper
 * 用于统一处理响应结果：
 *      1、保证HTTP的状态码与Result.code一致
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class DelegateHttpMessageConverter implements HttpMessageConverter<Object> {

    private final HttpMessageConverter<Object> delegate;

    public DelegateHttpMessageConverter(HttpMessageConverter<Object> delegate) {
        super();
        Assert.notNull(delegate, "Parameter 'delegate' can not be null!");
        this.delegate = delegate;
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return delegate.canRead(clazz, mediaType);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return delegate.canWrite(clazz, mediaType);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return delegate.getSupportedMediaTypes();
    }

    @Override
    public Object read(Class<?> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return delegate.read(clazz, inputMessage);
    }

    @Override
    public void write(Object t, MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        handleOutputMessage(t, outputMessage);
        delegate.write(t, contentType, outputMessage);
    }

    @SuppressWarnings("unchecked")
    protected void handleOutputMessage(Object t, HttpOutputMessage outputMessage) {
        if(outputMessage instanceof ServerHttpResponse) {
            ServerHttpResponse serverHttpResponse = (ServerHttpResponse) outputMessage;
            HttpStatus status;
            if(t instanceof BaseResponse) {
                BaseResponse<Object> result = (BaseResponse<Object>) t;
                status = ErrorCode.resolve(result.getCode());
                if(status != null) { //1、保证HTTP的状态码与Result.code一致
                    serverHttpResponse.setStatusCode(status);
                }
            } else if(t instanceof MapResponse) {
                MapResponse result = (MapResponse) t;
                status = ErrorCode.resolve(result.getCode());
                if(status != null) { //1、保证HTTP的状态码与Result.code一致
                    serverHttpResponse.setStatusCode(status);
                }
            }

        }
    }

}
