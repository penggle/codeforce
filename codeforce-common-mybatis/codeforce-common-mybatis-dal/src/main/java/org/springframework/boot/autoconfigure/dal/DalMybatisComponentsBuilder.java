package org.springframework.boot.autoconfigure.dal;

import com.penglecode.codeforce.common.consts.ApplicationConstants;
import com.penglecode.codeforce.common.util.SpringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * {@link SqlSessionFactoryBean}和{@link ClassPathMapperScanner}的Builder接口
 *
 * @author pengpeng
 * @version 1.0
 */
public abstract class DalMybatisComponentsBuilder implements EnvironmentAware, ResourceLoaderAware, BeanFactoryAware, InitializingBean {

    public static final String DEFAULT_CONFIG_LOCATION = "classpath:config/mybatis/default-mybatis-config.xml";

    private String[] defaultBasePackages;

    private String[] defaultMapperLocations;

    protected Environment environment;

    protected BeanFactory beanFactory;

    protected ResourceLoader resourceLoader;

    /**
     * 创建SqlSessionFactoryBean
     *
     * @param dataSource
     * @param mybatisProperties
     * @return
     */
    public abstract SqlSessionFactoryBean buildSqlSessionFactoryBean(DataSource dataSource, MybatisProperties mybatisProperties);

    /**
     * 创建ClassPathMapperScanner
     * 注意：此处不需要调用ClassPathMapperScanner#registerFilters()和scan()两个方法，build逻辑只需要设置相关属性即可
     *
     * @param database
     * @param registry
     * @return
     */
    public abstract ClassPathMapperScanner buildClassPathMapperScanner(String database, BeanDefinitionRegistry registry);

    /**
     * 解析mybatis-config.xml配置文件
     * @param configLocation
     * @return
     */
    protected Resource resolveConfigLocation(String configLocation) {
        configLocation = StringUtils.defaultIfBlank(configLocation, DEFAULT_CONFIG_LOCATION);
        Resource resource = getDefaultResourcePatternResolver().getResource(configLocation);
        return resource.exists() ? resource : null;
    }

    /**
     * 解析XxxMapper.xml文件
     * @param mapperLocations
     * @return
     */
    protected Resource[] resolveMapperLocations(String[] mapperLocations) {
        mapperLocations = ArrayUtils.isNotEmpty(mapperLocations) ? mapperLocations : this.defaultMapperLocations;
        return Stream.of(mapperLocations).flatMap(mapperLocation -> Stream.of(getResources(mapperLocation))).toArray(Resource[]::new);
    }

    protected Resource[] getResources(String mapperLocation) {
        try {
            return getDefaultResourcePatternResolver().getResources(mapperLocation);
        } catch (IOException e) {
            return new Resource[0];
        }
    }

    protected ResourcePatternResolver getDefaultResourcePatternResolver() {
        return ApplicationConstants.DEFAULT_RESOURCE_PATTERN_RESOLVER.get();
    }

    @Override
    public final void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public final void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public final void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.defaultBasePackages = SpringUtils.getDefaultBasePackages();
        int length = defaultBasePackages.length;
        this.defaultMapperLocations = new String[length];
        for(int i = 0; i < length; i++) {
            this.defaultMapperLocations[i] = String.format("classpath*:%s/**/*Mapper.xml", defaultBasePackages[i].replace(".", "/"));
        }
    }

    protected String[] getDefaultBasePackages() {
        return defaultBasePackages;
    }

    protected String[] getDefaultMapperLocations() {
        return defaultMapperLocations;
    }

    protected Environment getEnvironment() {
        return environment;
    }

    protected BeanFactory getBeanFactory() {
        return beanFactory;
    }

    protected ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

}
