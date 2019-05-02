/**
 *
 */
package xin.xihc.utils.json;

/**
 * JSON格式化输入工具类
 *
 * @author 席恒昌
 * @date 2018年1月5日
 * @since
 */
public final class JsonFormat {

	/**
	 * 格式化
	 *
	 * @param jsonStr
	 * @return
	 */
	public static String format(String jsonStr) {
		if (null == jsonStr || "".equals(jsonStr))
			return "";
		StringBuilder sb = new StringBuilder();
		char last = '\0';
		char current = '\0';
		int indent = 0;
		boolean isInQuotationMarks = false;
		for (int i = 0; i < jsonStr.length(); i++) {
			last = current;
			current = jsonStr.charAt(i);
			switch (current) {
				case '"':
					if (last != '\\') {
						isInQuotationMarks = !isInQuotationMarks;
					}
					sb.append(current);
					break;
				case '{':
				case '[':
					sb.append(current);
					if (!isInQuotationMarks) {
						sb.append('\n');
						indent++;
						addIndentBlank(sb, indent);
					}
					break;
				case '}':
				case ']':
					if (!isInQuotationMarks) {
						sb.append('\n');
						indent--;
						addIndentBlank(sb, indent);
					}
					sb.append(current);
					break;
				case ',':
					sb.append(current);
					if (last != '\\' && !isInQuotationMarks) {
						sb.append('\n');
						addIndentBlank(sb, indent);
					}
					break;
				default:
					// 不在引号内时，替换掉所有的空白字符
					if (!isInQuotationMarks) {
						String ss = String.valueOf(current).replaceAll("\\s", "");
						sb.append(ss);
					} else {
						sb.append(current);
					}
			}
		}

		return sb.toString();
	}

	/**
	 * 添加space
	 *
	 * @param sb
	 * @param indent
	 */
	private static void addIndentBlank(StringBuilder sb, int indent) {
		for (int i = 0; i < indent; i++) {
			sb.append('\t');
		}
	}
}
