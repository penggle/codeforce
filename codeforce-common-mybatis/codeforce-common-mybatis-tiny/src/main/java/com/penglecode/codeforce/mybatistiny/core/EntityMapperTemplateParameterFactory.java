package com.penglecode.codeforce.mybatistiny.core;

import com.penglecode.codeforce.common.domain.EntityObject;
import com.penglecode.codeforce.mybatistiny.annotations.Column;
import com.penglecode.codeforce.mybatistiny.annotations.GenerationType;
import com.penglecode.codeforce.mybatistiny.annotations.Id;
import com.penglecode.codeforce.mybatistiny.annotations.Table;
import com.penglecode.codeforce.mybatistiny.dsl.QueryCriteria;
import com.penglecode.codeforce.mybatistiny.core.EntityMeta.EntityField;
import com.penglecode.codeforce.mybatistiny.core.EntityMapperTemplateParameter.ColumnParameter;
import com.penglecode.codeforce.mybatistiny.mapper.BaseEntityMapper;
import com.penglecode.codeforce.mybatistiny.support.XmlMapperHelper;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * BaseEntityMapper.ftl模板参数Factory
 *
 * @author pengpeng
 * @version 1.0
 */
public class EntityMapperTemplateParameterFactory<E extends EntityObject> {

    private final DatabaseDialect databaseDialect;

    public EntityMapperTemplateParameterFactory(String databaseId) {
        DatabaseDialect databaseDialect = DatabaseDialect.of(databaseId);
        Assert.notNull(databaseDialect, String.format("Unsupported Database: No suitable DatabaseDialect found for databaseId(%s) in Mybatis Configuration!", databaseId));
        this.databaseDialect = databaseDialect;
    }

    public EntityMapperTemplateParameter<E> createTemplateParameter(Class<BaseEntityMapper<E>> entityMapperClass, EntityMeta<E> entityMeta) {
        EntityMapperTemplateParameter<E> parameter = newTemplateParameter();
        parameter.setEntityMapperClass(entityMapperClass);
        parameter.setEntityMeta(entityMeta);
        return setTemplateCustomParameter(setTemplateCommonParameter(parameter));
    }

    protected EntityMapperTemplateParameter<E> newTemplateParameter() {
        return new EntityMapperTemplateParameter<>();
    }

    protected EntityMapperTemplateParameter<E> setTemplateCommonParameter(EntityMapperTemplateParameter<E> parameter) {
        parameter.setMapperNamespace(parameter.getEntityMapperClass().getName());
        parameter.setMapperHelperClass(XmlMapperHelper.class.getName());
        parameter.setEntityName(parameter.getEntityMeta().getEntityClass().getSimpleName());
        parameter.setEntityClass(parameter.getEntityMeta().getEntityClass().getName());

        Table tableAnnotation = parameter.getEntityMeta().getTableAnnotation();
        parameter.setTableName(tableAnnotation.value());
        parameter.setTableAlias(QueryCriteria.TABLE_ALIAS_NAME);

        Map<String,EntityField> entityFields = parameter.getEntityMeta().getFieldNameKeyedFields();
        List<ColumnParameter> allColumns = entityFields.values().stream().map(ColumnParameter::new).collect(Collectors.toList());
        parameter.setIdColumns(allColumns.stream().filter(this::isIdColumn).collect(Collectors.toList()));
        parameter.setInsertColumns(allColumns.stream().filter(this::isInsertColumn).collect(Collectors.toList()));
        parameter.setUpdateColumns(allColumns.stream().filter(this::isUpdateColumn).collect(Collectors.toList()));
        parameter.setSelectColumns(allColumns);
        parameter.setAllColumns(allColumns);

        List<ColumnParameter> idColumns = parameter.getIdColumns();
        Assert.state(!CollectionUtils.isEmpty(idColumns), String.format("实体(%s)的未发现具有@Id注解的ID列!", parameter.getEntityMeta().getEntityClass()));
        if(idColumns.size() == 1) { //单一主键?
            ColumnParameter singleIdColumn = idColumns.get(0);
            Id idAnnotation = singleIdColumn.getEntityField().getIdAnnotation();
            parameter.setIdStrategy(idAnnotation.strategy().name());
            parameter.setIdGenerator(idAnnotation.generator());
        } else { //组合主键
            parameter.setIdStrategy(GenerationType.NONE.name());
            parameter.setIdGenerator(null);
        }

        parameter.setDeleteTargetAlias(databaseDialect.getDeleteTargetAlias());
        return parameter;
    }

    protected EntityMapperTemplateParameter<E> setTemplateCustomParameter(EntityMapperTemplateParameter<E> parameter) {
        return parameter;
    }

    protected boolean isIdColumn(ColumnParameter columnParameter) {
        return columnParameter.isIdColumn();
    }

    protected boolean isInsertColumn(ColumnParameter columnParameter) {
        Column columnAnnotation = columnParameter.getEntityField().getColumnAnnotation();
        return columnAnnotation == null || columnAnnotation.insertable();
    }

    protected boolean isUpdateColumn(ColumnParameter columnParameter) {
        Id idAnnotation = columnParameter.getEntityField().getIdAnnotation();
        if(idAnnotation != null) {
            return idAnnotation.updatable();
        } else {
            Column columnAnnotation = columnParameter.getEntityField().getColumnAnnotation();
            return columnAnnotation == null || columnAnnotation.updatable();
        }
    }

    protected DatabaseDialect getDatabaseDialect() {
        return databaseDialect;
    }

}
