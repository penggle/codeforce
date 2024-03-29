package com.penglecode.codeforce.common.codegen.service;

import com.penglecode.codeforce.common.codegen.config.*;
import com.penglecode.codeforce.common.codegen.support.CodegenContext;
import com.penglecode.codeforce.common.codegen.support.DomainMasterSlaveRelation;
import com.penglecode.codeforce.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.codeforce.common.codegen.util.CodegenUtils;
import com.penglecode.codeforce.common.support.BeanValidator;
import com.penglecode.codeforce.common.support.MapLambdaBuilder;
import com.penglecode.codeforce.common.support.MessageSupplier;
import com.penglecode.codeforce.common.support.ValidationAssert;
import com.penglecode.codeforce.common.util.CollectionUtils;
import com.penglecode.codeforce.common.util.DateTimeUtils;
import com.penglecode.codeforce.common.util.ObjectUtils;
import com.penglecode.codeforce.common.util.StringUtils;
import com.penglecode.codeforce.mybatistiny.dsl.LambdaQueryCriteria;
import com.penglecode.codeforce.mybatistiny.dsl.QueryCriteria;
import com.penglecode.codeforce.mybatistiny.support.EntityMapperHelper;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 领域实体的领域服务实现代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class DomainServiceImplementCodegenParameterBuilder extends AbstractDomainServiceCodegenParameterBuilder<ServiceImplementConfig, DomainServiceImplementCodegenParameter> {

    public DomainServiceImplementCodegenParameterBuilder(CodegenContext<ServiceCodegenConfigProperties, ServiceImplementConfig, DomainEntityConfig> codegenContext) {
        super(codegenContext);
    }

    public DomainServiceImplementCodegenParameterBuilder(ServiceCodegenConfigProperties codegenConfig, ServiceImplementConfig targetConfig, DomainEntityConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected DomainServiceImplementCodegenParameter setCustomCodegenParameter(DomainServiceImplementCodegenParameter codegenParameter) {
        DomainEntityConfig domainEntityConfig = getDomainObjectConfig();
        codegenParameter.setTargetComment(domainEntityConfig.getDomainObjectTitle() + "领域服务实现");
        ServiceInterfaceConfig serviceInterfaceConfig = getCodegenConfig().getService().getDomain().getInterfaceConfig();
        String serviceInterfaceName = serviceInterfaceConfig.getGeneratedTargetName(domainEntityConfig.getDomainEntityName(), false, false);
        codegenParameter.setTargetAnnotations(Collections.singletonList(String.format("@Service(\"%s\")", StringUtils.lowerCaseFirstChar(serviceInterfaceName))));
        codegenParameter.setTargetImplements(Collections.singletonList(serviceInterfaceName));
        MybatisJavaMapperConfig mybatisJavaMapperConfig = getCodegenConfig().getMybatis().getJavaMapperConfig();
        String mapperInterfaceName = mybatisJavaMapperConfig.getGeneratedTargetName(domainEntityConfig.getDomainEntityName(), false, false);
        codegenParameter.setMapperInterfaceName(mapperInterfaceName);
        codegenParameter.setMapperBeanName(domainEntityConfig.getRuntimeDataSource() + mapperInterfaceName);
        codegenParameter.setMapperInstanceName(StringUtils.lowerCaseFirstChar(mapperInterfaceName));
        codegenParameter.setTransactionManagerName(domainEntityConfig.getRuntimeDataSource() + "TransactionManager");
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(mybatisJavaMapperConfig.getGeneratedTargetName(domainEntityConfig.getDomainEntityName(), true, false)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(serviceInterfaceConfig.getGeneratedTargetName(domainEntityConfig.getDomainEntityName(), true, false)));

        return super.setCustomCodegenParameter(codegenParameter);
    }

    /**
     * 附带上领域服务代码import
     * @param codegenParameter
     */
    protected void attachDomainServiceImplementImports(DomainServiceImplementCodegenParameter codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Service.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Resource.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Transactional.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(BeanValidator.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(MessageSupplier.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(ValidationAssert.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(EntityMapperHelper.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Map.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(MapLambdaBuilder.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(ObjectUtils.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(CollectionUtils.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Collections.class.getName()));
    }
    
    @Override
    protected DomainServiceMethodParameter createDomainObject(DomainServiceImplementCodegenParameter codegenParameter) {
        DomainServiceMethodParameter serviceMethod = super.createDomainObject(codegenParameter);
        //开始生成方法实现(方法体代码)
        List<String> methodBodyLines = new ArrayList<>();
        DomainEntityConfig domainEntityConfig = getDomainObjectConfig();
        String domainObjectVariableName = codegenParameter.getDomainObjectParameter().getLowerDomainObjectName();
        methodBodyLines.add(String.format("ValidationAssert.notNull(%s, MessageSupplier.ofRequiredParameter(\"%s\"));", domainObjectVariableName, domainObjectVariableName));
        DomainEntityColumnConfig defaultCreateTimeColumn = getCodegenConfig().getDefaultCreateTimeColumn(domainEntityConfig);
        String upperDefaultCreateTimeFieldName = null;
        if(defaultCreateTimeColumn != null && defaultCreateTimeColumn.isColumnOnInsert()) {
            upperDefaultCreateTimeFieldName = StringUtils.upperCaseFirstChar(defaultCreateTimeColumn.getIntrospectedColumn().getJavaFieldName());
            setAccessTimeDefaultValue(codegenParameter, methodBodyLines, domainObjectVariableName, defaultCreateTimeColumn, upperDefaultCreateTimeFieldName);
        }
        DomainEntityColumnConfig defaultUpdateTimeColumn = getCodegenConfig().getDefaultUpdateTimeColumn(domainEntityConfig);
        if(defaultUpdateTimeColumn != null && defaultUpdateTimeColumn.isColumnOnInsert()) {
            String upperDefaultUpdateTimeFieldName = StringUtils.upperCaseFirstChar(defaultUpdateTimeColumn.getIntrospectedColumn().getJavaFieldName());
            if(upperDefaultCreateTimeFieldName != null && defaultUpdateTimeColumn.getJavaType().equals(defaultCreateTimeColumn.getJavaType())) {
                methodBodyLines.add(String.format("%s.set%s(%s.get%s());", domainObjectVariableName, upperDefaultUpdateTimeFieldName, domainObjectVariableName, upperDefaultCreateTimeFieldName));
            } else {
                setAccessTimeDefaultValue(codegenParameter, methodBodyLines, domainObjectVariableName, defaultUpdateTimeColumn, upperDefaultUpdateTimeFieldName);
            }
        }
        String validateFields = domainEntityConfig.getValidateFields("create");
        if(StringUtils.isNotBlank(validateFields)) {
            methodBodyLines.add(String.format("BeanValidator.validateBean(%s);", domainObjectVariableName + validateFields));
        }
        serviceMethod.setMethodBodyLines(methodBodyLines);
        //后续代码写在freemarker模板中了
        return serviceMethod;
    }

    /**
     * 设置操作时间的默认值
     */
    private void setAccessTimeDefaultValue(DomainServiceImplementCodegenParameter codegenParameter, List<String> methodBodyLines, String domainObjectVariableName, DomainEntityColumnConfig accessTimeColumn, String upperAccessTimeFieldName) {
        if(LocalDateTime.class.equals(accessTimeColumn.getJavaType())) {
            methodBodyLines.add(String.format("%s.set%s(ObjectUtils.defaultIfNull(%s.get%s(), LocalDateTime::now));", domainObjectVariableName, upperAccessTimeFieldName, domainObjectVariableName, upperAccessTimeFieldName));
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(LocalDateTime.class.getName()));
        } else {
            methodBodyLines.add(String.format("%s.set%s(StringUtils.defaultIfBlank(%s.get%s(), DateTimeUtils::formatNow));", domainObjectVariableName, upperAccessTimeFieldName, domainObjectVariableName, upperAccessTimeFieldName));
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(StringUtils.class.getName()));
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(DateTimeUtils.class.getName()));
        }
    }

    @Override
    protected DomainServiceMethodParameter createDomainObjects(DomainServiceImplementCodegenParameter codegenParameter) {
        DomainServiceMethodParameter serviceMethod = super.createDomainObjects(codegenParameter);
        List<String> methodBodyLines = new ArrayList<>();
        String domainObjectsVariableName = codegenParameter.getDomainObjectParameter().getLowerDomainObjectsName();
        methodBodyLines.add(String.format("ValidationAssert.notEmpty(%s, MessageSupplier.ofRequiredParameter(\"%s\"));", domainObjectsVariableName, domainObjectsVariableName));
        serviceMethod.setMethodBodyLines(methodBodyLines);
        //后续代码写在freemarker模板中了
        return serviceMethod;
    }

    @Override
    protected DomainServiceMethodParameter modifyDomainObjectById(DomainServiceImplementCodegenParameter codegenParameter) {
        DomainServiceMethodParameter serviceMethod = super.modifyDomainObjectById(codegenParameter);
        List<String> methodBodyLines = new ArrayList<>();
        DomainEntityConfig domainEntityConfig = getDomainObjectConfig();
        String domainObjectVariableName = codegenParameter.getDomainObjectParameter().getLowerDomainObjectName();
        methodBodyLines.add(String.format("ValidationAssert.notNull(%s, MessageSupplier.ofRequiredParameter(\"%s\"));", domainObjectVariableName, domainObjectVariableName));
        DomainEntityColumnConfig defaultUpdateTimeColumn = getCodegenConfig().getDefaultUpdateTimeColumn(domainEntityConfig);
        if(defaultUpdateTimeColumn != null && defaultUpdateTimeColumn.isColumnOnUpdate()) {
            String upperDefaultUpdateTimeFieldName = StringUtils.upperCaseFirstChar(defaultUpdateTimeColumn.getIntrospectedColumn().getJavaFieldName());
            setAccessTimeDefaultValue(codegenParameter, methodBodyLines, domainObjectVariableName, defaultUpdateTimeColumn, upperDefaultUpdateTimeFieldName);
        }
        String validateFields = domainEntityConfig.getValidateFields("modify");
        if(StringUtils.isNotBlank(validateFields)) {
            methodBodyLines.add(String.format("BeanValidator.validateBean(%s);", domainObjectVariableName + validateFields));
        }
        StringBuilder updateFields = new StringBuilder(String.format("Map<String,Object> updateColumns = MapLambdaBuilder.of(%s)", domainObjectVariableName)).append("\n");
        Map<String,DomainEntityColumnConfig> domainEntityColumns = domainEntityConfig.getDomainEntityColumns();
        for(Map.Entry<String,DomainEntityColumnConfig> entry : domainEntityColumns.entrySet()) {
            DomainEntityColumnConfig domainEntityColumn = entry.getValue();
            if(domainEntityColumn.isColumnOnUpdate()) {
                String fieldName = domainEntityColumn.getIntrospectedColumn().getJavaFieldName();
                String fieldType = domainEntityColumn.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters();
                updateFields.append("                .with(").append(domainEntityConfig.getDomainEntityName()).append("::").append(CodegenUtils.getGetterMethodName(fieldName, fieldType)).append(")").append("\n");
            }
        }
        updateFields.append("                .build();");
        methodBodyLines.add(updateFields.toString());
        serviceMethod.setMethodBodyLines(methodBodyLines);
        //后续代码写在freemarker模板中了
        return serviceMethod;
    }

    @Override
    protected DomainServiceMethodParameter modifyDomainObjectsById(DomainServiceImplementCodegenParameter codegenParameter) {
        DomainServiceMethodParameter serviceMethod = super.modifyDomainObjectsById(codegenParameter);
        List<String> methodBodyLines = new ArrayList<>();
        String domainObjectsVariableName = codegenParameter.getDomainObjectParameter().getLowerDomainObjectsName();
        methodBodyLines.add(String.format("ValidationAssert.notEmpty(%s, MessageSupplier.ofRequiredParameter(\"%s\"));", domainObjectsVariableName, domainObjectsVariableName));
        serviceMethod.setMethodBodyLines(methodBodyLines);
        //后续代码写在freemarker模板中了
        return serviceMethod;
    }

    @Override
    protected DomainServiceMethodParameter removeDomainObjectById(DomainServiceImplementCodegenParameter codegenParameter) {
        DomainServiceMethodParameter serviceMethod = super.removeDomainObjectById(codegenParameter);
        List<String> methodBodyLines = new ArrayList<>();
        String domainObjectIdName = codegenParameter.getDomainObjectParameter().getDomainObjectIdName();
        DomainEntityConfig domainEntityConfig = getDomainObjectConfig();
        List<DomainEntityColumnConfig> idColumns = domainEntityConfig.getIdColumns();
        if(idColumns.size() == 1) { //单一主键
            String idFieldName = idColumns.get(0).getIntrospectedColumn().getJavaFieldName();
            String idFieldType = idColumns.get(0).getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters();
            methodBodyLines.add(String.format("BeanValidator.validateProperty(%s, %s);", domainObjectIdName, domainEntityConfig.getDomainEntityName() + "::" + CodegenUtils.getGetterMethodName(idFieldName, idFieldType)));
        } else {
            methodBodyLines.add(String.format("BeanValidator.validateMap(%s%s);", domainObjectIdName, domainEntityConfig.getValidateFields("byId")));
        }
        serviceMethod.setMethodBodyLines(methodBodyLines);
        //后续代码写在freemarker模板中了
        return serviceMethod;
    }

    @Override
    protected DomainServiceMethodParameter removeDomainObjectsByIds(DomainServiceImplementCodegenParameter codegenParameter) {
        DomainServiceMethodParameter serviceMethod = super.removeDomainObjectsByIds(codegenParameter);
        List<String> methodBodyLines = new ArrayList<>();
        String domainObjectIdsName = codegenParameter.getDomainObjectParameter().getDomainObjectIdsName();
        methodBodyLines.add(String.format("ValidationAssert.notEmpty(%s, MessageSupplier.ofRequiredParameter(\"%s\"));", domainObjectIdsName, domainObjectIdsName));
        serviceMethod.setMethodBodyLines(methodBodyLines);
        //后续代码写在freemarker模板中了
        return serviceMethod;
    }

    @Override
    protected ByMasterIdDomainServiceMethodParameter removeDomainObjectsByMasterId(DomainServiceImplementCodegenParameter codegenParameter, DomainEntityConfig slaveDomainEntityConfig, DomainEntityConfig masterDomainEntityConfig, DomainAggregateSlaveConfig domainAggregateSlaveConfig, String masterIdNameOfSlave) {
        ByMasterIdDomainServiceMethodParameter serviceMethod = super.removeDomainObjectsByMasterId(codegenParameter, slaveDomainEntityConfig, masterDomainEntityConfig, domainAggregateSlaveConfig, masterIdNameOfSlave);
        List<String> methodBodyLines = new ArrayList<>();
        String slaveDomainObjectName = codegenParameter.getDomainObjectParameter().getDomainObjectName();
        String getMasterDomainObjectIdRef = slaveDomainObjectName + "::" + CodegenUtils.getGetterMethodName(masterIdNameOfSlave, serviceMethod.getMasterDomainObjectParameter().getDomainObjectIdType());
        methodBodyLines.add(String.format("BeanValidator.validateProperty(%s, %s);", masterIdNameOfSlave, getMasterDomainObjectIdRef));
        methodBodyLines.add(String.format("QueryCriteria<%s> criteria = LambdaQueryCriteria.ofSupplier(%s::new)", slaveDomainObjectName, slaveDomainObjectName));
        methodBodyLines.add(String.format("    .eq(%s, %s);", getMasterDomainObjectIdRef, masterIdNameOfSlave));
        serviceMethod.setMethodBodyLines(methodBodyLines);
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(QueryCriteria.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(LambdaQueryCriteria.class.getName()));
        return serviceMethod;
    }

    @Override
    protected DomainServiceMethodParameter getDomainObjectById(DomainServiceImplementCodegenParameter codegenParameter) {
        return super.getDomainObjectById(codegenParameter);
    }

    @Override
    protected DomainServiceMethodParameter getDomainObjectsByIds(DomainServiceImplementCodegenParameter codegenParameter) {
        return super.getDomainObjectsByIds(codegenParameter);
    }

    @Override
    protected ByMasterIdDomainServiceMethodParameter getDomainObjectsByMasterId(DomainServiceImplementCodegenParameter codegenParameter, DomainEntityConfig slaveDomainEntityConfig, DomainEntityConfig masterDomainEntityConfig, DomainAggregateSlaveConfig domainAggregateSlaveConfig, String masterIdNameOfSlave) {
        ByMasterIdDomainServiceMethodParameter serviceMethod = super.getDomainObjectsByMasterId(codegenParameter, slaveDomainEntityConfig, masterDomainEntityConfig, domainAggregateSlaveConfig, masterIdNameOfSlave);
        String mapperSelectMethod = DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation()) ? "selectListByCriteria" : "selectByCriteria";
        List<String> methodBodyLines = new ArrayList<>();
        String slaveDomainObjectName = codegenParameter.getDomainObjectParameter().getDomainObjectName();
        methodBodyLines.add(String.format("if(!ObjectUtils.isEmpty(%s)) {", masterIdNameOfSlave));
        methodBodyLines.add(String.format("    QueryCriteria<%s> criteria = LambdaQueryCriteria.ofSupplier(%s::new)", slaveDomainObjectName, slaveDomainObjectName));
        methodBodyLines.add(String.format("        .eq(%s::%s, %s);", slaveDomainObjectName, CodegenUtils.getGetterMethodName(masterIdNameOfSlave, serviceMethod.getMasterDomainObjectParameter().getDomainObjectIdType()), masterIdNameOfSlave));
        methodBodyLines.add(String.format("    return %s.%s(criteria);", codegenParameter.getMapperInstanceName(), mapperSelectMethod));
        methodBodyLines.add("}");
        methodBodyLines.add("return Collections.emptyList();");
        serviceMethod.setMethodBodyLines(methodBodyLines);
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(QueryCriteria.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(LambdaQueryCriteria.class.getName()));
        return serviceMethod;
    }

    @Override
    protected ByMasterIdDomainServiceMethodParameter getDomainObjectsByMasterIds(DomainServiceImplementCodegenParameter codegenParameter, DomainEntityConfig slaveDomainEntityConfig, DomainEntityConfig masterDomainEntityConfig, DomainAggregateSlaveConfig domainAggregateSlaveConfig, String masterIdNameOfSlave) {
        ByMasterIdDomainServiceMethodParameter serviceMethod = super.getDomainObjectsByMasterIds(codegenParameter, slaveDomainEntityConfig, masterDomainEntityConfig, domainAggregateSlaveConfig, masterIdNameOfSlave);
        List<String> methodBodyLines = new ArrayList<>();
        String masterIdsNameOfSlave = serviceMethod.getMasterIdsNameOfSlave();
        String slaveDomainObjectName = codegenParameter.getDomainObjectParameter().getDomainObjectName();
        String getMasterIdOfSlaveRef = slaveDomainObjectName + "::" + CodegenUtils.getGetterMethodName(serviceMethod.getMasterIdNameOfSlave(), serviceMethod.getMasterDomainObjectParameter().getDomainObjectIdType());
        methodBodyLines.add(String.format("if(!CollectionUtils.isEmpty(%s)) {", masterIdsNameOfSlave));
        methodBodyLines.add(String.format("    QueryCriteria<%s> criteria = LambdaQueryCriteria.ofSupplier(%s::new)", slaveDomainObjectName, slaveDomainObjectName));
        methodBodyLines.add(String.format("        .in(%s, %s.toArray());", getMasterIdOfSlaveRef, masterIdsNameOfSlave));
        String slaveDomainObjectsName = codegenParameter.getDomainObjectParameter().getLowerDomainObjectsName();
        methodBodyLines.add(String.format("    List<%s> %s = %s.selectModelListByCriteria(criteria);", slaveDomainObjectName, slaveDomainObjectsName, codegenParameter.getMapperInstanceName()));
        methodBodyLines.add(String.format("    if(!CollectionUtils.isEmpty(%s)) {", slaveDomainObjectsName));
        if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation())) { //如果是1:N关系?
            methodBodyLines.add(String.format("        return %s.stream().collect(Collectors.groupingBy(%s, Collectors.toList()));", slaveDomainObjectsName, getMasterIdOfSlaveRef));
        } else if(DomainMasterSlaveRelation.RELATION_11.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation())) { //如果是1:1关系?
            methodBodyLines.add(String.format("        return %s.stream().collect(Collectors.toMap(%s, Function.identity()));", slaveDomainObjectsName, getMasterIdOfSlaveRef));
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Function.class.getName()));
        }
        methodBodyLines.add("    }");
        methodBodyLines.add("}");
        methodBodyLines.add("return Collections.emptyMap();");
        serviceMethod.setMethodBodyLines(methodBodyLines);
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Collectors.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(QueryCriteria.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(LambdaQueryCriteria.class.getName()));
        return serviceMethod;
    }

    @Override
    protected DomainServiceMethodParameter getDomainObjectsByPage(DomainServiceImplementCodegenParameter codegenParameter) {
        DomainServiceMethodParameter serviceMethod = super.getDomainObjectsByPage(codegenParameter);
        if(serviceMethod != null) {
            DomainEntityConfig domainEntityConfig = getDomainObjectConfig();
            String domainObjectVariableName = codegenParameter.getDomainObjectParameter().getLowerDomainObjectName();
            StringBuilder criteriaCodes = new StringBuilder(String.format("QueryCriteria<%s> criteria = LambdaQueryCriteria.of(condition)", domainObjectVariableName)).append("\n");
            Map<String, DomainEntityFieldConfig> domainEntityFields = domainEntityConfig.getDomainEntityFields();
            for(Map.Entry<String,DomainEntityFieldConfig> entry : domainEntityFields.entrySet()) {
                DomainEntityFieldConfig domainEntityField = entry.getValue();
                if(domainEntityField.getQueryConditionOperator() != null) {
                    String fieldGetterName = CodegenUtils.getGetterMethodName(domainEntityField.getFieldName(), domainEntityField.getFieldType().getFullyQualifiedNameWithoutTypeParameters());
                    if(!domainEntityField.getFieldGroup().isSupportField()) { //持久化字段
                        criteriaCodes.append("                .").append(domainEntityField.getQueryConditionOperator().getOpName()).append("(").append(domainEntityConfig.getDomainEntityName()).append("::").append(fieldGetterName).append(")").append("\n");
                    } else { //辅助字段
                        DomainEntityColumnConfig refDomainEntityColumn = domainEntityField.getDomainEntityColumnConfig();
                        String refFieldGetterName = CodegenUtils.getGetterMethodName(refDomainEntityColumn.getIntrospectedColumn().getJavaFieldName(), refDomainEntityColumn.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters());
                        criteriaCodes.append("                .").append(domainEntityField.getQueryConditionOperator().getOpName()).append("(").append(domainEntityConfig.getDomainEntityName()).append("::").append(refFieldGetterName).append(", condition.").append(fieldGetterName).append("())").append("\n");
                    }
                }
            }
            criteriaCodes.append("                .dynamic(true)").append("\n");
            criteriaCodes.append("                .orderBy(page.getOrderBys());");
            serviceMethod.setMethodBodyLines(Collections.singletonList(criteriaCodes.toString()));
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(QueryCriteria.class.getName()));
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(LambdaQueryCriteria.class.getName()));
        }
        return serviceMethod;
    }

    @Override
    protected DomainServiceMethodParameter getDomainObjectTotalCount(DomainServiceImplementCodegenParameter codegenParameter) {
        return super.getDomainObjectTotalCount(codegenParameter);
    }

    @Override
    protected DomainServiceMethodParameter forEachDomainObject1(DomainServiceImplementCodegenParameter codegenParameter) {
        return super.forEachDomainObject1(codegenParameter);
    }

    @Override
    protected DomainServiceMethodParameter forEachDomainObject2(DomainServiceImplementCodegenParameter codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Cursor.class.getName()));
        return super.forEachDomainObject2(codegenParameter);
    }

    @Override
    protected String getTargetTemplateName() {
        return "DomainServiceImplement.ftl";
    }

}
