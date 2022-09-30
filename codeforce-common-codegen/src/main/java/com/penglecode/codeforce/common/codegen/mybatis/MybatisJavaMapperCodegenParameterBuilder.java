package com.penglecode.codeforce.common.codegen.mybatis;

import com.penglecode.codeforce.common.codegen.config.DomainEntityConfig;
import com.penglecode.codeforce.common.codegen.config.MybatisCodegenConfigProperties;
import com.penglecode.codeforce.common.codegen.config.MybatisJavaMapperConfig;
import com.penglecode.codeforce.common.codegen.support.CodegenAnnotationMeta;
import com.penglecode.codeforce.common.codegen.support.CodegenContext;
import com.penglecode.codeforce.common.codegen.support.CodegenParameterBuilder;
import com.penglecode.codeforce.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.codeforce.common.util.TemplateUtils;
import com.penglecode.codeforce.mybatistiny.mapper.BaseEntityMapper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 领域实体的Mybatis Java-Mapper代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0.0
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
        codegenParameter.setTargetExtends(String.format("%s<%s>", BaseEntityMapper.class.getSimpleName(), getDomainObjectConfig().getDomainEntityName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(getDomainObjectConfig().getGeneratedTargetName(getDomainObjectConfig().getDomainEntityName(), true, false)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(BaseEntityMapper.class.getName()));
        List<String> mapperAnnotations = new ArrayList<>();
        Set<CodegenAnnotationMeta> mapperAnnotationMetas = buildMapperAnnotations();
        for(CodegenAnnotationMeta mapperAnnotationMeta : mapperAnnotationMetas) {
            mapperAnnotations.add(mapperAnnotationMeta.getExpression());
            codegenParameter.addTargetImportTypes(mapperAnnotationMeta.getImportTypes());
        }
        codegenParameter.setTargetAnnotations(mapperAnnotations);
        return codegenParameter;
    }

    protected Set<CodegenAnnotationMeta> buildMapperAnnotations() {
        return getCodegenConfig().getMybatis().getJavaMapperConfig().getMapperAnnotations().stream()
                .map(mapperAnnotation -> {
                    String[] mapperAnnotations = mapperAnnotation.split(":");
                    return new CodegenAnnotationMeta(parseMapperAnnotations(mapperAnnotations[1]), Collections.singleton(new FullyQualifiedJavaType(mapperAnnotations[0])));
                }).collect(Collectors.toSet());
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
