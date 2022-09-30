package com.penglecode.codeforce.common.initializer;

import com.penglecode.codeforce.common.consts.ApplicationConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import javax.annotation.Priority;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认的SpringBoot应用上下文环境初始化
 *
 * @author pengpeng
 * @version 1.0.0
 */
@Priority(0)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DefaultSpringAppEnvInitializer implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        initDefaultAppProperties(environment); //初始化应用默认的配置
    }

    protected void initDefaultAppProperties(ConfigurableEnvironment environment) {
        Map<String,Object> appDefaultProperties = new HashMap<>();
        //添加appDefaultProperties，放置在最后(因此它的优先级最高)，这样可以在ApplicationContextInitializer中获取之，可以做动态的配置覆盖
        environment.getPropertySources().addLast(new MapPropertySource(ApplicationConstants.APP_DEFAULT_PROPERTY_SOURCE_NAME, appDefaultProperties));
    }

}
