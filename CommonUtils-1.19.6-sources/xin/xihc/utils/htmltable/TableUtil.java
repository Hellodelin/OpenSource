package xin.xihc.utils.htmltable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xin.xihc.utils.common.CommonUtil;

/**
 * 报表表格生成工具, 步骤：1、先扫描左边在外层，2、数据从下往上添加小计合计行；3、进行小计求和运算；4、进行行合并
 * 
 * @author 席恒昌
 * @date 2018年1月17日
 * @version 1.6
 * @since 1.6
 */
public class TableUtil {

	/**
	 * 将table对象转换成html-table
	 * 
	 * @param table
	 * @return
	 */
	public static String convertToHtmlTable(Table table) {
		if (null == table) {
			return null;
		}
		long i = System.currentTimeMillis();
		addSumRows(table);
		long j = System.currentTimeMillis();
		System.err.println("扫描添加小计耗时：" + (j - i) + "ms");

		computedAndFormat(table);
		long k = System.currentTimeMillis();
		System.err.println("计算以及格式化耗时：" + (k - j) + "ms");
		mergeRow(table);
		long l = System.currentTimeMillis();
		System.err.println("合并单元格耗时：" + (l - k) + "ms");
		
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("<table border='").append(table.getBorder() + "' cellpadding='")
					.append(table.getCellPadding() + "' cellspacing='").append(table.getCellSpacing() + "'");
			sb.append(" style='" + table.getStyle() + "'>");
			// 表头
			for (Row r : table.getHeader()) {
				sb.append("<tr");
				if (CommonUtil.isNotNullEmpty(r.getStyle())) {
					sb.append(" style='" + r.getStyle() + "'");
				}
				sb.append(">");
				for (Cell c : r.getCells()) {
					sb.append("<th");
					if (CommonUtil.isNotNullEmpty(c.getStyle())) {
						sb.append(" style='" + c.getStyle() + "'");
					}
					if (c.getRowSpan() > 1) {
						sb.append(" rowspan='" + c.getRowSpan() + "'");
					}
					if (c.getColSpan() > 1) {
						sb.append(" colspan='" + c.getColSpan() + "'");
					}
					sb.append(">");
					sb.append(c.getValue());
					sb.append("</th>");
				}
				sb.append("</tr>");
			}
			// 表格数据
			for (Row r : table.getData()) {
				sb.append("<tr");
				if (CommonUtil.isNotNullEmpty(r.getStyle())) {
					sb.append(" style='" + r.getStyle() + "'");
				}
				sb.append(">");
				for (Cell c : r.getCells()) {
					sb.append("<td");
					if (CommonUtil.isNotNullEmpty(c.getStyle())) {
						sb.append(" style='" + c.getStyle() + "'");
					}
					if (c.getRowSpan() > 1) {
						sb.append(" rowspan='" + c.getRowSpan() + "'");
					}
					if (c.getColSpan() > 1) {
						sb.append(" colspan='" + c.getColSpan() + "'");
					}
					sb.append(">");
					sb.append(c.getValue());
					sb.append("</td>");
				}
				sb.append("</tr>");
			}

			sb.append("</table>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 扫描所有列，在合适的位置添加小计
	 * 
	 * @param table
	 * @param colIndex
	 * @param rowStartIndex
	 * @param rowEndIndex
	 */
	private static void scanColumn(Table table, int colIndex, int rowStartIndex, int rowEndIndex) {
		if (!table.getIsSubTotal()) {
			return;
		}
		if (rowEndIndex < rowStartIndex || table.getData().size() - 1 < rowEndIndex) {
			return;
		}
		int colCount = table.getColumnCount();
		if (colIndex + 1 > colCount) {
			return;
		}
		if (!isAllowRowSpan(table, colIndex)) {
			return;
		}
		String temp = "";

		int iStart = 0;
		for (int i = rowEndIndex; i >= rowStartIndex; i--) {
			Row row = table.getData().get(i);
			String value = row.getCells().get(colIndex).getValue();
			if (CommonUtil.isNullEmpty(value)) {
				value = "";
			} else {
				value = value.replaceAll("<[^>]+>", "").trim();
			}
			if (!temp.equals(value)) {
				if (i == rowEndIndex) {
					Row row1 = new Row();
					row1.setStyle(table.getSubTotalStyle());
					row1.setType(1);
					row1.setTypeColIndex(colIndex);
					for (int k = 0; k < colIndex; k++) {
						row1.getCells().add(row.getCells().get(k));
					}
					Cell c = Cell.newInstance().setValue("小计");
					row1.getCells().add(c);
					for (int k = colIndex + 1; k < colCount; k++) {
						c = Cell.newInstance().setValue("");
						row1.getCells().add(c);
					}
					table.insertData(i + 1, row1);
				} else if (i < rowEndIndex) {
					scanColumn(table, colIndex + 1, i + 1, iStart);

					Row row1 = new Row();
					row1.setStyle(table.getSubTotalStyle());
					row1.setType(1);
					row1.setTypeColIndex(colIndex);
					for (int k = 0; k < colIndex; k++) {
						row1.getCells().add(row.getCells().get(k));
					}
					Cell c = Cell.newInstance().setValue("小计");
					row1.getCells().add(c);
					for (int k = colIndex + 1; k < colCount; k++) {
						c = Cell.newInstance().setValue("");
						row1.getCells().add(c);
					}
					table.insertData(i + 1, row1);
				}
				temp = value;
				iStart = i;
			}
			if (i == rowStartIndex) {
				scanColumn(table, colIndex + 1, i, iStart);
			}
		}
	}

	/**
	 * 1、计算合并行数、添加小计行
	 * 
	 * @param table
	 * @return
	 */
	private static void addSumRows(Table table) {
		int colCount = table.getColumnCount();
		int rowCount = table.getData().size();
		if (rowCount < 1) {
			return;
		}
		String temp = "";
		// 1、扫描最左边列
		ArrayList<Integer> indexList = new ArrayList<>(6);
		if (isAllowRowSpan(table, 0)) {
			for (int i = 0; i < rowCount; i++) {
				String value = table.getData().get(i).getCells().get(0).getValue();
				if (CommonUtil.isNullEmpty(value)) {
					value = "";
				} else {
					value = value.replaceAll("<[^>]+>", "").trim();
				}
				if (!temp.equals(value)) {
					temp = value;
					indexList.add(i);
				}
			}
			indexList.add(rowCount);
		} else {
			return;
		}
		for (int i = indexList.size() - 1; i > 0; i--) {
			scanColumn(table, 0, indexList.get(i - 1), indexList.get(i) - 1);
		}
		// 重新获取
		rowCount = table.getData().size();
		int iSt = 0;
		for (int i = colCount - 1; i >= 0; i--) {
			if (!isAllowRowSpan(table, i)) {
				continue;
			}
			temp = table.getData().get(0).getCells().get(i).getValue();
			if (CommonUtil.isNullEmpty(temp)) {
				temp = "";
			} else {
				temp = temp.replaceAll("<[^>]+>", "").trim();
			}
			iSt = 0;
			for (int j = 0; j < rowCount; j++) {
				String value = table.getData().get(j).getCells().get(i).getValue();
				if (CommonUtil.isNullEmpty(value)) {
					value = "";
				} else {
					value = value.replaceAll("<[^>]+>", "").trim();
				}
				if (temp.equals(value) && !"".equals(value)) {
					table.getData().get(iSt).getCells().get(i).setRowSpan(j - iSt + 1);
				} else {
					temp = value;
					iSt = j;
				}
			}
		}
	}

	/**
	 * 2、计算小计、合计，并格式化数值
	 * 
	 * @param table
	 */
	private static void computedAndFormat(Table table) {
		int colCount = table.getColumnCount();
		int rowCount = table.getData().size();

		if (table.getIsRowTotal()) {
			if (table.getHeader().size() == 1) {
				Cell e = Cell.newInstance();
				e.setValue("合计");
				e.setStyle(table.getRowTotalStyle());
				table.getHeader().get(0).getCells().add(e);
			} else if (table.getHeader().get(0).getCells().get(0).getColSpan() == table.getColumnCount()) {
				table.getHeader().get(0).getCells().get(0).setColSpan(colCount + 1);
				for (int k = 1; k < table.getHeader().size(); k++) {
					Cell e = Cell.newInstance();
					e.setValue("合计");
					e.setStyle(table.getRowTotalStyle());
					table.getHeader().get(k).getCells().add(e);
				}
			}
		}

		if (table.getIsTotal()) {
			Row row1 = new Row();
			row1.setStyle(table.getTotalStyle());
			row1.setType(2);
			row1.setTypeColIndex(0);
			Cell c = Cell.newInstance().setValue("合计");
			row1.getCells().add(c);
			for (int k = 1; k < colCount; k++) {
				c = Cell.newInstance().setValue("");
				row1.getCells().add(c);
			}
			table.insertData(rowCount, row1);
		}
		// 行总金额
		Map<String, Double> total = new HashMap<>(colCount * 2 + rowCount);
		// 重新获取行数
		rowCount = table.getData().size();
		// 初始化行统计
		for (int r = 0; r < rowCount; r++) {
			if (!total.containsKey("row" + r)) {
				total.put("row" + r, 0.0);
			}
		}
		for (int c = 0; c < colCount; c++) {
			if (!isSum(table, c)) {// 不求和的列直接跳过
				continue;
			}
			// 初始化小计
			for (int i = 0; i <= c; i++) {
				total.put("col_sub" + i, 0.0);
			}
			for (int r = 0; r < rowCount; r++) {
				Row row = table.getData().get(r);
				if (!total.containsKey("col" + c)) {
					total.put("col" + c, 0.0);
				}
				String value = row.getCells().get(c).getValue();
				if (CommonUtil.isNullEmpty(value)) {
					value = "0";
				} else {
					value = value.replaceAll("<[^>]+>", "").trim();
				}
				double val = 0.0;
				try {
					val = Double.valueOf(value);
				} catch (Exception e) {
					// e.printStackTrace();
					val = 0.0;
				}
				switch (row.getType()) {
				case 0:
					total.put("row" + r, val + total.get("row" + r));
					total.put("col" + c, val + total.get("col" + c));
					for (int i = 0; i <= c; i++) {
						total.put("col_sub" + i, val + total.get("col_sub" + i));
					}
					// 格式化原始数值
					row.getCells().get(c).setValue(formatNum(table, val));
					break;
				case 1:
					// 小计行设置新值
					row.getCells().get(c).setValue(formatNum(table, total.get("col_sub" + row.getTypeColIndex())));
					total.put("row" + r, total.get("col_sub" + row.getTypeColIndex()) + total.get("row" + r));
					total.put("col_sub" + row.getTypeColIndex(), 0.0);
					break;
				case 2: // 合计
					total.put("row" + r, total.get("col" + c) + total.get("row" + r));
					row.getCells().get(c).setValue(formatNum(table, total.get("col" + c)));
					break;

				default:
					break;
				}
			}
		}
		// 循环完所有列之后，是否添加行合计
		if (table.getIsRowTotal()) {
			for (int r = 0; r < rowCount; r++) {
				Row row = table.getData().get(r);
				Cell e = Cell.newInstance().setValue(formatNum(table, total.get("row" + r)));
				row.getCells().add(e);
			}
		}
	}

	/**
	 * 3、同列合并相同行
	 * 
	 * @param table
	 * @return
	 */
	private static Table mergeRow(Table table) {
		int colCount = table.getColumnCount();
		int rowCount = table.getData().size();
		// 列循环
		for (int i = colCount - 1; i >= 0; i--) {
			Boolean isAllowRowSpan = table.getHeader().get(table.getHeader().size() - 1).getCells().get(i)
					.getIsAllowRowSpan();
			if (!isAllowRowSpan) {
				continue;
			}
			// 表数据行循环
			for (int j = 0; j < rowCount; j++) {
				Integer rowSpan = table.getData().get(j).getCells().get(i).getRowSpan();
				if (rowSpan < 2) {
					continue;
				}
				for (int k = j + 1; k < j + rowSpan; k++) {
					table.getData().get(k).getCells().remove(i);
				}
				// 跳过跨的行
				j += rowSpan - 1;
			}
		}

		return table;
	}

	/**
	 * 是否允许跨行
	 * 
	 * @param table
	 * @param colIndex
	 * @return
	 */
	private static boolean isAllowRowSpan(Table table, int colIndex) {
		boolean res = false;
		for (int i = 0; i < table.getHeader().size(); i++) {
			if (table.getHeader().get(i).getCells().size() - 1 < colIndex) {
				res = false;
				continue;
			}
			res = res || table.getHeader().get(i).getCells().get(colIndex).getIsAllowRowSpan();
		}
		return res;
	}

	/**
	 * 是否需要求和、统计
	 * 
	 * @param table
	 * @param colIndex
	 * @return
	 */
	private static boolean isSum(Table table, int colIndex) {
		boolean res = false;
		for (int i = 0; i < table.getHeader().size(); i++) {
			if (table.getHeader().get(i).getCells().size() - 1 < colIndex) {
				res = false;
				continue;
			}
			res = res || table.getHeader().get(i).getCells().get(colIndex).getIsSum();
		}
		return res;
	}

	/**
	 * 格式化数值
	 * 
	 * @param tab
	 * @param val
	 * @return
	 */
	private static String formatNum(Table tab, double val) {
		String res = String.format("%." + tab.getNumDecimalCount() + "f", val);
		if (CommonUtil.isNotNullEmpty(tab.getNumPrefix())) {
			res = tab.getNumPrefix() + res;
		}
		if (CommonUtil.isNotNullEmpty(tab.getNumSuffix())) {
			res = res + tab.getNumSuffix();
		}
		return res;
	}

}
