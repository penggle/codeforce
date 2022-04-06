package com.penglecode.codeforce.examples.mybatistiny.mapper;

import com.penglecode.codeforce.examples.mybatis.domain.model.ProductBaseInfo;
import com.penglecode.codeforce.mybatistiny.mapper.BaseMybatisMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author pengpeng
 * @version 1.0
 */
@Mapper
public interface ProductBaseInfoMapper extends BaseMybatisMapper<ProductBaseInfo> {
}
