package xin.xihc.utils.htmltable;

/**
 * table单元格
 * 
 * @author 席恒昌
 * @date 2018年1月17日
 * @version 1.6
 * @since 1.6
 */
public class Cell {

	public static Cell newInstance() {
		return new Cell();
	}

	private Cell() {

	}

	/**
	 * 单元格样式
	 */
	private String style = "";
	/**
	 * 是否允许跨即统计列,仅在表头是有效,与isSum互斥
	 */
	private Boolean isAllowRowSpan = false;
	/**
	 * 是否求和,仅在表头是有效
	 */
	private Boolean isSum = false;
	/**
	 * 跨行数
	 */
	private Integer rowSpan = 1;
	/**
	 * 跨列数
	 */
	private Integer colSpan = 1;
	/**
	 * 支持html文本：<a href=""></a>
	 */
	private String value = "";

	public String getStyle() {
		return style;
	}

	/**
	 * 单元格样式
	 */
	public Cell setStyle(String style) {
		this.style = style;
		return this;
	}

	public Boolean getIsAllowRowSpan() {
		return isAllowRowSpan;
	}

	public Cell setIsAllowRowSpan(Boolean isAllowRowSpan) {
		if (isAllowRowSpan && this.isSum) {
			isAllowRowSpan = false;
		}
		this.isAllowRowSpan = isAllowRowSpan;
		return this;
	}

	public Integer getRowSpan() {
		return rowSpan;
	}

	public Cell setRowSpan(Integer rowSpan) {
		this.rowSpan = rowSpan;
		return this;
	}

	public Integer getColSpan() {
		return colSpan;
	}

	public Cell setColSpan(Integer colSpan) {
		this.colSpan = colSpan;
		return this;
	}

	public Boolean getIsSum() {
		return isSum;
	}

	public Cell setIsSum(Boolean isSum) {
		if (this.isAllowRowSpan && isSum) {
			isSum = false;
		}
		this.isSum = isSum;
		return this;
	}

	public String getValue() {
		return value;
	}

	public Cell setValue(String value) {
		this.value = value;
		return this;
	}

}
