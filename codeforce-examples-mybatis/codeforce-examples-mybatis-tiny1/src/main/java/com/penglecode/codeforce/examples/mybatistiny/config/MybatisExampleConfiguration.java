package com.penglecode.codeforce.examples.mybatistiny.config;

import com.penglecode.codeforce.mybatistiny.config.MybatisTinyConfiguration;
import org.springframework.boot.autoconfigure.custom.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author pengpeng
 * @version 1.0
 */
@Configuration
@Import({DefaultSpringAppConfiguration.class,
         DefaultServletWebMvcConfiguration.class,
         DefaultServletWebErrorConfiguration.class,
         DefaultValidationConfiguration.class,
         DefaultSwaggerConfiguration.class,
         MybatisTinyConfiguration.class})
public class MybatisExampleConfiguration {

}
