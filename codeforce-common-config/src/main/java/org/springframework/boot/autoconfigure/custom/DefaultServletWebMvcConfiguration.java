package org.springframework.boot.autoconfigure.custom;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.penglecode.codeforce.common.web.springmvc.support.DelegateHttpMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Configuration;
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
 * @version 1.0
 * @created 2021/4/11 12:24
 */
@Configuration
@ConditionalOnWebApplication(type=Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
public class DefaultServletWebMvcConfiguration extends AbstractSpringConfiguration implements WebMvcConfigurer {

    @Override
    @SuppressWarnings({"unchecked"})
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for(int i = 0, len = converters.size(); i < len; i++) {
            HttpMessageConverter<?> converter = converters.get(i);
            if(converter instanceof MappingJackson2HttpMessageConverter) {
                ObjectMapper objectMapper = ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
                objectMapper.setSerializationInclusion(JsonInclude.Include.USE_DEFAULTS); //解决null值字段不输出的问题
            }
            if(!(converter instanceof DelegateHttpMessageConverter)) {
                converter = new DelegateHttpMessageConverter((HttpMessageConverter<Object>) converter);
                converters.set(i, converter); //replace
            }
        }
    }

}