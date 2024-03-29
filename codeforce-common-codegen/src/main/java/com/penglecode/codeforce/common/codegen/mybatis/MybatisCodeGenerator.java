package com.penglecode.codeforce.common.codegen.mybatis;

import com.penglecode.codeforce.common.codegen.ModuleCodeGenerator;
import com.penglecode.codeforce.common.codegen.config.DomainEntityConfig;
import com.penglecode.codeforce.common.codegen.config.MybatisCodegenConfigProperties;
import com.penglecode.codeforce.common.codegen.config.MybatisJavaMapperConfig;
import com.penglecode.codeforce.common.codegen.support.CodegenContext;
import com.penglecode.codeforce.common.util.CollectionUtils;

import java.util.Map;

/**
 * Mybatis代码生成器
 * 专门用于生成指定bizModule模块下的Mybatis代码(如XxxMpper.java、XxxMapper.xml)
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class MybatisCodeGenerator extends ModuleCodeGenerator<MybatisCodegenConfigProperties> {

    public MybatisCodeGenerator(String module) {
        super(module);
    }

    @Override
    protected String getCodeName() {
        return "Mybatis代码";
    }

    @Override
    protected void executeGenerate() throws Exception {
        MybatisCodegenConfigProperties codegenConfig = getCodegenConfig();
        Map<String,DomainEntityConfig> domainEntityConfigs = codegenConfig.getDomain().getDomainEntities();
        if(!CollectionUtils.isEmpty(domainEntityConfigs)) {
            for(Map.Entry<String,DomainEntityConfig> entry : domainEntityConfigs.entrySet()) {
                DomainEntityConfig domainEntityConfig = entry.getValue();
                //生成XxxMapper.java接口
                CodegenContext<MybatisCodegenConfigProperties,MybatisJavaMapperConfig,DomainEntityConfig> codegenContext = new CodegenContext<>(codegenConfig, codegenConfig.getMybatis().getJavaMapperConfig(), domainEntityConfig);
                generateTarget(codegenContext, new MybatisJavaMapperCodegenParameterBuilder(codegenContext).buildCodegenParameter());
            }
        }
    }

}
