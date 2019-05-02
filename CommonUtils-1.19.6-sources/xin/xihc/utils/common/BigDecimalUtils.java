/**
 *
 */
package xin.xihc.utils.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 数值计算工具类,精确计算、防溢出计算
 *
 * @author xihc
 * @version 1.0
 * @date 2018年09月26日
 */
public class BigDecimalUtils {

	private RoundingMode roundingMode = RoundingMode.HALF_UP;// 默认四舍五入

	private int scale = 4;// 默认小数点位数

	public BigDecimalUtils() {
	}

	/**
	 * @param roundingMode
	 * @param scale
	 */
	public BigDecimalUtils(int scale, RoundingMode roundingMode) {
		super();
		this.roundingMode = roundingMode;
		this.scale = scale;
	}

	/**
	 * 设置保留位数方式
	 *
	 * @param mode
	 */
	public void setRoundingMode(RoundingMode mode) {
		roundingMode = mode;
	}

	/**
	 * @return the scale
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(int scale) {
		this.scale = scale;
	}

	/**
	 * @return the roundingMode
	 */
	public RoundingMode getRoundingMode() {
		return roundingMode;
	}

	/**
	 * 多个数字相加
	 *
	 * @param bn
	 * @return
	 */
	public <T extends Number> BigDecimal add(T... bn) {
		BigDecimal res = BigDecimal.ZERO.setScale(this.scale, this.roundingMode);
		if (null == bn || bn.length < 1) {
			return res;
		}
		for (int i = 0; i < bn.length; i++) {
			if (null != bn[i]) {
				res = res.add(BigDecimal.valueOf(bn[i].doubleValue()));
			}
		}
		return res.setScale(this.scale, this.roundingMode);
	}

	/**
	 * 多个数做减法
	 *
	 * @param b1 被减数
	 * @param bn
	 * @return
	 */
	public <T extends Number> BigDecimal subtract(T b1, T... bn) {
		return subtract(false, b1, bn);
	}

	/**
	 * BigDecimal的安全减法运算
	 *
	 * @param isZero 减法结果为负数时是否返回0，true是返回0（金额计算时使用），false是返回负数结果
	 * @param b1     被减数
	 * @param bn     需要减的减数数组
	 * @return
	 */
	public <T extends Number> BigDecimal subtract(Boolean isZero, T b1, T... bn) {
		BigDecimal res = BigDecimal.ZERO.setScale(this.scale, this.roundingMode);
		if (null != b1) {
			res = BigDecimal.valueOf(b1.doubleValue());
		}
		for (T b : bn) {
			if (null != b) {
				res = res.subtract(BigDecimal.valueOf(b.doubleValue()));
			}
		}
		if (isZero) {
			if (res.compareTo(BigDecimal.ZERO) <= 0) {
				return BigDecimal.ZERO.setScale(this.scale, this.roundingMode);
			}
		}
		return res.setScale(this.scale, this.roundingMode);
	}

	/**
	 * 金额除法计算，返回4位小数
	 *
	 * @param b1
	 * @param b2
	 * @return
	 */
	public <T extends Number> BigDecimal divide(T b1, T b2) {
		return divide(b1, b2, BigDecimal.ZERO);
	}

	/**
	 * BigDecimal的除法运算封装，如果除数或者被除数为0，返回默认值 <br>
	 * 默认返回小数位后4位，用于金额计算
	 *
	 * @param b1
	 * @param b2
	 * @param defaultValue
	 * @return
	 */
	public <T extends Number> BigDecimal divide(T b1, T b2, BigDecimal defaultValue) {
		if (null == b1 || null == b2) {
			return defaultValue.setScale(this.scale, this.roundingMode);
		}
		try {
			return BigDecimal.valueOf(b1.doubleValue()).divide(BigDecimal.valueOf(b2.doubleValue()), this.scale, this.roundingMode);
		} catch (Exception e) {
			return defaultValue.setScale(this.scale, this.roundingMode);
		}
	}

	/**
	 * BigDecimal的乘法运算封装
	 *
	 * @param b1
	 * @param bn
	 * @return 最后格式化位数
	 */
	public <T extends Number> BigDecimal multiply(T b1, T... bn) {
		BigDecimal res = BigDecimal.ZERO.setScale(this.scale, this.roundingMode);
		if (null == b1 || b1.doubleValue() == 0d) {
			return res;
		}
		res = BigDecimal.valueOf(b1.doubleValue());
		for (T b : bn) {
			if (null == b) {
				continue;
			}
			res = res.multiply(BigDecimal.valueOf(b.doubleValue()));
		}
		return res.setScale(this.scale, this.roundingMode);
	}

}
