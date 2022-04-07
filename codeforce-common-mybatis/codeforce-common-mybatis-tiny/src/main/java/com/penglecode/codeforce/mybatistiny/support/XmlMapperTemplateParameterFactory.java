package com.penglecode.codeforce.mybatistiny.support;

import com.penglecode.codeforce.common.domain.EntityObject;
import com.penglecode.codeforce.common.util.ReflectionUtils;
import com.penglecode.codeforce.mybatistiny.annotations.*;
import com.penglecode.codeforce.mybatistiny.dsl.QueryCriteria;
import com.penglecode.codeforce.mybatistiny.mapper.BaseMybatisMapper;
import com.penglecode.codeforce.mybatistiny.support.XmlMapperTemplateParameter.ColumnParameter;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BaseMybatisMapper.ftl模板参数Factory
 *
 * @author pengpeng
 * @version 1.0
 */
public class XmlMapperTemplateParameterFactory<E extends EntityObject> {

    private final DatabaseDialect databaseDialect;

    public XmlMapperTemplateParameterFactory(String databaseId) {
        DatabaseDialect databaseDialect = DatabaseDialect.of(databaseId);
        Assert.notNull(databaseDialect, String.format("Unsupported Database: No suitable DatabaseDialect found for databaseId(%s) in Mybatis Configuration!", databaseId));
        this.databaseDialect = databaseDialect;
    }

    public XmlMapperTemplateParameter createTemplateParameter(Class<BaseMybatisMapper<E>> entityMapperClass) {
        XmlMapperTemplateParameter parameter = newTemplateParameter();
        parameter.setEntityMapperClass(entityMapperClass);
        ResolvableType resolvableType = ResolvableType.forClass(BaseMybatisMapper.class, entityMapperClass);
        parameter.setEntityClass(resolvableType.getGeneric(0).resolve());
        List<Field> entityFields = new ArrayList<>();
        ReflectionUtils.doWithFields(parameter.getEntityClass(), entityFields::add, this::isEntityField);
        parameter.setEntityFields(entityFields);
        return setTemplateCustomParameter(setTemplateCommonParameter(parameter));
    }

    protected XmlMapperTemplateParameter newTemplateParameter() {
        return new XmlMapperTemplateParameter();
    }

    protected XmlMapperTemplateParameter setTemplateCommonParameter(XmlMapperTemplateParameter parameter) {
        parameter.setMapperNamespace(parameter.getEntityMapperClass().getName());
        parameter.setMapperHelperClass(XmlMapperHelper.class.getName());
        parameter.setEntityName(parameter.getEntityClass().getSimpleName());

        Table tableAnnotation = parameter.getEntityClass().getAnnotation(Table.class);
        parameter.setTableName(tableAnnotation.value());
        parameter.setTableAlias(QueryCriteria.TABLE_ALIAS_NAME);

        List<Field> entityFields = parameter.getEntityFields();
        List<ColumnParameter> allColumns = entityFields.stream().map(ColumnParameter::new).collect(Collectors.toList());
        parameter.setIdColumns(allColumns.stream().filter(this::isIdColumn).collect(Collectors.toList()));
        parameter.setInsertColumns(allColumns.stream().filter(this::isInsertColumn).collect(Collectors.toList()));
        parameter.setUpdateColumns(allColumns.stream().filter(this::isUpdateColumn).collect(Collectors.toList()));
        parameter.setSelectColumns(allColumns);
        parameter.setAllColumns(allColumns);

        List<ColumnParameter> idColumns = parameter.getIdColumns();
        Assert.state(!CollectionUtils.isEmpty(idColumns), String.format("实体(%s)的未发现具有@Id注解的ID列!", parameter.getEntityClass()));
        if(idColumns.size() == 1) { //单一主键?
            ColumnParameter singleIdColumn = idColumns.get(0);
            Id idAnnotation = singleIdColumn.getJavaField().getAnnotation(Id.class);
            parameter.setIdStrategy(idAnnotation.strategy().name());
            parameter.setIdGenerator(idAnnotation.generator());
        } else { //组合主键
            parameter.setIdStrategy(GenerationType.NONE.name());
            parameter.setIdGenerator(null);
        }

        parameter.setDeleteTargetAlias(databaseDialect.getDeleteTargetAlias());
        parameter.setCursorFetchSize(String.valueOf(databaseDialect.getCursorFetchSize()));
        return parameter;
    }

    protected XmlMapperTemplateParameter setTemplateCustomParameter(XmlMapperTemplateParameter parameter) {
        return parameter;
    }

    protected boolean isEntityField(Field field) {
        if(field.getModifiers() == Modifier.PRIVATE) { //实体字段必须只能是private修饰的!
            return field.getAnnotation(Transient.class) == null;
        }
        return false;
    }

    protected boolean isIdColumn(ColumnParameter columnParameter) {
        return columnParameter.isIdColumn();
    }

    protected boolean isInsertColumn(ColumnParameter columnParameter) {
        Column columnAnnotation = columnParameter.getJavaField().getAnnotation(Column.class);
        return columnAnnotation == null || columnAnnotation.insertable();
    }

    protected boolean isUpdateColumn(ColumnParameter columnParameter) {
        Id idAnnotation = columnParameter.getJavaField().getAnnotation(Id.class);
        if(idAnnotation != null) {
            return idAnnotation.updatable();
        } else {
            Column columnAnnotation = columnParameter.getJavaField().getAnnotation(Column.class);
            return columnAnnotation == null || columnAnnotation.updatable();
        }
    }

    protected DatabaseDialect getDatabaseDialect() {
        return databaseDialect;
    }

}
