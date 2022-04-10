package com.penglecode.codeforce.examples.mybatistiny.mapper;

import com.penglecode.codeforce.examples.mybatis.domain.model.ProductExtraInfo;
import com.penglecode.codeforce.mybatistiny.mapper.BaseEntityMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author pengpeng
 * @version 1.0
 */
@Mapper
public interface ProductExtraInfoMapper extends BaseEntityMapper<ProductExtraInfo> {
}
