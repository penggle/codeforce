package com.penglecode.codeforce.common.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用分页请求DTO
 *
 * @author peng2.peng
 * @version 1.0.0
 */
@Schema(description="通用分页请求DTO")
public class PageRequest<T> extends BaseRequest {

    /**
     * 当前页码
     */
    @Min(value=1, message="当前页码(pageIndex)不能小于1")
    @Schema(description="当前页码(默认1)", defaultValue="1", example="1")
    private int pageIndex = 1;

    /**
     * 每页显示条数
     */
    @Min(value=1, message="每页显示条数(pageSize)不能小于1")
    @Schema(description="每页显示条数(默认10)", defaultValue="10", example="10")
    private int pageSize = 10;

    /**
     * 分页排序列表
     */
    @Valid
    @Schema(description="分页排序列表")
    private List<OrderBy> orderBys = new ArrayList<>();

    /**
     * 查询参数
     */
    @Valid
    @NotNull(message="查询参数不能为空!")
    @Schema(description="查询参数")
    private T data;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<OrderBy> getOrderBys() {
        return orderBys;
    }

    public void setOrderBys(List<OrderBy> orderBys) {
        this.orderBys = orderBys;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{pageIndex=" + pageIndex + ", pageSize=" + pageSize + ", orderBys=" + orderBys + ", data=" + data + "}";
    }

}
