package org.springframework.boot.autoconfigure.custom;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.extensions.CustomDalMybatisComponentsBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.dal.DalMybatisComponentsBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 默认的DAL层组件配置
 *
 * @author pengpeng
 * @version 1.0
 */
@Configuration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class, MybatisProperties.class})
public class DefaultDalComponentConfiguration extends AbstractSpringConfiguration {

    @Bean
    public DalMybatisComponentsBuilder dalMybatisComponentsBuilder() {
        return new CustomDalMybatisComponentsBuilder(); //启动动态Executor
    }

}
