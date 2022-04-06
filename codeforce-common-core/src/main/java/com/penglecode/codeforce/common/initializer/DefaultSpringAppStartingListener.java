package com.penglecode.codeforce.common.initializer;

import com.penglecode.codeforce.common.util.ReflectionUtils;
import com.penglecode.codeforce.common.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

import java.util.Objects;

/**
 * 默认的SpringBoot应用启动监听器
 * (在整个SpringBoot应用刚启动时做的操作)
 *
 * @author pengpeng
 * @version 1.0
 */
public class DefaultSpringAppStartingListener implements ApplicationListener<ApplicationStartingEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpringAppStartingListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        LOGGER.info(">>> SpringBoot应用程序启动..., springApplication = {}", event.getSpringApplication());
        ReflectionUtils.invokeMethod(Objects.requireNonNull(ReflectionUtils.findMethod(SpringUtils.class, "setSpringApplication")), null, event.getSpringApplication());
    }

}
