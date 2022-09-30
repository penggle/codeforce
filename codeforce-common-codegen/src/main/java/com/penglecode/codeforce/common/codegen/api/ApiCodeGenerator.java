package com.penglecode.codeforce.common.codegen.api;

import com.penglecode.codeforce.common.codegen.ModuleCodeGenerator;
import com.penglecode.codeforce.common.codegen.config.*;
import com.penglecode.codeforce.common.codegen.support.ApiMethod;
import com.penglecode.codeforce.common.codegen.support.ApiModelType;
import com.penglecode.codeforce.common.codegen.support.CodegenContext;
import com.penglecode.codeforce.common.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

/**
 * API接口代码生成器
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class ApiCodeGenerator extends ModuleCodeGenerator<ApiCodegenConfigProperties> {

    protected ApiCodeGenerator(String module) {
        super(module);
    }

    @Override
    protected String getCodeName() {
        return "Api代码";
    }

    @Override
    protected void executeGenerate() throws Exception {
        ApiCodegenConfigProperties codegenConfig = getCodegenConfig();
        Map<String,Set<ApiMethod>> clientApiProviders = codegenConfig.getApi().getClientConfig().getApiProviders();
        Map<String,Set<ApiMethod>> runtimeApiProviders = codegenConfig.getApi().getRuntimeConfig().getApiProviders();
        Map<String, DomainEntityConfig> domainEntityConfigs = codegenConfig.getDomain().getDomainEntities();
        if(!CollectionUtils.isEmpty(domainEntityConfigs)) {
            //1、处理独立的领域实体对象暴露API接口的情况
            for (Map.Entry<String,DomainEntityConfig> entry : domainEntityConfigs.entrySet()) {
                DomainEntityConfig domainEntityConfig = entry.getValue();
                if(clientApiProviders.containsKey(domainEntityConfig.getDomainEntityName())) { //1.1、生成指定领域实体的API接口(Client接口及实现)
                    generateApiModel(codegenConfig, codegenConfig.getApi().getClientConfig(), domainEntityConfig); //生成API接口的DTO
                    CodegenContext<ApiCodegenConfigProperties, ApiClientConfig, DomainEntityConfig> codegenContext1 = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getClientConfig(), domainEntityConfig);
                    generateTarget(codegenContext1, new ApiClientCodegenParameterBuilder<>(codegenContext1).buildCodegenParameter()); //生成API接口Client

                    CodegenContext<ApiCodegenConfigProperties, ApiRuntimeConfig, DomainEntityConfig> codegenContext2 = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getRuntimeConfig(), domainEntityConfig);
                    generateTarget(codegenContext2, new ApiClientRuntimeCodegenParameterBuilder<>(codegenContext2).buildCodegenParameter()); //生成API接口Client的实现
                } else if(runtimeApiProviders.containsKey(domainEntityConfig.getDomainEntityName())) { //1.2、生成指定领域实体的API接口(Controller)
                    generateApiModel(codegenConfig, codegenConfig.getApi().getRuntimeConfig(), domainEntityConfig); //生成API接口的DTO
                    CodegenContext<ApiCodegenConfigProperties, ApiRuntimeConfig, DomainEntityConfig> codegenContext = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getRuntimeConfig(), domainEntityConfig);
                    generateTarget(codegenContext, new ApiControllerRuntimeCodegenParameterBuilder<>(codegenContext).buildCodegenParameter()); //生成API接口Controller实现
                }
            }
        }
        Map<String, DomainAggregateConfig> domainAggregateConfigs = codegenConfig.getDomain().getDomainAggregates();
        if(!CollectionUtils.isEmpty(domainAggregateConfigs)) {
            //2、处理聚合根对象暴露API接口的情况
            for (Map.Entry<String, DomainAggregateConfig> entry : domainAggregateConfigs.entrySet()) {
                DomainAggregateConfig domainAggregateConfig = entry.getValue();
                if(clientApiProviders.containsKey(domainAggregateConfig.getDomainAggregateName())) { //2.1、生成指定聚合根的API接口(Client接口及实现)
                    generateApiModel(codegenConfig, codegenConfig.getApi().getClientConfig(), domainAggregateConfig); //生成DTO
                    CodegenContext<ApiCodegenConfigProperties, ApiClientConfig, DomainAggregateConfig> codegenContext1 = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getClientConfig(), domainAggregateConfig);
                    generateTarget(codegenContext1, new ApiClientCodegenParameterBuilder<>(codegenContext1).buildCodegenParameter()); //生成API接口Client

                    CodegenContext<ApiCodegenConfigProperties, ApiRuntimeConfig, DomainAggregateConfig> codegenContext2 = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getRuntimeConfig(), domainAggregateConfig);
                    generateTarget(codegenContext2, new ApiClientRuntimeCodegenParameterBuilder<>(codegenContext2).buildCodegenParameter()); //生成API接口Client的实现
                } else if(runtimeApiProviders.containsKey(domainAggregateConfig.getDomainAggregateName())) { //2.2、生成指定聚合根的API接口(Controller/Client实现)
                    generateApiModel(codegenConfig, codegenConfig.getApi().getRuntimeConfig(), domainAggregateConfig); //生成API接口的DTO
                    CodegenContext<ApiCodegenConfigProperties, ApiRuntimeConfig, DomainAggregateConfig> codegenContext = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getRuntimeConfig(), domainAggregateConfig);
                    generateTarget(codegenContext, new ApiControllerRuntimeCodegenParameterBuilder<>(codegenContext).buildCodegenParameter()); //生成API接口Controller实现
                }
            }
        }
    }

    /**
     * 生成指定领域对象的Request/Response对象
     * @param codegenConfig
     * @param apiProviderConfig
     * @param domainObjectConfig
     * @throws Exception
     */
    protected void generateApiModel(ApiCodegenConfigProperties codegenConfig, ApiProviderConfig apiProviderConfig, DomainObjectConfig domainObjectConfig) throws Exception {
        CodegenContext<ApiCodegenConfigProperties,ApiModelConfig,? extends DomainObjectConfig> codegenContext1 = new CodegenContext<>(codegenConfig, apiProviderConfig.getApiModelConfig().forModelType(ApiModelType.SAVE_REQUEST), domainObjectConfig);
        generateTarget(codegenContext1, new SaveReqApiModelCodegenParameterBuilder<>(codegenContext1).buildCodegenParameter());

        CodegenContext<ApiCodegenConfigProperties,ApiModelConfig,? extends DomainObjectConfig> codegenContext2 = new CodegenContext<>(codegenConfig, apiProviderConfig.getApiModelConfig().forModelType(ApiModelType.QUERY_REQUEST), domainObjectConfig);
        generateTarget(codegenContext2, new QueryReqApiModelCodegenParameterBuilder<>(codegenContext2).buildCodegenParameter());

        CodegenContext<ApiCodegenConfigProperties,ApiModelConfig,? extends DomainObjectConfig> codegenContext3 = new CodegenContext<>(codegenConfig, apiProviderConfig.getApiModelConfig().forModelType(ApiModelType.QUERY_RESPONSE), domainObjectConfig);
        generateTarget(codegenContext3, new QueryResApiModelCodegenParameterBuilder<>(codegenContext3).buildCodegenParameter());
    }

    /**
     * 生成指定聚合根的Request/Response DTO对象
     * @param codegenConfig
     * @param apiProviderConfig
     * @param domainAggregateConfig
     * @throws Exception
     */
    protected void generateApiModel(ApiCodegenConfigProperties codegenConfig, ApiProviderConfig apiProviderConfig, DomainAggregateConfig domainAggregateConfig) throws Exception {
        //生成Master的SAVE_DTO/QUERY_DTO
        generateDtoModel(codegenConfig, apiProviderConfig, codegenConfig.getDomain().getDomainEntities().get(domainAggregateConfig.getAggregateMasterEntity()));
        for(DomainAggregateSlaveConfig domainAggregateSlaveConfig : domainAggregateConfig.getAggregateSlaveEntities()) {
            //生成Slave的SAVE_DTO/QUERY_DTO
            generateDtoModel(codegenConfig, apiProviderConfig, codegenConfig.getDomain().getDomainEntities().get(domainAggregateSlaveConfig.getAggregateSlaveEntity()));
        }
        //生成聚合根的SAVE_REQUEST/QUERY_REQUEST/QUERY_RESPONSE对象
        generateApiModel(codegenConfig, apiProviderConfig, (DomainObjectConfig) domainAggregateConfig);
    }

    /**
     * 生成指定领域实体的DTO对象
     * @param codegenConfig
     * @param apiProviderConfig
     * @param domainEntityConfig
     * @throws Exception
     */
    protected void generateDtoModel(ApiCodegenConfigProperties codegenConfig, ApiProviderConfig apiProviderConfig, DomainEntityConfig domainEntityConfig) throws Exception {
        CodegenContext<ApiCodegenConfigProperties,ApiModelConfig,DomainEntityConfig> codegenContext1 = new CodegenContext<>(codegenConfig, apiProviderConfig.getApiModelConfig().forModelType(ApiModelType.SAVE_DTO), domainEntityConfig);
        generateTarget(codegenContext1, new SaveDtoApiModelCodegenParameterBuilder(codegenContext1).buildCodegenParameter());

        CodegenContext<ApiCodegenConfigProperties,ApiModelConfig,DomainEntityConfig> codegenContext2 = new CodegenContext<>(codegenConfig, apiProviderConfig.getApiModelConfig().forModelType(ApiModelType.QUERY_DTO), domainEntityConfig);
        generateTarget(codegenContext2, new QueryDtoApiModelCodegenParameterBuilder(codegenContext2).buildCodegenParameter());
    }

}
