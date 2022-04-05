package com.penglecode.codeforce.mybatistiny.support;

import com.penglecode.codeforce.common.domain.EntityObject;
import com.penglecode.codeforce.common.util.ClassUtils;
import com.penglecode.codeforce.common.util.ReflectionUtils;
import com.penglecode.codeforce.mybatistiny.annotations.*;
import com.penglecode.codeforce.mybatistiny.dsl.QueryCriteria;
import com.penglecode.codeforce.mybatistiny.mapper.BaseMybatisMapper;
import com.penglecode.codeforce.mybatistiny.support.MapperTemplateParameter.ColumnParameter;
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
public class MapperTemplateParameterFactory<E extends EntityObject> {

    private final DatabaseDialect databaseDialect;

    public MapperTemplateParameterFactory(String databaseId) {
        DatabaseDialect databaseDialect = DatabaseDialect.of(databaseId);
        Assert.notNull(databaseDialect, String.format("No DatabaseDialect found for databaseId(%s) in Mybatis Configuration!", databaseId));
        this.databaseDialect = databaseDialect;
    }

    public MapperTemplateParameter createTemplateParameter(Class<BaseMybatisMapper<E>> entityMapperClass) {
        MapperTemplateParameter parameter = newTemplateParameter();
        parameter.setEntityMapperClass(entityMapperClass);
        parameter.setEntityClass(ClassUtils.getSuperClassGenericType(entityMapperClass, BaseMybatisMapper.class, 0));
        List<Field> entityFields = new ArrayList<>();
        ReflectionUtils.doWithFields(parameter.getEntityClass(), entityFields::add, this::isEntityField);
        parameter.setEntityFields(entityFields);
        return setTemplateCustomParameter(setTemplateCommonParameter(parameter));
    }

    protected MapperTemplateParameter newTemplateParameter() {
        return new MapperTemplateParameter();
    }

    protected MapperTemplateParameter setTemplateCommonParameter(MapperTemplateParameter parameter) {
        parameter.setMapperNamespace(parameter.getEntityMapperClass().getName());
        parameter.setMapperHelperClass(MapperHelper.class.getName());
        parameter.setEntityName(parameter.getEntityClass().getSimpleName());

        Table tableAnnotation = parameter.getEntityClass().getAnnotation(Table.class);
        parameter.setTableName(tableAnnotation.value());
        parameter.setTableAlias(QueryCriteria.TABLE_ALIAS_NAME);

        List<Field> entityFields = parameter.getEntityFields();
        List<ColumnParameter> allColumns = entityFields.stream().map(ColumnParameter::new).collect(Collectors.toList());
        parameter.setIdColumns(allColumns.stream().filter(this::isIdColumn).collect(Collectors.toList()));
        parameter.setInsertColumns(allColumns.stream().filter(this::isInsertColumn).collect(Collectors.toList()));
        parameter.setUpdateColumns(allColumns.stream().filter(this::isInsertColumn).collect(Collectors.toList()));
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
        return parameter;
    }

    protected MapperTemplateParameter setTemplateCustomParameter(MapperTemplateParameter parameter) {
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
        Column columnAnnotation = columnParameter.getJavaField().getAnnotation(Column.class);
        return columnAnnotation == null || columnAnnotation.updatable();
    }

    protected DatabaseDialect getDatabaseDialect() {
        return databaseDialect;
    }

}
