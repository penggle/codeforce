package com.penglecode.codeforce.mybatistiny.config;

import com.penglecode.codeforce.common.domain.EntityObject;
import com.penglecode.codeforce.mybatistiny.spring.MapperBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatistiny自动配置
 *
 * @author pengpeng
 * @version 1.0
 */
@Configuration
public class MybatistinyConfiguration {

    @Bean
    public <E extends EntityObject> MapperBeanPostProcessor<E> mapperBeanPostProcessor() {
        return new MapperBeanPostProcessor<>();
    }

}
