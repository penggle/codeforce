package com.penglecode.codeforce.common.model;

import com.penglecode.codeforce.common.support.BeanIntrospector;
import com.penglecode.codeforce.common.support.SerializableFunction;
import com.penglecode.codeforce.common.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;

/**
 * 通用排序DTO
 *
 * @author pengpeng
 * @version 1.0.0
 */
@Schema(description="通用排序DTO")
public class OrderBy implements BaseDTO {

	private static final long serialVersionUID = 1L;

	/** 排序字段名 */
	@NotBlank(message="排序字段名(property)不能为空!")
	@Schema(description="排序字段名")
	private String property;

	/** 排序顺序：asc|desc */
	@NotBlank(message="排序顺序(direction)不能为空!")
	@Schema(description="排序顺序(asc|desc)", defaultValue="asc")
	private String direction;

	protected OrderBy() {
		super();
	}

	protected OrderBy(String property, String direction) {
		super();
		if(StringUtils.isNotBlank(direction)) {
			direction = Direction.DESC.toString().equalsIgnoreCase(direction) ? Direction.DESC.toString() : Direction.ASC.toString();
		} else {
			direction = Direction.ASC.toString();
		}
		this.property = property;
		this.direction = direction;
	}

	public static OrderBy by(String property, Direction direction) {
		return new OrderBy(property, direction.toString());
	}

	public static OrderBy asc(String property) {
		return new OrderBy(property, Direction.ASC.toString());
	}

	public static OrderBy desc(String property) {
		return new OrderBy(property, Direction.DESC.toString());
	}

	public static <T,R> OrderBy by(SerializableFunction<T,R> getterReference, String order) {
		Field field = BeanIntrospector.introspectField(getterReference);
		return new OrderBy(field.getName(), order);
	}

	public static <T,R> OrderBy asc(SerializableFunction<T,R> getterReference) {
		Field field = BeanIntrospector.introspectField(getterReference);
		return new OrderBy(field.getName(), Direction.ASC.toString());
	}

	public static <T,R> OrderBy desc(SerializableFunction<T,R> getterReference) {
		Field field = BeanIntrospector.introspectField(getterReference);
		return new OrderBy(field.getName(), Direction.DESC.toString());
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	/**
	 * Used by SpringMVC @RequestParam and JAX-RS @QueryParam
	 * @param order
	 * @return
	 */
	public static OrderBy valueOf(String order) {
		if(order != null && !order.isEmpty()) {
			String[] orderBys = order.trim().split(":");
			String prop = orderBys[0] == null ? null : orderBys[0].trim();
			String dir = null;
			if(orderBys.length == 2) {
				dir = orderBys[1] == null ? null : orderBys[1].trim();
			}
			if(prop != null && prop.length() > 0) {
				return new OrderBy(prop, dir);
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return property + ":" + direction;
	}

	public enum Direction {

		ASC, DESC
	}

}