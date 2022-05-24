package com.penglecode.codeforce.common.codegen.mybatis;

import com.penglecode.codeforce.common.codegen.config.DomainEntityConfig;
import com.penglecode.codeforce.common.codegen.config.MybatisCodegenConfigProperties;
import com.penglecode.codeforce.common.codegen.config.MybatisJavaMapperConfig;
import com.penglecode.codeforce.common.codegen.support.CodegenContext;
import com.penglecode.codeforce.common.codegen.support.CodegenParameterBuilder;
import com.penglecode.codeforce.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.codeforce.common.util.CollectionUtils;
import com.penglecode.codeforce.common.util.TemplateUtils;
import com.penglecode.codeforce.mybatistiny.mapper.BaseEntityMapper;

import java.util.*;

/**
 * 领域实体的Mybatis Java-Mapper代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 */
public class MybatisJavaMapperCodegenParameterBuilder extends CodegenParameterBuilder<MybatisCodegenConfigProperties, MybatisJavaMapperConfig, DomainEntityConfig, MybatisJavaMapperCodegenParameter> {

    public MybatisJavaMapperCodegenParameterBuilder(CodegenContext<MybatisCodegenConfigProperties, MybatisJavaMapperConfig, DomainEntityConfig> codegenContext) {
        super(codegenContext);
    }

    public MybatisJavaMapperCodegenParameterBuilder(MybatisCodegenConfigProperties codegenConfig, MybatisJavaMapperConfig targetConfig, DomainEntityConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected MybatisJavaMapperCodegenParameter setCustomCodegenParameter(MybatisJavaMapperCodegenParameter codegenParameter) {
        codegenParameter.setTargetComment(getDomainObjectConfig().getDomainEntityTitle() + "Mybatis-Mapper接口");
        codegenParameter.setTargetExtends(String.format("BaseMybatisMapper<%s>", getDomainObjectConfig().getDomainEntityName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(getDomainObjectConfig().getGeneratedTargetName(getDomainObjectConfig().getDomainEntityName(), true, false)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(BaseEntityMapper.class.getName()));

        Set<String> mapperAnnotationSet = getCodegenConfig().getMybatis().getJavaMapperConfig().getMapperAnnotations();
        List<String> mapperAnnotations = new ArrayList<>();
        if(!CollectionUtils.isEmpty(mapperAnnotationSet)) {
            for(String mapperAnnotation : mapperAnnotationSet) {
                String[] mapperAnnotationArray = mapperAnnotation.split(":");
                mapperAnnotations.add(parseMapperAnnotations(mapperAnnotationArray[1]));
                codegenParameter.addTargetImportType(new FullyQualifiedJavaType(mapperAnnotationArray[0]));
            }
        }
        codegenParameter.setTargetAnnotations(mapperAnnotations);
        return codegenParameter;
    }

    protected String parseMapperAnnotations(String mapperAnnotations) {
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("runtimeDataSource", getDomainObjectConfig().getRuntimeDataSource());
        return TemplateUtils.parseTemplate(mapperAnnotations, parameter);
    }

    @Override
    protected String getTargetTemplateName() {
        return "MybatisJavaMapper.ftl";
    }

}
