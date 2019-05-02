package xin.xihc.utils.htmltable;

import java.util.ArrayList;
import java.util.List;

/**
 * 表格对象
 * 
 * @author 席恒昌
 * @date 2018年1月17日
 * @version 1.6
 * @since 1.6
 */
public class Table {

	/**
	 * 表格样式
	 */
	private String style = "width:100%;margin-top:20px;text-align:center;border:1px solid #ccc;";
	/**
	 * 表格边框
	 */
	private Integer border = 1;
	/**
	 * 表格单元格内间距
	 */
	private Integer cellPadding = 10;
	/**
	 * 表格单元格间间距
	 */
	private Integer cellSpacing = 0;
	/**
	 * 表头
	 */
	private List<Row> header = new ArrayList<Row>(1);
	/**
	 * 数据
	 */
	private List<Row> data = new ArrayList<Row>(10);
	/**
	 * 是否小计
	 */
	private Boolean isSubTotal = false;
	/**
	 * 小计样式
	 */
	private String subTotalStyle;
	/**
	 * 是否合计
	 */
	private Boolean isTotal = false;
	/**
	 * 合计样式
	 */
	private String totalStyle;
	/**
	 * 行是否合计
	 */
	private Boolean isRowTotal = false;
	/**
	 * 行合计样式
	 */
	private String rowTotalStyle;
	/**
	 * 数值前缀￥，可以写html语言
	 */
	private String numPrefix = "";
	/**
	 * 数值后缀-元，可以写html语言
	 */
	private String numSuffix = "元";
	/**
	 * 数值小数点位数
	 */
	private Integer numDecimalCount = 2;

	/**
	 * 数值后缀-元
	 */
	public String getNumSuffix() {
		return numSuffix;
	}

	/**
	 * 数值后缀-元，可以写html语言
	 */
	public Table setNumSuffix(String numSuffix) {
		this.numSuffix = numSuffix;
		return this;
	}

	/**
	 * 数值前缀
	 */
	public String getNumPrefix() {
		return numPrefix;
	}

	/**
	 * 数值前缀￥，可以写html语言
	 */
	public Table setNumPrefix(String numPrefix) {
		this.numPrefix = numPrefix;
		return this;
	}

	/**
	 * 数值小数点位数
	 */
	public Integer getNumDecimalCount() {
		return numDecimalCount;
	}

	/**
	 * 数值小数点位数
	 */
	public Table setNumDecimalCount(Integer numDecimalCount) {
		if (numDecimalCount < 0) {
			numDecimalCount = 0;
		}
		this.numDecimalCount = numDecimalCount;
		return this;
	}

	public Boolean getIsRowTotal() {
		return isRowTotal;
	}

	public Table setIsRowTotal(Boolean isRowTotal) {
		this.isRowTotal = isRowTotal;
		return this;
	}

	public String getRowTotalStyle() {
		return rowTotalStyle;
	}

	public Table setRowTotalStyle(String rowTotalStyle) {
		this.rowTotalStyle = rowTotalStyle;
		return this;
	}

	/**
	 * 表头
	 * 
	 * @param data
	 * @return
	 */
	public Table setHeader(List<Row> header) {
		this.header = header;
		return this;
	}

	/**
	 * 数据
	 * 
	 * @param data
	 * @return
	 */
	public Table setData(List<Row> data) {
		this.data = data;
		return this;
	}

	/**
	 * 是否开启小计
	 */
	public Boolean getIsSubTotal() {
		return isSubTotal;
	}

	/**
	 * 是否开启小计
	 */
	public Table setIsSubTotal(Boolean isSubTotal) {
		this.isSubTotal = isSubTotal;
		return this;
	}

	/**
	 * 小计样式
	 */
	public String getSubTotalStyle() {
		return subTotalStyle;
	}

	/**
	 * 小计样式
	 */
	public Table setSubTotalStyle(String subTotalStyle) {
		this.subTotalStyle = subTotalStyle;
		return this;
	}

	public Boolean getIsTotal() {
		return isTotal;
	}

	public Table setIsTotal(Boolean isTotal) {
		this.isTotal = isTotal;
		return this;
	}

	public String getTotalStyle() {
		return totalStyle;
	}

	public Table setTotalStyle(String totalStyle) {
		this.totalStyle = totalStyle;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public Table setStyle(String style) {
		this.style = style;
		return this;
	}

	public Integer getBorder() {
		return border;
	}

	public Table setBorder(Integer border) {
		this.border = border;
		return this;
	}

	public Integer getCellPadding() {
		return cellPadding;
	}

	public void setCellPadding(Integer cellPadding) {
		this.cellPadding = cellPadding;
	}

	public Integer getCellSpacing() {
		return cellSpacing;
	}

	public void setCellSpacing(Integer cellSpacing) {
		this.cellSpacing = cellSpacing;
	}

	public List<Row> getHeader() {
		return header;
	}

	public List<Row> getData() {
		return data;
	}

	/**
	 * 添加一行数据
	 */
	public Table addData(Row row) {
		this.data.add(row);
		return this;
	}

	/**
	 * 在指定位置插入一行数据
	 * 
	 * @param index
	 * @param row
	 * @return
	 */
	public Table insertData(int index, Row row) {
		this.data.add(index, row);
		return this;
	}

	public Table addHeader(Row row) {
		this.header.add(row);
		return this;
	}

	/**
	 * 获取列数
	 * 
	 * @return
	 */
	public int getColumnCount() {
		if (null == getHeader() || getHeader().size() < 1) {
			if (null == getData() || getData().size() < 1) {
				return 0;
			}
			return getData().get(0).getColumnCount();
		} else {
			return getHeader().get(0).getColumnCount();
		}
	}

}
