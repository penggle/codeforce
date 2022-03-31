package org.mybatis.spring.extensions;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.autoconfigure.dal.DefaultDalMybatisComponentsBuilder;

import javax.sql.DataSource;

/**
 * 自定义的DalMybatisComponentsBuilder
 * 启用动态Executor
 *
 * @see DynamicExecutor
 * @see ExecutorSynchronizationManager
 *
 * @author pengpeng
 * @version 1.0
 */
public class CustomDalMybatisComponentsBuilder extends DefaultDalMybatisComponentsBuilder {

    @Override
    public SqlSessionFactoryBean buildSqlSessionFactoryBean(DataSource dataSource, MybatisProperties properties) {
        SqlSessionFactoryBean sqlSessionFactoryBean = super.buildSqlSessionFactoryBean(dataSource, properties);
        sqlSessionFactoryBean.setSqlSessionFactoryBuilder(new CustomSqlSessionFactoryBuilder());
        return sqlSessionFactoryBean;
    }

}
