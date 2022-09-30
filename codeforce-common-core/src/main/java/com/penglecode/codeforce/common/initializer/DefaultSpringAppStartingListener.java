package com.penglecode.codeforce.common.initializer;

import com.penglecode.codeforce.common.util.ReflectionUtils;
import com.penglecode.codeforce.common.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 默认的SpringBoot应用启动监听器
 * (在整个SpringBoot应用刚启动时做的操作)
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class DefaultSpringAppStartingListener implements ApplicationListener<ApplicationStartingEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpringAppStartingListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        SpringApplication springApplication = event.getSpringApplication();
        LOGGER.info(">>> SpringBoot应用程序启动! springApplication = {}", springApplication);
        Method method = Objects.requireNonNull(ReflectionUtils.findMethod(SpringUtils.class, "setSpringApplication", SpringApplication.class));
        method.setAccessible(true);
        ReflectionUtils.invokeMethod(method, null, springApplication);
    }

}
