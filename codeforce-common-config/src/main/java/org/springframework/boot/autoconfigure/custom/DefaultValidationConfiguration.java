package org.springframework.boot.autoconfigure.custom;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * 默认的javax.validation配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/6/17 22:47
 */
@Configuration
@ConditionalOnClass(HibernateValidator.class)
public class DefaultValidationConfiguration extends AbstractSpringConfiguration {

    @Primary
    @Bean(name="defaultValidator")
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LocalValidatorFactoryBean defaultValidator() {
        LocalValidatorFactoryBean defaultValidator = new LocalValidatorFactoryBean();
        defaultValidator.setProviderClass(HibernateValidator.class);
        defaultValidator.getValidationPropertyMap().put("hibernate.validator.fail_fast", Boolean.TRUE.toString());
        MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory();
        defaultValidator.setMessageInterpolator(interpolatorFactory.getObject());
        return defaultValidator;
    }

}
