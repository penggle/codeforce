package com.penglecode.codeforce.common.support;

import org.springframework.context.support.MessageSourceAccessor;

import java.util.Locale;
import java.util.function.Function;

/**
 * 国际化消息Supplier
 *
 * @author pengpeng
 * @version 1.0.0
 */
public interface MessageSupplier extends Function<MessageSourceAccessor,String> {

    static MessageSupplier ofRequiredParameter(String parameterName) {
        return messageSourceAccessor -> messageSourceAccessor.getMessage("message.parameter.required", parameterName);
    }

    static MessageSupplier of(String code, Object... args) {
        return messageSourceAccessor -> messageSourceAccessor.getMessage(code, args);
    }

    static MessageSupplier of(Locale locale, String code, Object... args) {
        return messageSourceAccessor -> messageSourceAccessor.getMessage(code, args, locale);
    }

}