package com.penglecode.codeforce.common.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 通用分页结果DTO
 *
 * @author pengpeng
 * @version 1.0.0
 */
@Schema(description="通用分页结果DTO")
public class Page implements BaseDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 当前页码
	 */
	@Schema(description="当前页码(默认1)", defaultValue="1", example="1")
	private int pageIndex = 1;

	/**
	 * 每页显示条数
	 */
	@Schema(description="每页显示条数(默认10)", defaultValue="10", example="10")
	private int pageSize = 10;

	/**
	 * 总记录数
	 */
	@Schema(description="总记录数(默认0)")
	private int totalRowCount;

	/**
	 * 总分页数
	 */
	@Schema(description="总分页数(默认0)")
	private int totalPageCount;

	/**
	 * 分页排序列表
	 */
	@Schema(description="分页排序列表")
	private List<OrderBy> orderBys = new ArrayList<>();
	
	protected Page() {
		super();
	}

	protected Page(int pageIndex, int pageSize) {
		super();
		if(pageIndex > 0){
			this.pageIndex = pageIndex;
		}
		if(pageSize > 0){
			this.pageSize = pageSize;
		}
	}

	protected Page(int pageIndex, int pageSize, List<OrderBy> orderBys) {
		this(pageIndex, pageSize);
		if(orderBys != null){
			this.orderBys = orderBys;
		}
	}

	protected Page(int pageIndex, int pageSize, int totalRowCount) {
		this(pageIndex, pageSize);
		this.setTotalRowCount(totalRowCount);
	}

	public static Page ofDefault() {
		return new Page();
	}

	public static Page of(int pageIndex, int pageSize) {
		return new Page(pageIndex, pageSize);
	}
	
	public static Page of(int pageIndex, int pageSize, int totalRowCount) {
		return new Page(pageIndex, pageSize, totalRowCount);
	}

	public static Page of(int pageIndex, int pageSize, OrderBy... orderBys) {
		return new Page(pageIndex, pageSize, Stream.of(orderBys).collect(Collectors.toList()));
	}

	public static Page of(int pageIndex, int pageSize, List<OrderBy> orderBys) {
		return new Page(pageIndex, pageSize, orderBys);
	}

	public static Page copyOf(Page page) {
		Page newPage = new Page();
		newPage.setPageIndex(page.getPageIndex());
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalRowCount(page.getTotalRowCount());
		newPage.setOrderBys(page.getOrderBys());
		return newPage;
	}
	
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

	public int getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
		calcTotalPageCount(); //计算totalPageCount
	}

	public List<OrderBy> getOrderBys() {
		return orderBys;
	}

	public void setOrderBys(List<OrderBy> orderBys) {
		this.orderBys = orderBys;
	}

	public void addOrder(OrderBy orderBy) {
		this.orderBys.add(orderBy);
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public int offset() {
		return (pageIndex - 1) * pageSize;
	}
	
	public int limit() {
		return getPageSize();
	}

	protected void calcTotalPageCount() {
		if(totalRowCount <= 0){
			totalPageCount = 0;
		}else{
			totalPageCount = totalRowCount % pageSize == 0 ? totalRowCount / pageSize : (totalRowCount / pageSize) + 1;
		}
	}

	public String toString() {
		return "{pageIndex=" + pageIndex + ", pageSize=" + pageSize
				+ ", totalRowCount=" + totalRowCount + ", totalPageCount="
				+ totalPageCount + ", orderBys=" + orderBys + "}";
	}

}
