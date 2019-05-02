/**
 * 
 */
package xin.xihc.utils.configfile;

/**
 * 定义的配置文件对象的接口，自定义配置属性对象需要实现它,不能使用基本数据类型（int/byte/long/float/double）
 * 
 * @author 席恒昌
 * @date 2018年1月5日
 * @version 1.4
 * @since 1.4
 */
public abstract class _BaseConfig {

	/**
	 * 配置文件前缀，默认为包名+类名,可重写方法实现自定义前缀
	 * 
	 * @return 文件前缀
	 */
	public String prefix() {
		return this.getClass().getName();
	}

	/**
	 * 配置文件的路径+名称,可重写方法实现自定义文件路径
	 * 
	 * @return 文件的路径
	 */
	public String filePath() {
		return "./config/config.conf";
	}

}
