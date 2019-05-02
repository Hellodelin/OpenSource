package xin.xihc.utils.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.MessageDigest;
import java.util.*;

/**
 * 通用工具类
 *
 * @author 席恒昌
 */
public final class CommonUtil {

	public static char HEXDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	/**
	 * 获取uuid
	 *
	 * @param isUpp 是否转大写
	 * @return GUID
	 */
	public static String newGuid(boolean isUpp) {
		String res = UUID.randomUUID().toString().replaceAll("-", "");
		return isUpp ? res.toUpperCase() : res;
	}

	/**
	 * 将string进行MD5
	 *
	 * @param cont  内容
	 * @param isUpp 是否转大写
	 * @return MD5摘要
	 */
	public static String md5Str(String cont, boolean isUpp) {
		try {
			byte[] strTemp = cont.getBytes(CharsetUtil.UTF8);
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = HEXDigits[byte0 >>> 4 & 0xf];
				str[k++] = HEXDigits[byte0 & 0xf];
			}
			return isUpp ? new String(str).toUpperCase() : new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 判断对象是否为空
	 *
	 * @param obj
	 * @return true-对象obj为空
	 */
	public final static boolean isNullEmpty(Object obj) {
		if (null == obj) {
			return true;
		} else {
			if ((obj instanceof String)) {
				return "".equals(((String) obj).trim());
			}
			if (obj.getClass().isArray()) {
				return ((Object[]) obj).length < 1;
			}
			if ((obj instanceof List)) {
				return ((List<?>) obj).size() < 1;
			}
		}
		return false;
	}

	/**
	 * 判断对象是否不为空
	 *
	 * @param obj
	 * @return
	 */
	public final static boolean isNotNullEmpty(Object obj) {
		return !isNullEmpty(obj);
	}

	/**
	 * 获取该对象的所有字段(包含父类的)
	 *
	 * @param clazz
	 * @return
	 */
	public static List<Field> getAllFields(Class<?> clazz, boolean containFinal, boolean containStatic) {
		List<Field> res = new ArrayList<>(10);
		if (null == clazz) {
			return res;
		}
		ArrayList<Field> temp = null;
		while (!clazz.equals(Object.class)) {
			temp = new ArrayList<>(10);
			for (Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				if (!containStatic && Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				if (!containFinal && Modifier.isFinal(field.getModifiers())) {
					continue;
				}
				temp.add(field);
			}
			res.addAll(0, temp);
			clazz = clazz.getSuperclass();
		}
		return res;
	}

	/**
	 * 将对象转为hashMap,暂不支持嵌套对象
	 *
	 * @param obj
	 * @param containsNull 是否包含null值的
	 * @return
	 */
	public static Map<String, Object> objToMap(Object obj, boolean containsNull) {
		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Object val;
		for (Field field : getAllFields(obj.getClass(), true, true)) {
			field.setAccessible(true);
			try {
				val = field.get(obj);
				if (containsNull) {
					map.put(field.getName(), val);
				} else if (null != val) {
					map.put(field.getName(), val);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

}
