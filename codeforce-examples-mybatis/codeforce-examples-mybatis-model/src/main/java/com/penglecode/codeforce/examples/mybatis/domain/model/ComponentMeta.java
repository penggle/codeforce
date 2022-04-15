package com.penglecode.codeforce.examples.mybatis.domain.model;

import com.penglecode.codeforce.common.domain.DomainObject;
import com.penglecode.codeforce.common.domain.EntityObject;
import com.penglecode.codeforce.mybatistiny.annotations.Column;
import com.penglecode.codeforce.mybatistiny.annotations.GenerationType;
import com.penglecode.codeforce.mybatistiny.annotations.Id;
import com.penglecode.codeforce.mybatistiny.annotations.Table;
import com.penglecode.codeforce.mybatistiny.type.Jackson2TypeHandler;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author pengpeng
 * @version 1.0
 */
@Table("t_component_meta")
public class ComponentMeta implements EntityObject {

    private static final long serialVersionUID = 1L;

    /** 组件代码 */
    @NotBlank(message="组件代码不能为空!")
    @Id(strategy=GenerationType.NONE)
    private String componentCode;

    /** 组件名称 */
    @NotBlank(message="组件名称不能为空!")
    private String componentName;

    /** 组件类型*/
    @NotBlank(message="组件类型不能为空!")
    private String componentType;

    /** 组件属性 */
    @NotBlank(message="组件属性不能为空!")
    @Column(typeHandler=ComponentPropsTypeHandler.class)
    private Map<String,Object> componentProps;

    /** 组件API列表 */
    @NotBlank(message="组件API列表不能为空!")
    @Column(typeHandler=ComponentApisTypeHandler.class)
    private List<ComponentApiMeta> componentApis;

    /** 创建时间 */
    @NotBlank(message="创建时间不能为空!")
    @Column(updatable=false, select="DATE_FORMAT({name}, '%Y-%m-%d %T')")
    private String createTime;

    /** 最近修改时间 */
    @NotBlank(message="最近更新时间不能为空!")
    @Column(select="DATE_FORMAT({name}, '%Y-%m-%d %T')")
    private String updateTime;

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public Map<String, Object> getComponentProps() {
        return componentProps;
    }

    public void setComponentProps(Map<String, Object> componentProps) {
        this.componentProps = componentProps;
    }

    public List<ComponentApiMeta> getComponentApis() {
        return componentApis;
    }

    public void setComponentApis(List<ComponentApiMeta> componentApis) {
        this.componentApis = componentApis;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String identity() {
        return componentCode;
    }

    public static class ComponentPropsTypeHandler extends Jackson2TypeHandler<Map<String,Object>> {

        public ComponentPropsTypeHandler(Class<Map<String, Object>> javaType) {
            super(javaType);
        }

    }

    public static class ComponentApisTypeHandler extends Jackson2TypeHandler<List<ComponentApiMeta>> {

        public ComponentApisTypeHandler(Class<List<ComponentApiMeta>> javaType) {
            super(javaType);
        }

    }

    public static class ComponentApiMeta implements DomainObject {

        private String apiName;

        private String apiMethod;

        private String apiUrl;

        public ComponentApiMeta() {
        }

        public ComponentApiMeta(String apiName, String apiMethod, String apiUrl) {
            this.apiName = apiName;
            this.apiMethod = apiMethod;
            this.apiUrl = apiUrl;
        }

        public String getApiName() {
            return apiName;
        }

        public void setApiName(String apiName) {
            this.apiName = apiName;
        }

        public String getApiMethod() {
            return apiMethod;
        }

        public void setApiMethod(String apiMethod) {
            this.apiMethod = apiMethod;
        }

        public String getApiUrl() {
            return apiUrl;
        }

        public void setApiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
        }
    }

}
