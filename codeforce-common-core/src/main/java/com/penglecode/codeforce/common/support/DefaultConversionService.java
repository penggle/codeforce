package com.penglecode.codeforce.common.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 默认的ConversionService
 *
 * @author pengpeng
 * @version 1.0.0
 */
@SuppressWarnings({"rawtypes"})
public class DefaultConversionService extends ApplicationConversionService implements BeanFactoryAware {

    /**
     * 默认的Java8日期时间格式化注册器
     */
    public static final DateTimeFormatterRegistrar DEFAULT_DATETIME_FORMATTER_REGISTRAR = new DateTimeFormatterRegistrar();

    public static final DateFormatterRegistrar DEFAULT_DATE_FORMATTER_REGISTRAR = new DateFormatterRegistrar();

    static {
        DEFAULT_DATETIME_FORMATTER_REGISTRAR.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DEFAULT_DATETIME_FORMATTER_REGISTRAR.setTimeFormatter(DateTimeFormatter.ofPattern("HH:mm:ss"));
        DEFAULT_DATETIME_FORMATTER_REGISTRAR.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        DEFAULT_DATE_FORMATTER_REGISTRAR.setFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
    }

    private ConfigurableListableBeanFactory beanFactory;

    public DefaultConversionService() {
        super();
        DEFAULT_DATETIME_FORMATTER_REGISTRAR.registerFormatters(this);
        DEFAULT_DATE_FORMATTER_REGISTRAR.registerFormatters(this);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = ((ConfigurableListableBeanFactory) beanFactory);
        this.applyCustomConverters();
    }

    protected Map<String,Converter> collectCustomConverters() {
        return beanFactory.getBeansOfType(Converter.class);
    }

    protected Map<String,ConverterFactory> collectCustomConverterFactories() {
        return beanFactory.getBeansOfType(ConverterFactory.class);
    }

    protected Map<String,GenericConverter> collectCustomGenericConverters() {
        return beanFactory.getBeansOfType(GenericConverter.class);
    }

    protected void applyCustomConverters() {
        collectCustomConverters().values().forEach(this::addConverter);
        collectCustomConverterFactories().values().forEach(this::addConverterFactory);
        collectCustomGenericConverters().values().forEach(this::addConverter);
    }

    protected ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

}
