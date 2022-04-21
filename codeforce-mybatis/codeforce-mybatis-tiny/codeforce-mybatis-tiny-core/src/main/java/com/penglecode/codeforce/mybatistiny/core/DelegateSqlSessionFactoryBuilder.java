package com.penglecode.codeforce.mybatistiny.core;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * 自定义的SqlSessionFactoryBuilder
 *
 * @author pengpeng
 * @version 1.0
 */
public class DelegateSqlSessionFactoryBuilder extends SqlSessionFactoryBuilder {

    @Override
    public SqlSessionFactory build(Configuration config) {
        return super.build(new DelegateConfiguration(config));
    }

}
