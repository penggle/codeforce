package com.penglecode.codeforce.examples.mybatis.domain.model;

import com.penglecode.codeforce.common.domain.EntityObject;
import com.penglecode.codeforce.common.domain.ID;
import com.penglecode.codeforce.mybatistiny.annotations.Column;
import com.penglecode.codeforce.mybatistiny.annotations.GenerationType;
import com.penglecode.codeforce.mybatistiny.annotations.Id;
import com.penglecode.codeforce.mybatistiny.annotations.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 商品销售库存信息实体
 *
 * @author AutoCodeGenerator
 * @version 1.0
 */
@Table("t_product_sale_stock")
public class ProductSaleStock implements EntityObject {

    private static final long serialVersionUID = 1L;

    /** 商品ID */
    @NotNull(message="商品ID不能为空!")
    @Id(strategy=GenerationType.NONE)
    private Long productId;

    /** 商品规格编号,多个t_product_spec.spec_no按顺序拼凑 */
    @NotBlank(message="规格编号不能为空!")
    @Id(strategy=GenerationType.NONE)
    private String specNo;

    /** 商品规格编号,多个t_product_spec.spec_name按顺序拼凑 */
    @NotBlank(message="规格名称不能为空!")
    private String specName;

    /** 商品售价(单位分) */
    @NotNull(message="售价不能为空!")
    private Long sellPrice;

    /** 库存量 */
    @NotNull(message="库存不能为空!")
    private Integer stock;

    /** 创建时间 */
    @NotBlank(message="创建时间不能为空!")
    @Column(updatable=false)
    private String createTime;

    /** 最近修改时间 */
    @NotBlank(message="最近更新时间不能为空!")
    private String updateTime;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSpecNo() {
        return specNo;
    }

    public void setSpecNo(String specNo) {
        this.specNo = specNo;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public Long getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Long sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
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
    public ID identity() {
        return new ID().addKey("productId", productId).addKey("specNo", specNo);
    }

}
