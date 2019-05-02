package xin.xihc.utils.logfile;

import xin.xihc.utils.common.DateUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * 保存日志记录到文件,支持多线程操作,支持多个文件保存,限制单个文件大小
 *
 * @author 席恒昌
 * @version 1.5
 * @Date 2017年11月17日
 */
public final class LogFileUtil {

	/**
	 * 是否为调试模式，即控制台也输出
	 */
	private static boolean debugger = true;
	/**
	 * 文件路径格式
	 */
	private static PathType pathType = PathType.YYYYMM_DD;
	/**
	 * 日志文件保存的根路径
	 */
	private static String logRootDir = "./logs";
	/**
	 * 每个日志文件大小单位MB
	 */
	private static int logSize = 5;// 5MB
	/**
	 * 系统剩余空间大小 单位GB
	 */
	private static int systemFreeSize = 5;// 5GB
	/**
	 * 创建一个空的信号量
	 */
	private static Semaphore semaphore = new Semaphore(0);
	/**
	 * 日志消息队列
	 */
	private static LinkedBlockingQueue<LogBean> logQueue = new LinkedBlockingQueue<>();

	static {
		Thread thread = new Thread(() -> {
			while (true) {
				semaphore.acquireUninterruptibly();// 阻塞
				LogBean bean = logQueue.poll();
				write(bean);
			}
		});
		thread.setName("WriteToLogFile");
		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * 初始化日志配置
	 *
	 * @param logRootDir     日志保存路径
	 * @param logSize        单个日志大小(MB)
	 * @param systemFreeSize 系统空间剩余大小(GB)
	 */
	public static void init(String logRootDir, int logSize, int systemFreeSize) {
		LogFileUtil.logRootDir = logRootDir;
		File f = new File(logRootDir);
		if (!f.exists()) {
			f.mkdirs();
		}
		LogFileUtil.logSize = logSize;
		LogFileUtil.systemFreeSize = systemFreeSize;
	}

	public static boolean isDebugger() {
		return debugger;
	}

	public static void setDebugger(boolean debugger) {
		LogFileUtil.debugger = debugger;
	}

	public static PathType getPathType() {
		return pathType;
	}

	public static void setPathType(PathType pathType) {
		LogFileUtil.pathType = pathType;
	}

	public static String getLogRootDir() {
		return logRootDir;
	}

	public static int getLogSize() {
		return logSize;
	}

	public static int getSystemFreeSize() {
		return systemFreeSize;
	}

	public static int getSystemTotalSize() {
		File f = new File(logRootDir);
		return (int) (f.getFreeSpace() / 1024 / 1024 / 1024);
	}

	/**
	 * 获取当天文件计数
	 *
	 * @param filePath
	 * @param key
	 * @return
	 */
	private static int getMaxFileCount(String filePath, final String key) {
		int res = 0;
		File f = new File(filePath);
		File[] listFiles = f.listFiles(pathname -> {
			if (pathname.getName().indexOf("_" + key + "_") > -1) {
				return true;
			}
			return false;
		});
		if (null != listFiles) {
			res = listFiles.length;
		}
		return res == 0 ? 0 : res - 1;
	}

	/**
	 * 增加文件计数
	 *
	 * @param filePath
	 * @param key
	 * @return
	 */
	private static int addFileCount(String filePath, String key) {
		int res = getMaxFileCount(filePath, key);
		res++;
		// 新增线程执行存储空间检查
		new Thread(() -> {
			File f = new File(logRootDir);
			long usableSpace = f.getUsableSpace();
			if (usableSpace < systemFreeSize * 1024 * 1024 * 1024) {
				deleteFile(f.listFiles()[0]);
			}
			System.gc();
		}).start();
		return res;
	}

	/**
	 * 记录info日志
	 *
	 * @param name
	 * @param info
	 */
	public static void info(String name, String info) {
		if (null == name || "".equals(name)) {
			name = "info";
		}
		LogBean e = new LogBean();
		e.setLevel("INFO");
		e.setName(name);
		e.setMsg(info);
		e.setDate(new Date());
		logQueue.add(e);
		semaphore.release();
	}

	/**
	 * 记录错误error日志
	 *
	 * @param name
	 * @param error
	 */
	public static void error(String name, String error) {
		if (null == name || "".equals(name)) {
			name = "info";
		}
		LogBean e = new LogBean();
		e.setLevel("ERROR");
		e.setName(name);
		e.setMsg(error);
		e.setDate(new Date());
		logQueue.add(e);
		semaphore.release();
	}

	/**
	 * 记录异常Exception日志
	 *
	 * @param name
	 * @param e
	 */
	public static void exception(String name, Throwable e) {
		if (null == name || "".equals(name)) {
			name = "info";
		}
		LogBean log = new LogBean();
		log.setLevel("EXCEPTION");
		log.setName(name);
		log.setMsg(e);
		log.setDate(new Date());
		logQueue.add(log);
		semaphore.release();
	}

	/**
	 * 记录异常信息
	 *
	 * @param logBean
	 */
	private static void write(LogBean logBean) {
		if (null == logBean)
			return;
		PrintStream ps = null;
		try {
			File f = getFile(logBean);
			ps = new PrintStream(new FileOutputStream(f, true), false, "UTF-8");

			ps.println("[" + logBean.getLevel() + "] [" + DateUtil
					.formatDateTime(logBean.getDate(), DateUtil.FORMAT_DATETIME_MS) + "]");
			if (logBean.getMsg() instanceof Throwable) {
				if (debugger) {
					System.err.println();
					System.err.println("[" + logBean.getLevel() + "] [" + DateUtil
							.formatDateTime(logBean.getDate(), DateUtil.FORMAT_DATETIME_MS) + "]");
					System.err.println(((Throwable) logBean.getMsg()).getLocalizedMessage());
					StackTraceElement[] se = ((Throwable) logBean.getMsg()).getStackTrace();
					for (int i = 0; i < se.length; i++) {
						System.err.println(se[i].toString());
					}
				}
				ps.println(((Throwable) logBean.getMsg()).getLocalizedMessage());
				StackTraceElement[] se = ((Throwable) logBean.getMsg()).getStackTrace();
				for (int i = 0; i < se.length; i++) {
					ps.println(se[i].toString());
				}
			} else {
				if (debugger) {
					System.out.println();
					System.out.println("[" + logBean.getLevel() + "] [" + DateUtil
							.formatDateTime(logBean.getDate(), DateUtil.FORMAT_DATETIME_MS) + "]");
					System.out.println(logBean.getMsg());
				}
				ps.println(logBean.getMsg());
			}
			ps.println();
		} catch (Exception ex) {
			ex.printStackTrace();
			exception(logBean.getName(), ex);
		} finally {
			if (null != ps) {
				ps.close();
			}
		}
	}

	/**
	 * 根据name获取文件路径以及计数
	 *
	 * @param logBean
	 * @return
	 * @throws IOException
	 */
	private static File getFile(LogBean logBean) throws IOException {
		String today = DateUtil.formatDateTime(logBean.getDate(), DateUtil.FORMAT_DATE_NUM);
		String fileName = logRootDir + "/";
		switch (pathType) {
			case YYYY_MM_DD:
				fileName += today.substring(0, 4) + "/" + today.substring(4, 6) + "/" + today.substring(6, 8) + "/";
				break;
			case YYYY_MMDD:
				fileName += today.substring(0, 4) + "/" + today.substring(4, 8) + "/";
				break;
			case YYYYMMDD:
				fileName += today + "/";
				break;
			case YYYYMM_DD:
				fileName += today.substring(0, 6) + "/" + today.substring(6, 8) + "/";
				break;
			default:
				break;
		}
		File f = new File(fileName + today + "_" + logBean.getName() + "_" + getMaxFileCount(fileName,
				logBean.getName()) + ".log");
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		if (!f.exists()) {
			f.createNewFile();
		}
		if (f.length() >= logSize * 1024 * 1024) {
			f = new File(fileName + today + "_" + logBean.getName() + "_" + addFileCount(fileName,
					logBean.getName()) + ".log");
		}
		return f;
	}

	/**
	 * 删除文件以及文件夹
	 *
	 * @param files
	 */
	private static void deleteFile(File... files) {
		for (File file : files) {
			if (file.isDirectory()) {
				deleteFile(file.listFiles());
			}
			file.delete();
		}
	}

	/**
	 * 日志保存路径枚举
	 *
	 * @author 席恒昌
	 * @version 1.5
	 * @date 2018年1月17日
	 * @since 1.5
	 */
	public enum PathType {
		YYYY_MM_DD, YYYY_MMDD, YYYYMMDD, YYYYMM_DD;

		private PathType() {

		}
	}

	/**
	 * 日志队列的对象
	 *
	 * @author 席恒昌
	 * @Date 2017年12月8日
	 * @Description
	 */
	public static class LogBean {

		private String level;
		private String name;
		private Date date;
		private Object msg;

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public String getLevel() {
			return level;
		}

		public void setLevel(String level) {
			this.level = level;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Object getMsg() {
			return msg;
		}

		public void setMsg(Object msg) {
			this.msg = msg;
		}

	}

}
