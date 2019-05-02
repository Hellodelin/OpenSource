package xin.xihc.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xin.xihc.utils.common.CommonUtil;

/**
 * JsonUtil
 *
 * @author 席恒昌
 * @version 2.0
 * @Date 2017年6月13日 获取ObjectMapper对象
 */
public final class JsonUtil {

	private static Logger LOG = LoggerFactory.getLogger(JsonUtil.class);

	/**
	 * 普通的mapper
	 */
	private static ObjectMapper mapper = new ObjectMapper();
	/**
	 * 序列化时不包含null值的属性
	 */
	private static ObjectMapper nonNullMapper;

	private JsonUtil() {

	}

	static {
		// 设置时间格式
//		mapper.setDateFormat(new SimpleDateFormat(DateUtil.FORMAT_DATETIME_MS));
		// 允许没有双引号
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		// 设置忽略没有的字段
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		// 不包含Null属性
		nonNullMapper = mapper.copy();
		nonNullMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	/**
	 * 每次获取一个新的ObjectMapper,可以自定义解析配置
	 *
	 * @return
	 */
	public static ObjectMapper newObjectMapper() {
		return nonNullMapper.copy();
	}

	/**
	 * 将JsonString转为对象
	 *
	 * @param content
	 * @param valueType
	 * @return
	 */
	public static <T> T readValue(String content, Class<T> valueType) {
		if (CommonUtil.isNullEmpty(content)) {
			return null;
		}
		try {
			return mapper.readValue(content, valueType);
		} catch (Exception e) {
			LOG.error("JsonUtil.readValue()", e);
			return null;
		}
	}

	/**
	 * 将JsonString转为复杂嵌套的对象
	 *
	 * @param content
	 * @param valueTypeRef
	 * @return
	 */
	public static <T> T readValue(String content, TypeReference<T> valueTypeRef) {
		if (CommonUtil.isNullEmpty(content)) {
			return null;
		}
		try {
			return mapper.readValue(content, valueTypeRef);
		} catch (Exception e) {
			LOG.error("JsonUtil.readValue()", e);
			return null;
		}
	}

	/**
	 * 将对象转为另一个类型的对象
	 *
	 * @param obj
	 * @param valueType
	 * @return
	 */
	public static <T> T convertValue(Object obj, Class<T> valueType) {
		if (null == obj) {
			return null;
		}
		try {
			return mapper.convertValue(obj, valueType);
		} catch (Exception e) {
			LOG.error("JsonUtil.convertValue()", e);
			return null;
		}
	}

	/**
	 * 将对象转为另一个复杂嵌套类型的对象
	 *
	 * @param obj
	 * @param valueTypeRef
	 * @return
	 */
	public static <T> T convertValue(Object obj, TypeReference<T> valueTypeRef) {
		if (null == obj) {
			return null;
		}
		try {
			return mapper.convertValue(obj, valueTypeRef);
		} catch (Exception e) {
			LOG.error("JsonUtil.convertValue()", e);
			return null;
		}
	}

	/**
	 * 将对象转为JsonString
	 *
	 * @param obj
	 * @param pretty 是否美化、格式化json
	 * @return
	 */
	public static String toJsonString(Object obj, final boolean pretty) {
		if (null == obj) {
			return null;
		}
		try {
			String jsonStr = null;
			if (pretty) {
				jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
//				jsonStr = JsonFormat.format(jsonStr);
			} else {
				jsonStr = mapper.writeValueAsString(obj);
			}
			return jsonStr;
		} catch (Exception e) {
			LOG.error("JsonUtil.toJsonString()", e);
			return null;
		}
	}

	/**
	 * 将对象转为JsonString,不包含为null的字段
	 *
	 * @param obj
	 * @param pretty 是否美化、格式化json
	 * @return
	 * @since 1.18
	 */
	public static String toNoNullJsonStr(Object obj, final boolean pretty) {
		if (null == obj) {
			return null;
		}
		try {
			String jsonStr = null;
			if (pretty) {
				jsonStr = nonNullMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
//				jsonStr = JsonFormat.format(jsonStr);
			} else {
				jsonStr = nonNullMapper.writeValueAsString(obj);
			}
			return jsonStr;
		} catch (Exception e) {
			LOG.error("JsonUtil.toNoNullJsonStr()", e);
			return null;
		}
	}

}
