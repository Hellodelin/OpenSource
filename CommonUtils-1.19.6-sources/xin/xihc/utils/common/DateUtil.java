package xin.xihc.utils.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期、时间工具类
 *
 * @author 席恒昌
 * @date 2018年1月3日
 * @since 1.3
 */
public final class DateUtil {

	/**
	 * 日期格式yyyy-MM-dd
	 */
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	/**
	 * 日期纯数字格式yyyyMMdd
	 */
	public static final String FORMAT_DATE_NUM = "yyyyMMdd";
	/**
	 * 日期时间格式yyyy-MM-dd HH:mm:ss
	 */
	public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 日期时间纯数字格式yyyyMMddHHmmss
	 */
	public static final String FORMAT_DATETIME_NUM = "yyyyMMddHHmmss";
	/**
	 * 日期时间毫秒格式yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static final String FORMAT_DATETIME_MS = "yyyy-MM-dd HH:mm:ss.SSS";
	/**
	 * 日期时间毫秒纯数字格式yyyyMMddHHmmssSSS
	 */
	public static final String FORMAT_DATETIME_MS_NUM = "yyyyMMddHHmmssSSS";
	/**
	 * 时间格式HH:mm:ss
	 */
	public static final String FORMAT_TIME = "HH:mm:ss";
	/**
	 * 日时间纯数字格式HHmmss
	 */
	public static final String FORMAT_TIME_NUM = "HHmmss";

	/**
	 * 获取当前时间,格式yyyy-MM-dd HH:mm:ss
	 *
	 * @return
	 */
	public static String getNow() {
		SimpleDateFormat sf = new SimpleDateFormat(FORMAT_DATETIME);
		return sf.format(System.currentTimeMillis());
	}

	/**
	 * 获取当前时间,并格式化
	 *
	 * @return
	 */
	public static String getNow(String pattern) {
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		return sf.format(System.currentTimeMillis());
	}

	/**
	 * 得到多少秒之后的时间
	 *
	 * @param seconds
	 * @return 格式之后的结果
	 */
	public static String getDateTimeFromNow(int seconds, String formater) {
		SimpleDateFormat sf = new SimpleDateFormat(formater);
		Calendar cld = Calendar.getInstance();
		cld.add(Calendar.SECOND, seconds);
		return sf.format(cld.getTime());
	}

	/**
	 * 计算时间then之后seconds秒之后的时间
	 *
	 * @param then     时间
	 * @param seconds  秒数
	 * @param formater 格式
	 * @return
	 */
	public static String getDateTimeFromThen(String then, int seconds, String formater) {
		SimpleDateFormat sf = new SimpleDateFormat(formater);
		Calendar cld = Calendar.getInstance();
		String ret = null;
		try {
			Date temp = sf.parse(then);
			cld.setTime(temp);
			cld.add(Calendar.SECOND, seconds);
			ret = sf.format(cld.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			ret = null;
		}
		return ret;
	}

	/**
	 * 时间格式化
	 *
	 * @param date     日期时间
	 * @param formater 格式
	 * @return
	 */
	public static String formatDateTime(Date date, String formater) {
		SimpleDateFormat sf = new SimpleDateFormat(formater);
		return sf.format(date);
	}

	/**
	 * 字符串时间转为Date日期
	 *
	 * @param dateTime
	 * @param formater
	 * @return
	 */
	public static Date toDate(String dateTime, String formater) {
		SimpleDateFormat sf = new SimpleDateFormat(formater);
		Date ret = null;
		try {
			ret = sf.parse(dateTime);
		} catch (ParseException e) {
			return ret;
		}
		return ret;
	}

	/**
	 * 字符串时间转为时间戳
	 *
	 * @param dateTime
	 * @param formater
	 * @return
	 */
	public static long toTimeStamp(String dateTime, String formater) {
		SimpleDateFormat sf = new SimpleDateFormat(formater);
		Date ret = null;
		try {
			ret = sf.parse(dateTime);
		} catch (ParseException e) {
			return 0L;
		}
		return ret.getTime();
	}

	/**
	 * 判断今天是否在某个时间段内
	 *
	 * @param start    起始时间
	 * @param end      截止时间
	 * @param formater 时间格式
	 * @return -2-起始日期大于截止日期; -1-格式不对; 0-开始时间之前; 1-时间之间 ; 2-结束时间之后
	 */
	public static int todayBetween(String start, String end, String formater) {
		if (null == formater || "".equals(formater)) {
			return -1;
		}
		Long startDate = 0L;
		Long endDate = Long.MAX_VALUE;
		Long today = new Long(getNow(formater).replaceAll("[-: /]", ""));
		try {
			// 日期2017-06-06 00:00:00-> 20170606000000
			if (start != null && !"".equals(start)) {
				if (start.length() == formater.length()) {
					startDate = new Long(start.replaceAll("[-: /]", ""));
				} else {
					return -1; // 格式不对
				}
			}
			if (end != null && !"".equals(end)) {
				if (end.length() == formater.length()) {
					endDate = new Long(end.replaceAll("[-: /]", ""));
				} else {
					return -1; // 格式不对
				}
			}
		} catch (Exception e) {
			return -1;
		}
		if (startDate > endDate) { // 起始日期大于截止日期
			return -2;
		} else if (today < startDate) { // 今天在开始日期之前
			return 0;
		} else if (today > endDate) { // 今天在结束日期之后
			return 2;
		} else { // 今天在开始结束之间
			return 1;
		}
	}

	/**
	 * 判断今天是否在某个时间段内
	 *
	 * @param start    起始时间
	 * @param end      截止时间
	 * @param formater 时间格式
	 * @return -2-起始日期大于截止日期; -1-格式不对; 0-开始时间之前; 1-时间之间 ; 2-结束时间之后
	 */
	public static int todayBetween(Date start, Date end, String formater) {
		if (null == formater || "".equals(formater)) {
			return -1;
		}
		Long startDate = 0L;
		Long endDate = Long.MAX_VALUE;
		Long today = new Long(getNow(formater).replaceAll("[-: /]", ""));
		try {
			// 日期2017-06-06 00:00:00-> 20170606000000
			if (start != null) {
				startDate = new Long(formatDateTime(start, formater).replaceAll("[-: /]", ""));
			}
			if (end != null) {
				endDate = new Long(formatDateTime(end, formater).replaceAll("[-: /]", ""));
			}
		} catch (Exception e) {
			return -1;
		}
		if (startDate > endDate) { // 起始日期大于截止日期
			return -2;
		} else if (today < startDate) { // 今天在开始日期之前
			return 0;
		} else if (today > endDate) { // 今天在结束日期之后
			return 2;
		} else { // 今天在开始结束之间
			return 1;
		}
	}

	/**
	 * 判断day是否在某个时间段内
	 *
	 * @param start    起始时间
	 * @param end      截止时间
	 * @param formater 时间格式
	 * @return -2-起始日期大于截止日期; -1-格式不对; 0-开始时间之前; 1-时间之间 ; 2-结束时间之后
	 */
	public static int dayBetween(String day, String start, String end, String formater) {
		if (null == formater || "".equals(formater)) {
			return -1;
		}
		Long startDate = 0L;
		Long endDate = Long.MAX_VALUE;
		Long then = 1L;
		try {
			// 日期2017-06-06 00:00:00-> 20170606000000
			if (day != null && !"".equals(day)) {
				if (day.length() == formater.length()) {
					then = new Long(day.replaceAll("[-: /]", ""));
				} else {
					return -1; // 格式不对
				}
			}
			if (start != null && !"".equals(start)) {
				if (start.length() == formater.length()) {
					startDate = new Long(start.replaceAll("[-: /]", ""));
				} else {
					return -1; // 格式不对
				}
			}
			if (end != null && !"".equals(end)) {
				if (end.length() == formater.length()) {
					endDate = new Long(end.replaceAll("[-: /]", ""));
				} else {
					return -1; // 格式不对
				}
			}
		} catch (Exception e) {
			return -1;
		}
		if (startDate > endDate) { // 起始日期大于截止日期
			return -2;
		} else if (then < startDate) { // day在开始日期之前
			return 0;
		} else if (then > endDate) { // day在结束日期之后
			return 2;
		} else { // day在开始结束之间
			return 1;
		}
	}

	/**
	 * 判断day是否在某个时间段内
	 *
	 * @param start 起始时间
	 * @param end   截止时间
	 * @return -2-起始日期大于截止日期; -1-格式不对; 0-开始时间之前; 1-时间之间 ; 2-结束时间之后
	 */
	public static int dayBetween(Date day, Date start, Date end) {
		Long startDate = 0L;
		Long endDate = Long.MAX_VALUE;
		Long then = 1L;
		try {
			if (day != null) {
				then = day.getTime();
			}
			if (start != null) {
				startDate = start.getTime();
			}
			if (end != null) {
				endDate = end.getTime();
			}
		} catch (Exception e) {
			return -1;
		}
		if (startDate > endDate) { // 起始日期大于截止日期
			return -2;
		} else if (then < startDate) { // day在开始日期之前
			return 0;
		} else if (then > endDate) { // day在结束日期之后
			return 2;
		} else { // day在开始结束之间
			return 1;
		}
	}

	/**
	 * 获取该时间戳对应的00:00:00.000的时间戳
	 *
	 * @param timeStamp 需大于0
	 * @param delta     天数，0为当天的
	 * @return
	 * @since 1.12
	 */
	public static long getZeroTimestamp(long timeStamp, int delta) {
		if (timeStamp < 0) {
			timeStamp = System.currentTimeMillis();
		}
		long zero = timeStamp / (1000 * 3600 * 24L) * (1000 * 3600 * 24L) - Long
				.valueOf(TimeZone.getDefault().getRawOffset());
		return zero + (1000 * 3600 * 24L) * delta;
	}

	/**
	 * 获取半夜23:59:59.999的毫秒数
	 *
	 * @param delta 天数，0为当天的
	 * @return
	 */
	public static long getMidOfNightTime(int delta) {
		return getMidOfNightTime(System.currentTimeMillis(), delta);
	}

	/**
	 * 获取半夜23:59:59.999的毫秒数
	 *
	 * @param timeStamp 时间戳
	 * @param delta     天数，0为当天的
	 * @return
	 */
	public static long getMidOfNightTime(long timeStamp, int delta) {
		++delta;
		long zeroTimestamp = getZeroTimestamp(timeStamp, delta);
		return zeroTimestamp - 1L;
	}

}
