package org.springframework.boot.autoconfigure.dal;

import com.penglecode.codeforce.common.consts.ApplicationConstants;
import com.penglecode.codeforce.common.initializer.AbstractSpringAppContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

/**
 * 禁用SpringBoot默认的DAL配置，具体来说就是：
 *      spring.autoconfigure.exclude[0]:org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
 *      spring.autoconfigure.exclude[1]:org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration
 * @author pengpeng
 * @version 1.0
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
public class DisableDefaultAutoConfigurationInitializer extends AbstractSpringAppContextInitializer {

    public static final String PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE = "spring.autoconfigure.exclude";

    @Override
    public void doInitialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MapPropertySource appDefaultProperties = (MapPropertySource) environment.getPropertySources().get(ApplicationConstants.APP_DEFAULT_PROPERTY_SOURCE_NAME);
        if(appDefaultProperties != null) { //框架默认基于多数据源，此时应该强制禁用SpringBoot默认的数据源及Mybatis配置
            appDefaultProperties.getSource().put("spring.autoconfigure.exclude[0]", "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration");
            appDefaultProperties.getSource().put("spring.autoconfigure.exclude[1]", "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration");
        }
    }

}
