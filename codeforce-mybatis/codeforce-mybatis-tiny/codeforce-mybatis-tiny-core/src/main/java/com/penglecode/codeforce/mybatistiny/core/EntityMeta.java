package com.penglecode.codeforce.mybatistiny.core;

import com.penglecode.codeforce.common.domain.EntityObject;
import com.penglecode.codeforce.common.util.StringUtils;
import com.penglecode.codeforce.mybatistiny.annotations.Column;
import com.penglecode.codeforce.mybatistiny.annotations.Id;
import com.penglecode.codeforce.mybatistiny.annotations.Table;
import com.penglecode.codeforce.mybatistiny.annotations.Transient;
import com.penglecode.codeforce.mybatistiny.support.JavaJdbcTypeEnum;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 实体元数据信息
 *
 * 实体对象(EntityObject)，它对应着数据库中的一张表；而领域对象(DomainObject)则是包含了实体对象，不一定对应着数据库中的一张表
 *
 * @author pengpeng
 * @version 1.0
 */
public class EntityMeta<E extends EntityObject> {

    /** 实体类Class */
    private final Class<E> entityClass;

    /** 以fieldName为key的实体字段名映射 */
    private final Map<String,EntityField> fieldNameKeyedFields;

    /** 以columnName为key的实体字段名映射 */
    private final Map<String,EntityField> columnNameKeyedFields;

    /** 实体类上的@Table注解 */
    private final Table tableAnnotation;

    protected EntityMeta(Class<E> entityClass) {
        this.entityClass = entityClass;
        this.fieldNameKeyedFields = resolveEntityFields(entityClass, EntityField::getFieldName);
        this.columnNameKeyedFields = resolveEntityFields(entityClass, EntityField::getColumnName);
        this.tableAnnotation = entityClass.getAnnotation(Table.class);
        Assert.state(tableAnnotation != null, String.format("EntityObject[%s] must be annotated with @%s", entityClass.getName(), Table.class.getName()));
        Assert.state(fieldNameKeyedFields.values().stream().anyMatch(field -> field.getIdAnnotation() != null), String.format("EntityObject[%s] must specify an id which annotated with @%s", entityClass.getName(), Id.class.getName()));
    }

    protected Map<String,EntityField> resolveEntityFields(Class<E> entityClass, Function<EntityField,String> keyFunction) {
        List<Field> entityFields = new ArrayList<>();
        ReflectionUtils.doWithFields(entityClass, entityFields::add, this::isEntityField);
        return entityFields.stream().map(EntityField::new).collect(Collectors.toMap(keyFunction, Function.identity(), (u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); }, LinkedHashMap::new));
    }

    protected boolean isEntityField(Field field) {
        if(field.getModifiers() == Modifier.PRIVATE) { //实体字段必须只能是private修饰的!
            return field.getAnnotation(Transient.class) == null;
        }
        return false;
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public Map<String, EntityField> getFieldNameKeyedFields() {
        return fieldNameKeyedFields;
    }

    public Map<String, EntityField> getColumnNameKeyedFields() {
        return columnNameKeyedFields;
    }

    public Table getTableAnnotation() {
        return tableAnnotation;
    }

    public static class EntityField {

        /** 实体字段名 */
        private final String fieldName;

        /** 实体字段类型 */
        private final Class<?> fieldType;

        /** 实体列名 */
        private final String columnName;

        /** 实体列名的jdbcType */
        private final JdbcType jdbcType;

        /** 实体字段的java.lang.reflect.Field实例 */
        private final Field javaField;

        /** 实体字段上的@Id注解，如果当前字段是主键的话，则该值不为空 */
        private final Id idAnnotation;

        /** 实体字段上的@Column注解, 如果当前字段被@Column注解的话，则该值不为空 */
        private final Column columnAnnotation;

        public EntityField(Field javaField) {
            this.fieldName = javaField.getName();
            this.fieldType = javaField.getType();
            this.columnName = resolveColumnName(javaField);
            this.jdbcType = JavaJdbcTypeEnum.getJdbcType(this.fieldType);
            this.javaField = javaField;
            this.idAnnotation = javaField.getAnnotation(Id.class);
            this.columnAnnotation = javaField.getAnnotation(Column.class);
        }

        protected String resolveColumnName(Field javaField) {
            Column columnAnnotation = javaField.getAnnotation(Column.class);
            String columnName = null;
            if(columnAnnotation != null) {
                columnName = columnAnnotation.name();
            }
            if(StringUtils.isBlank(columnName)) {
                return StringUtils.camelNamingToSnake(javaField.getName());
            }
            return columnName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Class<?> getFieldType() {
            return fieldType;
        }

        public String getColumnName() {
            return columnName;
        }

        public JdbcType getJdbcType() {
            return jdbcType;
        }

        public Field getJavaField() {
            return javaField;
        }

        public Id getIdAnnotation() {
            return idAnnotation;
        }

        public Column getColumnAnnotation() {
            return columnAnnotation;
        }

    }

}
