package com.penglecode.codeforce.mybatistiny.core;

import com.penglecode.codeforce.common.domain.EntityObject;
import com.penglecode.codeforce.mybatistiny.dsl.QueryCriteria;
import com.penglecode.codeforce.mybatistiny.exception.XMLMapperParseException;
import com.penglecode.codeforce.mybatistiny.interceptor.DomainObjectQueryInterceptor;
import com.penglecode.codeforce.mybatistiny.interceptor.PageLimitInterceptor;
import com.penglecode.codeforce.mybatistiny.mapper.BaseEntityMapper;
import com.penglecode.codeforce.mybatistiny.support.Utilities;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import com.penglecode.codeforce.mybatistiny.CustomConfiguration;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * XML-Mapper注册器
 * 通过实体Java-Mapper接口来注册自动生成的XML-Mapper，具体做了下面四件事
 *
 *  1、代理SqlSessionFactory中的configuration属性，如果可能的话
 *  2、向{@link Configuration}中注册{@link DomainObjectQueryInterceptor}和{@link PageLimitInterceptor}
 *  3、向{@link Configuration}中注册CommonMybatisMapper.xml
 *  4、向{@link Configuration}中注册自动生成的XxxMapper.xml
 *
 * @author pengpeng
 * @version 1.0
 * @param <E> 实体对象(EntityObject)，它对应着数据库中的一张表；而领域对象(DomainObject)则是包含了实体对象，不一定对应着数据库中的一张表
 */
public class EntityMapperRegistry<E extends EntityObject> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityMapperRegistry.class);

    private final SqlSessionFactory sqlSessionFactory;

    private final CustomConfiguration configuration;

    private final EntityMapperTemplateParameterFactory<E> entityMapperTemplateParameterFactory;

    public EntityMapperRegistry(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.configuration = delegateConfiguration();
        this.entityMapperTemplateParameterFactory = createTemplateParameterFactory();
        this.registerCommonTypeAlias();
        this.registerCommonPlugin();
        this.registerCommonMapper();
    }

    /**
     * 代理DefaultSqlSessionFactory中Configuration
     */
    protected CustomConfiguration delegateConfiguration() {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        Assert.state(sqlSessionFactory instanceof DefaultSqlSessionFactory, String.format("Can not delegate 'configuration' of SqlSessionFactory[%s], expected sqlSessionFactory is the type of %s!", sqlSessionFactory, DefaultSqlSessionFactory.class.getName()));
        CustomConfiguration delegateConfiguration;
        Configuration configuration = sqlSessionFactory.getConfiguration();
        if(!(configuration instanceof CustomConfiguration)) { //避免可能出现的重复delegate
            delegateConfiguration = new CustomConfiguration(configuration);
            Utilities.setFinalFieldValue(sqlSessionFactory, "configuration", delegateConfiguration);
            LOGGER.info(">>> Successfully delegate 'configuration' of DefaultSqlSessionFactory[{}]", sqlSessionFactory);
        } else {
            delegateConfiguration = (CustomConfiguration) configuration;
        }
        return delegateConfiguration;
    }

    /**
     * 注册CommonMybatisMapper.xml
     * 该公共XML-Mapper每个Configuration仅需注册一次即可
     */
    protected void registerCommonMapper() {
        String baseMapperLocation = BaseEntityMapper.class.getPackage().getName().replace(".", "/") + "/CommonMybatisMapper.xml";
        registerXmlMapper(new ClassPathResource(baseMapperLocation), baseMapperLocation);
    }

    /**
     * 注册公共的类型别名
     */
    protected void registerCommonTypeAlias() {
        configuration.getTypeAliasRegistry().registerAlias(QueryCriteria.class);
    }

    /**
     * 注册公共的插件
     */
    protected void registerCommonPlugin() {
        List<Interceptor> interceptors = configuration.getInterceptors();
        if(CollectionUtils.isEmpty(interceptors) || interceptors.stream().noneMatch(interceptor -> interceptor instanceof DomainObjectQueryInterceptor)) {
            configuration.addInterceptor(new DomainObjectQueryInterceptor());
            LOGGER.info(">>> Dynamically registered interceptor[{}] into {}", DomainObjectQueryInterceptor.class.getName(), configuration);
        }
        if(CollectionUtils.isEmpty(interceptors) || interceptors.stream().noneMatch(interceptor -> interceptor instanceof PageLimitInterceptor)) {
            configuration.addInterceptor(new PageLimitInterceptor());
            LOGGER.info(">>> Dynamically registered interceptor[{}] into {}", PageLimitInterceptor.class.getName(), configuration);
        }
    }

    protected EntityMapperTemplateParameterFactory<E> createTemplateParameterFactory() {
        Assert.hasText(configuration.getDatabaseId(), "Property 'databaseId' must be required in Mybatis Configuration!");
        return new EntityMapperTemplateParameterFactory<>(configuration.getDatabaseId());
    }

    /**
     * 注册实体对象的Mybatis-Mapper
     *
     * @param entityMapperClass
     */
    public void registerEntityMapper(Class<BaseEntityMapper<E>> entityMapperClass) {
        //创建实体元数据
        EntityMeta<E> entityMeta = createEntityMeta(entityMapperClass);
        //为动态生成的实体XxxMapper.xml创建所需的模板参数
        EntityMapperTemplateParameter<E> templateParameter = getXmlMapperTemplateParameterFactory().createTemplateParameter(entityMapperClass, entityMeta);

        freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        Class<?> resourceLoadClass = BaseEntityMapper.class;
        configuration.setClassForTemplateLoading(resourceLoadClass, "/" + resourceLoadClass.getPackage().getName().replace(".", "/"));
        try {
            Template xmlMapperTemplate = configuration.getTemplate("BaseEntityMapper.ftl");
            StringWriter xmlMapperWriter = new StringWriter();
            xmlMapperTemplate.process(templateParameter, xmlMapperWriter);
            String xmlMapperContent = xmlMapperWriter.toString();
            String xmlMapperLocation = entityMapperClass.getName().replace(".", "/") + ".xml";
            LOGGER.debug("<-----------------------------【{}】----------------------------->\n{}", xmlMapperLocation, xmlMapperContent);
            registerXmlMapper(new ByteArrayResource(xmlMapperContent.getBytes(StandardCharsets.UTF_8), xmlMapperLocation), xmlMapperLocation);
        } catch (IOException | TemplateException e) {
            throw new XMLMapperParseException("Failed to parse 'BaseEntityMapper.ftl'", e);
        }
    }

    /**
     * 创建实体元数据
     *
     * @param entityMapperClass
     * @return
     */
    protected EntityMeta<E> createEntityMeta(Class<BaseEntityMapper<E>> entityMapperClass) {
        Class<E> entityClass = Utilities.getSuperGenericType(entityMapperClass, BaseEntityMapper.class, 0);
        Assert.notNull(entityClass, String.format("Can not resolve parameterized entity class from entity mapper: %s", entityMapperClass));
        return EntityMetaFactory.getEntityMeta(entityClass);
    }

    /**
     * 注册动态生成的实体XxxMapper.xml
     *
     * @param xmlMapperResource
     * @param xmlMapperLocation     - XxxMapper.xml的classpath路径，例如：com/xxx/xxx/XxxMapper.xml
     * @return
     */
    protected void registerXmlMapper(Resource xmlMapperResource, String xmlMapperLocation) {
        try {
            //考虑到开发者可以自定义XxxMapper.xml，所以必须要重命名，否则与mybatis-spring默认加载的有可能重名，导致MappedStatement加载不到Configuration中去
            String xmlMapperResourceName = String.format("Auto-Generated XML-Mapper [%s]", xmlMapperLocation);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(xmlMapperResource.getInputStream(), configuration, xmlMapperResourceName, configuration.getSqlFragments());
            xmlMapperBuilder.parse();
            LOGGER.info(">>> Dynamically registered {} into {}", xmlMapperResourceName, configuration);
        } catch (IOException e) {
            throw new XMLMapperParseException("Failed to parse mapping resource: '" + xmlMapperResource + "'", e);
        } finally {
            ErrorContext.instance().reset();
        }
    }

    protected SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    protected CustomConfiguration getConfiguration() {
        return configuration;
    }

    protected EntityMapperTemplateParameterFactory<E> getXmlMapperTemplateParameterFactory() {
        return entityMapperTemplateParameterFactory;
    }

}
