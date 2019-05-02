package xin.xihc.utils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.Objects;

/**
 * 处理xml与javaBean转换<br>
 *
 * @author xihc
 * @version 1.0
 * @XmlRootElement 将一个Java类映射为一段XML的根节点。
 * @XmlAccessorType 定义映射这个类中的何种类型需要映射到XML。
 * @XmlElement 指定一个字段或get/set方法映射到XML的节点。
 * @XmlAttribute 指定一个字段或get/set方法映射到XML的属性。
 * @XmlTransient 定义某一字段或属性不需要被映射为XML。
 * @XmlType 定义映射的一些相关规则。
 * @XmlElementWrapper 为数组元素或集合元素定义一个父节点。
 * @XmlJavaTypeAdapter 自定义某一字段或属性映射到XML的适配器。
 * @XmlSchema 配置整个包的namespace
 * @date 2018年10月10日
 * @since 1.16
 */
public class XMLUtil {

	private static final Logger LOG = LoggerFactory.getLogger(XMLUtil.class);

	private XMLUtil() {
	}

	/**
	 * 以UTF-8字符编码转为xml字符串
	 *
	 * @param obj 将要转换的对象
	 * @return
	 */
	public static String convertToXml(Object obj) {
		return convertToXmlForEncode(obj, "UTF-8");
	}

	/**
	 * 以自定义编码转为xml字符串
	 *
	 * @param obj      将要转换的对象
	 * @param encoding 编码格式
	 * @return
	 */
	public static String convertToXmlForEncode(Object obj, String encoding) {
		if (null == obj) {
			return null;
		}
		String result = null;
		try {
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);

			try (StringWriter writer = new StringWriter()) {
				marshaller.marshal(obj, writer);
				result = writer.toString();
			}
		} catch (Exception e) {
			LOG.error("XMLUtil.convertToXmlForEncode()", e);
		}
		return result;
	}

	/**
	 * 将对象根据路径转换成xml文件
	 *
	 * @param obj      需要保存的对象
	 * @param file     需要保存的文件
	 * @param appended 是否追加
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static void saveToXmlFile(Object obj, File file, boolean appended) throws JAXBException, IOException {
		Objects.requireNonNull(obj, "保存的对象不能为空");

		// 利用jdk中自带的转换类实现
		JAXBContext context = JAXBContext.newInstance(obj.getClass());
		Marshaller marshaller = context.createMarshaller();
		// 格式化xml输出的格式
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		// 将对象转换成输出流形式的xml
		// 创建输出流
		try (FileWriter fw = new FileWriter(file, appended)) {
			marshaller.marshal(obj, fw);
		}
	}

	/**
	 * 将String类型的xml转换成对象
	 *
	 * @param xmlStr xml字符串
	 * @param clazz  需要转成的类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseToObject(String xmlStr, Class<T> clazz) {
		T xmlObject = null;
		try {
			xmlStr = escapingToXml(xmlStr);
			JAXBContext context = JAXBContext.newInstance(clazz);
			// 进行将Xml转成对象的核心接口
			Unmarshaller unmarshaller = context.createUnmarshaller();
			try (StringReader sr = new StringReader(xmlStr)) {
				xmlObject = (T) unmarshaller.unmarshal(sr);
			}
		} catch (JAXBException e) {
			LOG.error("XMLUtil.parseToObject()", e);
		}
		return xmlObject;
	}

	/**
	 * 将file类型的xml转换成对象
	 *
	 * @param xmlFile xml文件
	 * @param clazz   需要转成的类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseToObject(File xmlFile, Class<T> clazz) {
		T xmlObject = null;
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			try (FileReader fr = new FileReader(xmlFile)) {
				xmlObject = (T) unmarshaller.unmarshal(fr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (JAXBException e) {
			LOG.error("XMLUtil.parseToObject()", e);
		}
		return xmlObject;
	}

	/**
	 * 将xml字符转义
	 *
	 * @param xmlStr
	 * @return
	 */
	public static String xmlEscaping(String xmlStr) {
		return xmlStr.replaceAll("&", "&amp;")//
		             .replaceAll("<", "&lt;")//
		             .replaceAll(">", "&gt;")//
		             .replaceAll("\"", "&quot;")//
		             .replaceAll("'", "&apos;");
	}

	/**
	 * 逆解析
	 *
	 * @param xmlStr
	 * @return
	 */
	public static String escapingToXml(String xmlStr) {
		return xmlStr.replaceAll("&amp;", "&")//
		             .replaceAll("&lt;", "<")//
		             .replaceAll("&gt;", ">")//
		             .replaceAll("&quot;", "\"")//
		             .replaceAll("&apos;", "'");
	}

}
