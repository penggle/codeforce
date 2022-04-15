package com.penglecode.codeforce.examples.mapper.test;

import com.penglecode.codeforce.examples.mybatis.domain.model.ComponentMeta;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Map;

/**
 * @author pengpeng
 * @version 1.0
 */
public class TypeReferenceExample {

    public static void main(String[] args) {
        ParameterizedTypeReference<Map<String,Object>> typeReference1 = new ParameterizedTypeReference<Map<String, Object>>() {};
        System.out.println(typeReference1.getType().getTypeName());

        ParameterizedTypeReference<ComponentMeta> typeReference2 = new ParameterizedTypeReference<ComponentMeta>() {};
        System.out.println(typeReference2.getType().getTypeName());
    }

}
