package xin.xihc.utils.htmltable;

import java.util.ArrayList;
import java.util.List;

/**
 * 表格行数据
 * 
 * @author 席恒昌
 * @date 2018年1月17日
 * @version 1.6
 * @since 1.6
 */
public class Row {

	/**
	 * 行样式,
	 * <tr>
	 * </tr>
	 */
	private String style = "";
	/**
	 * 0-普通行;1-小计行;2-合计行;
	 */
	private Integer type = 0;
	/**
	 * 小计、合计行的起始列的索引
	 */
	private Integer typeColIndex = 0;
	/**
	 * 行内单元格
	 */
	private List<Cell> cells = new ArrayList<Cell>(10);

	public Integer getTypeColIndex() {
		return typeColIndex;
	}

	public void setTypeColIndex(Integer typeColIndex) {
		this.typeColIndex = typeColIndex;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public List<Cell> getCells() {
		return cells;
	}

	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}

	public int getColumnCount() {
		int res = 0;
		for (Cell cell : cells) {
			res += cell.getColSpan().intValue();
		}
		return res;
	}

}
