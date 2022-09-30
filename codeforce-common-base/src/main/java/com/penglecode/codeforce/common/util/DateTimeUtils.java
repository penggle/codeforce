package com.penglecode.codeforce.common.util;

import org.springframework.util.Assert;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

/**
 * 基于JDK8时间框架的日期时间处理工具类
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class DateTimeUtils {

	/**
	 * 默认的日期格式: yyyy-MM-dd
	 */
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	/**
	 * 默认的日期格式: yyyy-MM-dd
	 */
	public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

	/**
	 * 默认的日期+时间格式: yyyy-MM-dd HH:mm:ss
	 */
	public static final String DEFAULT_DATETIME_PATTERN = DEFAULT_DATE_PATTERN + " " + DEFAULT_TIME_PATTERN;

	/**
	 * 默认的JAVA8日期DateTimeFormatter
	 */
	public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);

	/**
	 * 默认的JAVA8时间DateTimeFormatter
	 */
	public static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN);

	/**
	 * 默认的JAVA8日期时间DateTimeFormatter
	 */
	public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN);

	/**
	 * 多种格式兼容的日期DateTimeFormatter
	 * 兼容：1995-08-24, 1995/08/24
	 */
	public static final DateTimeFormatter COMPATIBLE_DATE_FORMATTER = new DateTimeFormatterBuilder()
			.appendPattern("[yyyy-MM-dd][yyyy/MM/dd]")
			.toFormatter();

	/**
	 * 多种格式兼容的日期时间DateTimeFormatter
	 * 兼容：1995-08-24 12:34:56，1995-08-24 12:34:56.123，1995-08-24T12:34:56，1995-08-24T12:34:56.123
	 *      1995/08/24 12:34:56，1995/08/24 12:34:56.123，1995/08/24T12:34:56，1995/08/24T12:34:56.123
	 */
	public static final DateTimeFormatter COMPATIBLE_DATETIME_FORMATTER = new DateTimeFormatterBuilder()
			.appendPattern("[yyyy-MM-dd HH:mm:ss[.SSS]]")
			.appendPattern("[yyyy-MM-dd'T'HH:mm:ss[.SSS]]")
			.appendPattern("[yyyy/MM/dd HH:mm:ss[.SSS]]")
			.appendPattern("[yyyy/MM/dd'T'HH:mm:ss[.SSS]]")
			.toFormatter();

	
	private DateTimeUtils() {}

	/**
	 * <p>将@{code java.util.Date}转换为@{code java.time.LocalDateTime}
	 * 
	 * @param date
	 * @return
	 */
	public static LocalDateTime from(Date date){
		checkDate(date);
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
	
	/**
	 * <p>将@{code java.util.Date}转换为@{code java.time.LocalDateTime}
	 * 
	 * @param dateTime
	 * @return
	 */
	public static Date from(LocalDateTime dateTime){
		checkDateTime(dateTime);
		return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	/**
	 * <p>将@{code java.time.LocalDateTime}以指定的日期格式格式化为字符串</p>
	 * 
	 * @param dateTime
	 * @param pattern
	 * @return
	 */
	public static String format(LocalDateTime dateTime, String pattern){
		checkDateTime(dateTime);
		checkPattern(pattern);
		return dateTime.format(DateTimeFormatter.ofPattern(pattern));
	}
	
	/**
	 * <p>将@{code java.util.Date}以指定的日期格式格式化为字符串</p>
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern){
		checkDate(date);
		checkPattern(pattern);
		return from(date).format(DateTimeFormatter.ofPattern(pattern));
	}
	
	/**
	 * <p>以指定的日期格式格式化当前时间</p>
	 * 
	 * @param pattern
	 * @return
	 */
	public static String formatNow(String pattern){
		checkPattern(pattern);
		return LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(pattern));
	}
	
	/**
	 * <p>以默认的日期格式(yyyy-MM-dd HH:mm:ss)格式化当前时间</p>
	 * 
	 * @return
	 */
	public static String formatNow(){
		return formatNow(DEFAULT_DATETIME_PATTERN);
	}

	/**
	 * <p>将字符串格式的日期转换为@{java.time.LocalDateTime}</p>
	 * 
	 * @param dateTime		- 日期字符串形式的值
	 * @param pattern		- 针对dateTimeText的日期格式
	 * @return
	 */
	public static LocalDateTime parse2DateTime(String dateTime, String pattern){
		Assert.hasText(dateTime, "Parameter 'dateTime' can not be empty!");
		checkPattern(pattern);
		return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * <p>将字符串格式的日期转换为@{java.time.LocalDateTime}</p>
	 * 兼容多种常见日期时间格式
	 *
	 * @param dateTime		- 日期字符串形式的值
	 * @return
	 */
	public static LocalDateTime parse2DateTime(String dateTime){
		Assert.hasText(dateTime, "Parameter 'dateTime' can not be empty!");
		return LocalDateTime.parse(dateTime, COMPATIBLE_DATETIME_FORMATTER);
	}

	/**
	 * <p>将字符串格式的日期转换为@{code java.util.Date}</p>
	 *
	 * @param dateTime			- 日期字符串形式的值
	 * @return
	 */
	public static Date parse2Date(String dateTime){
		return from(parse2DateTime(dateTime));
	}

	/**
	 * <p>将字符串格式的日期转换为@{code java.util.Date}</p>
	 * 
	 * @param dateTime			- 日期字符串形式的值
	 * @param pattern			- 针对dateTimeText的日期格式
	 * @return
	 */
	public static Date parse2Date(String dateTime, String pattern){
		return from(parse2DateTime(dateTime, pattern));
	}
	
	/**
	 * 检测dateTime的日期格式是否是pattern
	 * @param dateTime
	 * @param pattern
	 * @return
	 */
	public static boolean matchesDatePattern(String dateTime, String pattern) {
		if(dateTime != null){
			try {
				LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 标准化dateTimeText，将其他格式的日期格式统一标准化为yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss
	 * @param dateTime
	 * @return
	 */
	public static String normalizeDateTime(String dateTime) {
		if(dateTime != null) {
			dateTime = dateTime.replace("T", " ");
			dateTime = dateTime.replace("/", "-");
			dateTime = dateTime.replace("年", "-");
			dateTime = dateTime.replace("月", "-");
			dateTime = dateTime.replace("日", "");
			dateTime = dateTime.replace("时", ":");
			dateTime = dateTime.replace("分", ":");
			dateTime = dateTime.replace("秒", "");
		}
		return dateTime;
	}
	
	/**
	 * 毫秒时间戳转LocalDateTime
	 * @param timestamp
	 * @return
	 */
	public static LocalDateTime ofEpochMilli(long timestamp) {
	    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
	}
	
	/**
	 * LocalDateTime转毫秒时间戳
	 * @param dateTime
	 * @return
	 */
	public static Long toEpochMilli(LocalDateTime dateTime) {
		return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	private static void checkDate(Date date) {
		Assert.notNull(date, "Parameter 'date' can not be null!");
	}

	private static void checkDateTime(LocalDateTime dateTime) {
		Assert.notNull(dateTime, "Parameter 'dateTime' can not be null!");
	}

	private static void checkPattern(String pattern) {
		Assert.hasText(pattern, "Parameter 'pattern' can not be empty!");
	}

}