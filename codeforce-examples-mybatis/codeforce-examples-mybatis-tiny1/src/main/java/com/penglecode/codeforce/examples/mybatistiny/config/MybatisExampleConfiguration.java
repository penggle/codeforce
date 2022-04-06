package com.penglecode.codeforce.examples.mybatistiny.config;

import com.penglecode.codeforce.mybatistiny.config.MybatisTinyConfiguration;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.boot.autoconfigure.custom.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Properties;

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

    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.put("MySQL", "mysql");
        properties.put("Oracle", "oracle");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }

}
