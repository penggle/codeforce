package com.penglecode.codeforce.examples.product.mapper.test;

import com.penglecode.codeforce.common.domain.ID;
import com.penglecode.codeforce.common.model.OrderBy;
import com.penglecode.codeforce.common.support.MapLambdaBuilder;
import com.penglecode.codeforce.common.util.CollectionUtils;
import com.penglecode.codeforce.common.util.DateTimeUtils;
import com.penglecode.codeforce.common.util.JsonUtils;
import com.penglecode.codeforce.examples.mybatis.domain.enums.ProductAuditStatusEnum;
import com.penglecode.codeforce.examples.mybatis.domain.enums.ProductOnlineStatusEnum;
import com.penglecode.codeforce.examples.mybatis.domain.model.*;
import com.penglecode.codeforce.examples.mybatistiny.boot.MybatisTinyExampleApplication;
import com.penglecode.codeforce.examples.mybatistiny.mapper.ProductBaseInfoMapper;
import com.penglecode.codeforce.examples.mybatistiny.mapper.ProductExtraInfoMapper;
import com.penglecode.codeforce.examples.mybatistiny.mapper.ProductSaleSpecMapper;
import com.penglecode.codeforce.examples.mybatistiny.mapper.ProductSaleStockMapper;
import com.penglecode.codeforce.examples.product.ProductTestHelper;
import com.penglecode.codeforce.mybatistiny.dsl.LambdaQueryCriteria;
import com.penglecode.codeforce.mybatistiny.dsl.QueryColumns;
import com.penglecode.codeforce.mybatistiny.dsl.QueryCriteria;
import org.apache.ibatis.executor.extensions.JdbcBatchOperation;
import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author pengpeng
 * @version 1.0
 * @created 2021/7/24 14:33
 */
@SpringBootTest(classes= MybatisTinyExampleApplication.class)
public class ProductModuleMapperTest {

    @Autowired
    private ProductBaseInfoMapper productBaseInfoMapper;

    @Autowired
    private ProductExtraInfoMapper productExtraInfoMapper;

    @Autowired
    private ProductSaleSpecMapper productSaleSpecMapper;

    @Autowired
    private ProductSaleStockMapper productSaleStockMapper;

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Test
    public void createProduct() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(dataSourceTransactionManager);
        transactionTemplate.execute(this::doCreateProduct);
    }

    protected Object doCreateProduct(TransactionStatus status) {
        ProductAggregate testProduct = ProductTestHelper.genTestProduct4Create();
        testProduct.setProductId(1L);
        //System.out.println(JsonUtils.object2Json(productBase));
        productBaseInfoMapper.insert(testProduct);

        ProductExtraInfo productExtra = testProduct.getProductExtra();
        productExtra.setProductId(testProduct.getProductId());
        productExtraInfoMapper.insert(productExtra);

        List<ProductSaleSpec> productSaleSpecs = testProduct.getProductSaleSpecs();
        try(JdbcBatchOperation batchOperation = new JdbcBatchOperation()) {
            for(ProductSaleSpec productSaleSpec : productSaleSpecs) {
                productSaleSpec.setProductId(testProduct.getProductId());
                productSaleSpecMapper.insert(productSaleSpec);
            }
            productSaleSpecMapper.flushStatements(); //批量提交
        }

        List<ProductSaleStock> productSaleStocks = testProduct.getProductSaleStocks();
        try(JdbcBatchOperation batchOperation = new JdbcBatchOperation()) {
            for(ProductSaleStock productSaleStock : productSaleStocks) {
                productSaleStock.setProductId(testProduct.getProductId());
                productSaleStockMapper.insert(productSaleStock);
            }
            productSaleStockMapper.flushStatements(); //批量提交
        }
        return null;
    }

    @Test
    public void updateProduct() {
        Long productId = 1L;
        ProductBaseInfo productBase = productBaseInfoMapper.selectById(productId);
        System.out.println(JsonUtils.object2Json(productBase));

        QueryCriteria<ProductSaleStock> criteria = LambdaQueryCriteria.ofSupplier(ProductSaleStock::new)
                .eq(ProductSaleStock::getProductId, productId)
                .likeRight(ProductSaleStock::getSpecNo, "11");
        List<ProductSaleStock> productStocks = productSaleStockMapper.selectListByCriteria(criteria);

        if(!CollectionUtils.isEmpty(productStocks)) {
            productStocks.forEach(stock -> System.out.println(JsonUtils.object2Json(stock)));
        }

        String nowTime = DateTimeUtils.formatNow();
        productBase.setAuditStatus(ProductAuditStatusEnum.AUDIT_PASSED.getStatusCode());
        productBase.setOnlineStatus(ProductOnlineStatusEnum.ONLINE.getStatusCode());
        productBase.setUpdateTime(nowTime);
        Map<String,Object> paramMap1 = MapLambdaBuilder.of(productBase)
                .with(ProductBaseInfo::getAuditStatus)
                .with(ProductBaseInfo::getOnlineStatus)
                .with(ProductBaseInfo::getUpdateTime)
                .build();
        productBaseInfoMapper.updateById(productBase.getProductId(), paramMap1);

        ProductExtraInfo productExtra = productExtraInfoMapper.selectById(productId);
        productExtra.setProductServices("商品服务111");
        productExtra.setUpdateTime(nowTime);
        Map<String,Object> paramMap2 = MapLambdaBuilder.of(productExtra)
                .with(ProductExtraInfo::getProductServices)
                .with(ProductExtraInfo::getUpdateTime)
                .build();
        productExtraInfoMapper.updateById(productExtra.getProductId(), paramMap2);

        Map<String,Object> paramMap3 = MapLambdaBuilder.<ProductSaleStock>ofEmpty()
                .withOverride(ProductSaleStock::getSellPrice, 599700)
                .withOverride(ProductSaleStock::getUpdateTime, nowTime)
                .build();
        productSaleStockMapper.updateByCriteria(criteria, paramMap3);
    }

    @Test
    public void queryProduct() {
        Long productId = 1L;
        List<ProductBaseInfo> productBases = productBaseInfoMapper.selectListByIds(Arrays.asList(5L, 6L, 7L, 8L, 9L),
                new QueryColumns(ProductBaseInfo::getProductId, ProductBaseInfo::getProductName, ProductBaseInfo::getProductType));
        if(!CollectionUtils.isEmpty(productBases)) {
            productBases.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }

        System.out.println("-----------------------------------------");

        QueryCriteria<ProductBaseInfo> criteria1 = LambdaQueryCriteria.ofSupplier(ProductBaseInfo::new)
                .eq(ProductBaseInfo::getProductType, 1)
                .orderBy(OrderBy.desc(ProductBaseInfo::getCreateTime))
                .limit(10);
        productBases = productBaseInfoMapper.selectListByCriteria(criteria1);
        if(!CollectionUtils.isEmpty(productBases)) {
            productBases.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }

        System.out.println("-----------------------------------------");

        QueryCriteria<ProductSaleStock> criteria2 = LambdaQueryCriteria.ofSupplier(ProductSaleStock::new)
                .eq(ProductSaleStock::getProductId, productId)
                .likeRight(ProductSaleStock::getSpecNo, "11")
                .orderBy(OrderBy.desc(ProductSaleStock::getSellPrice));
        List<ProductSaleStock> productStocks = productSaleStockMapper.selectListByCriteria(criteria2, new QueryColumns(ProductSaleStock::getProductId, ProductSaleStock::getSpecNo, ProductSaleStock::getSellPrice, ProductSaleStock::getStock));
        if(!CollectionUtils.isEmpty(productStocks)) {
            productStocks.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }

        System.out.println("-----------------------------------------");

        List<ID> idList = new ArrayList<>();
        idList.add(new ID().addKey("productId", productId).addKey("specNo", "11:21:31"));
        idList.add(new ID().addKey("productId", productId).addKey("specNo", "11:21:32"));
        productStocks = productSaleStockMapper.selectListByIds(idList, new QueryColumns(column -> true));
        if(!CollectionUtils.isEmpty(productStocks)) {
            productStocks.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }

        System.out.println("-----------------------------------------");

        ProductSaleStock productStock = productSaleStockMapper.selectById(new ID().addKey("productId", productId).addKey("specNo", "11:21:31"));
        System.out.println(JsonUtils.object2Json(productStock));

        System.out.println("-----------------------------------------");

        QueryCriteria<ProductBaseInfo> criteria3 = LambdaQueryCriteria.ofSupplier(ProductBaseInfo::new)
                .eq(ProductBaseInfo::getProductType, 1)
                .or(subCriteria -> subCriteria.eq(ProductBaseInfo::getOnlineStatus, 1)
                        .like(ProductBaseInfo::getProductName, "华为"))
                .or(subCriteria -> subCriteria.eq(ProductBaseInfo::getAuditStatus, 0)
                        .between(ProductBaseInfo::getCreateTime, "2022-01-05", "2022-01-06"))
                .orderBy(OrderBy.desc(ProductBaseInfo::getCreateTime))
                .limit(10);
        productBases = productBaseInfoMapper.selectListByCriteria(criteria3);
        if(!CollectionUtils.isEmpty(productBases)) {
            productBases.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }

    }

    @Test
    public void queryProductByPage() {
        QueryCriteria<ProductBaseInfo> criteria = LambdaQueryCriteria.ofSupplier(ProductBaseInfo::new)
                .eq(ProductBaseInfo::getProductType, 1)
                .between(ProductBaseInfo::getCreateTime, "2022-01-01", "20220-01-31")
                .orderBy(OrderBy.desc(ProductBaseInfo::getCreateTime))
                .limit(5); //limit与在分页查询(selectPageListByCriteria)时会失效
        System.out.println("totalCount = " + productBaseInfoMapper.selectPageCountByCriteria(criteria));
        System.out.println("--------------------------第1页------------------------");
        List<ProductBaseInfo> productBases1 = productBaseInfoMapper.selectPageListByCriteria(criteria, new RowBounds(0, 10));
        if(!CollectionUtils.isEmpty(productBases1)) {
            productBases1.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }
        System.out.println("--------------------------第2页------------------------");
        List<ProductBaseInfo> productBases2 = productBaseInfoMapper.selectPageListByCriteria(criteria, new RowBounds(10, 10), new QueryColumns(ProductSaleStock::getProductId, ProductSaleStock::getSpecNo, ProductSaleStock::getSellPrice, ProductSaleStock::getStock));
        if(!CollectionUtils.isEmpty(productBases2)) {
            productBases2.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }
    }

    @Test
    public void deleteProduct() {
        Long productId = 1L;
        productSaleStockMapper.deleteById(new ID().addKey("productId", productId).addKey("specNo", "00:10:20"));

        List<ID> idList = new ArrayList<>();
        idList.add(new ID().addKey("productId", productId).addKey("specNo", "00:11:20"));
        idList.add(new ID().addKey("productId", productId).addKey("specNo", "00:11:21"));
        productSaleStockMapper.deleteByIds(idList);

        QueryCriteria<ProductSaleStock> criteria = LambdaQueryCriteria.ofSupplier(ProductSaleStock::new)
                .eq(ProductSaleStock::getProductId, productId)
                .likeRight(ProductSaleStock::getSpecNo, "00");
        productSaleStockMapper.deleteByCriteria(criteria);

        productBaseInfoMapper.deleteByIds(Arrays.asList(1L, 2L, 3L, 4L, 5L));
    }

}