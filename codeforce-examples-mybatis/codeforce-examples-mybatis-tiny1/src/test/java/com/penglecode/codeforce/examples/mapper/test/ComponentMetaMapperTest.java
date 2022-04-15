package com.penglecode.codeforce.examples.mapper.test;

import com.penglecode.codeforce.common.support.MapLambdaBuilder;
import com.penglecode.codeforce.common.util.DateTimeUtils;
import com.penglecode.codeforce.common.util.JsonUtils;
import com.penglecode.codeforce.examples.mybatis.domain.model.ComponentMeta;
import com.penglecode.codeforce.examples.mybatis.domain.model.ComponentMeta.ComponentApiMeta;
import com.penglecode.codeforce.examples.mybatistiny.boot.MybatisTinyExampleApplication;
import com.penglecode.codeforce.examples.mybatistiny.mapper.ComponentMetaMapper;
import com.penglecode.codeforce.mybatistiny.dsl.LambdaQueryCriteria;
import com.penglecode.codeforce.mybatistiny.dsl.QueryCriteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pengpeng
 * @version 1.0
 */
@SpringBootTest(classes=MybatisTinyExampleApplication.class)
public class ComponentMetaMapperTest {

    @Autowired
    private ComponentMetaMapper componentMetaMapper;

    @Test
    public void createComponentMeta() {
        ComponentMeta componentMeta = new ComponentMeta();
        componentMeta.setComponentCode("StaffSelector");
        componentMeta.setComponentName("人员选择器");
        componentMeta.setComponentType("业务组件");

        Map<String,Object> properties = new HashMap<>();
        properties.put("rootOrgId", 1024);
        properties.put("pageSize", 50);
        componentMeta.setComponentProps(properties);

        List<ComponentApiMeta> apiList = new ArrayList<>();
        apiList.add(new ComponentApiMeta("queryOrgTree", "GET", "/api/org/tree"));
        apiList.add(new ComponentApiMeta("queryStaffList", "GET", "/api/staff/list"));
        componentMeta.setComponentApis(apiList);
        componentMeta.setCreateTime(DateTimeUtils.formatNow());
        componentMeta.setUpdateTime(componentMeta.getCreateTime());

        componentMetaMapper.insert(componentMeta);
    }

    @Test
    public void updateComponentMeta() {
        ComponentMeta componentMeta = new ComponentMeta();
        componentMeta.setComponentCode("StaffSelector");
        componentMeta.setComponentName("人员选择器(Enhanced)");
        componentMeta.setComponentType("业务组件");

        Map<String,Object> properties = new HashMap<>();
        properties.put("rootOrgId", 1024);
        properties.put("staffRoleId", "ADMIN");
        properties.put("pageSize", 50);
        componentMeta.setComponentProps(properties);

        List<ComponentApiMeta> apiList = new ArrayList<>();
        apiList.add(new ComponentApiMeta("queryRoleList", "GET", "/api/role/list"));
        apiList.add(new ComponentApiMeta("queryOrgTree", "GET", "/api/org/tree"));
        apiList.add(new ComponentApiMeta("queryStaffList", "GET", "/api/staff/list"));
        componentMeta.setComponentApis(apiList);
        componentMeta.setCreateTime(DateTimeUtils.formatNow());
        componentMeta.setUpdateTime(componentMeta.getCreateTime());

        Map<String,Object> updateColumns = MapLambdaBuilder.of(componentMeta)
                .with(ComponentMeta::getComponentName)
                .with(ComponentMeta::getComponentType)
                .with(ComponentMeta::getComponentProps)
                .with(ComponentMeta::getComponentApis)
                .build();
        componentMetaMapper.updateById(componentMeta.identity(), updateColumns);
    }

    @Test
    public void queryComponentMeta() {
        ComponentMeta componentMeta = componentMetaMapper.selectById("StaffSelector");
        System.out.println(componentMeta);

        QueryCriteria<ComponentMeta> queryCriteria = LambdaQueryCriteria.ofSupplier(ComponentMeta::new)
                .eq(ComponentMeta::getComponentCode, "StaffSelector");
        List<ComponentMeta> componentList = componentMetaMapper.selectListByCriteria(queryCriteria);
        if(componentList != null) {
            componentList.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }
    }

}
