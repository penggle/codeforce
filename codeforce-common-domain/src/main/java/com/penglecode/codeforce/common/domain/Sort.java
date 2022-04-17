package com.penglecode.codeforce.common.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通用排序对象
 *
 * @author pengpeng
 * @version 1.0
 */
public class Sort implements DomainObject {

	private static final long serialVersionUID = 1L;

	/** 排序列表 */
	private List<OrderBy> orderBys = new ArrayList<>();

	protected Sort() {
		super();
	}

	protected Sort(List<OrderBy> orderBys) {
		super();
		if(orderBys != null) {
			this.orderBys = orderBys;
		}
	}
	
	public static Sort orderBy(OrderBy... orderBys) {
		return new Sort(Arrays.asList(orderBys));
	}

	public static Sort orderBy(List<OrderBy> orderBys) {
		return new Sort(orderBys);
	}

	public boolean addOrderBy(OrderBy orderBy) {
		return orderBys.add(orderBy);
	}

	public List<OrderBy> getOrderBys() {
		return orderBys;
	}

	public void setOrderBys(List<OrderBy> orderBys) {
		this.orderBys = orderBys;
	}

	@Override
	public String toString() {
		return "Sort " + orderBys + "";
	}

}
