package org.springframework.boot.autoconfigure.custom;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * 默认的javax.validation配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/6/17 22:47
 */
@Configuration
@ConditionalOnClass({HibernateValidator.class, LocalValidatorFactoryBean.class})
public class DefaultValidationConfiguration extends AbstractSpringConfiguration implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof LocalValidatorFactoryBean) {
            ((LocalValidatorFactoryBean) bean).getValidationPropertyMap().put("hibernate.validator.fail_fast", Boolean.TRUE.toString());
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

}
