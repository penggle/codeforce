package com.penglecode.codeforce.common.config.defaults;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.penglecode.codeforce.common.web.servlet.support.HttpApiTraceFilter;
import com.penglecode.codeforce.common.web.springmvc.support.DelegateHttpMessageConverter;
import com.penglecode.codeforce.common.config.AbstractSpringConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Servlet;
import java.util.List;

/**
 * 默认的SpringMVC的定制化配置
 *
 * @author pengpeng
 * @version 1.0.0
 */
@Configuration
@ConditionalOnWebApplication(type=Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
@ConditionalOnProperty(name=DefaultServletWebMvcConfiguration.CONFIGURATION_ENABLED, havingValue="true", matchIfMissing=true)
public class DefaultServletWebMvcConfiguration extends AbstractSpringConfiguration implements WebMvcConfigurer {

    public static final String CONFIGURATION_ENABLED = "spring.mvc.customized.enabled";

    @Override
    @SuppressWarnings({"unchecked"})
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for(int i = 0, len = converters.size(); i < len; i++) {
            HttpMessageConverter<?> converter = converters.get(i);
            if(converter instanceof MappingJackson2HttpMessageConverter) {
                ObjectMapper objectMapper = ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
                objectMapper.setSerializationInclusion(JsonInclude.Include.USE_DEFAULTS); //解决null值字段不输出的问题
                objectMapper.registerModule(new Jdk8Module());
                objectMapper.registerModule(new JavaTimeModule());
            }
            if(!(converter instanceof DelegateHttpMessageConverter)) {
                converter = new DelegateHttpMessageConverter((HttpMessageConverter<Object>) converter);
                converters.set(i, converter); //replace
            }
        }
    }

    @Bean
    @ConditionalOnMissingBean(name="httpApiTraceFilter")
    public FilterRegistrationBean<HttpApiTraceFilter> httpApiTraceFilter() {
        FilterRegistrationBean<HttpApiTraceFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setName("httpApiTraceFilter");
        registrationBean.setFilter(new HttpApiTraceFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

}
