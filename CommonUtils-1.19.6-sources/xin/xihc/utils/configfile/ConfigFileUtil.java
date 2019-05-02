package xin.xihc.utils.configfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xin.xihc.utils.common.CommonUtil;
import xin.xihc.utils.common.DateUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * 读写配置文件工具类,支持多线程操作,兼容application.properties等属性配置文件
 *
 * @author 席恒昌
 * @version 1.5
 * @date 2017年12月11日
 * @since 1.0
 */
public final class ConfigFileUtil {

    private final static String commentsStart = "#";
    private static Logger LOG = LoggerFactory.getLogger(ConfigFileUtil.class);

    private ConfigFileUtil() {
    }

    /**
     * 读取配置文件类的所有配置项
     *
     * @param fileName 文件路径名称
     * @return 有序的Map
     * @throws Exception 异常
     */
    public static TreeMap<String, String> readAllConfig(String fileName) throws IOException {
        synchronized (fileName) {
            TreeMap<String, String> configMap = new TreeMap<>();
            File f = new File(fileName);
            if (!f.exists()) {
                if (!f.getParentFile().exists()) {
                    f.getParentFile().mkdirs();
                }
                f.createNewFile();
                return configMap;
            }
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            try {
                String line = null;
                while ((line = br.readLine()) != null) {
                    int pos = line.indexOf("=");
                    if (pos < 1 || line.startsWith(commentsStart)) {
                        continue;
                    }
                    configMap.put(line.substring(0, pos).trim(), line.substring(pos + 1).trim());
                }
            } finally {
                if (null != br) {
                    br.close();
                }
                if (null != fr) {
                    fr.close();
                }
            }
            return configMap;
        }
    }

    /**
     * 将配置文件中符合的属性赋值到对象conf中
     *
     * @param clazz
     * @param readNull 是否需要读取空值
     * @return BaseConfig
     */
    public static <T extends _BaseConfig> T readConfig(Class<T> clazz, boolean readNull) {
        Objects.requireNonNull(clazz, "clazz is null");

        T res = null;
        try {
            res = clazz.newInstance();
            String filePath = res.filePath();
            TreeMap<String, String> allConfig = readAllConfig(filePath);
            res = convertToConfigBean(allConfig, clazz, readNull);
        } catch (Exception e) {
            LOG.error("ConfigFileUtil.readConfig():", e);
            res = null;
        }
        return res;
    }

    /**
     * 将配置文件中符合的属性转成TreeMap
     *
     * @return BaseConfig
     */
    public static TreeMap<String, String> readAllConfig(InputStream input) throws IOException {
        Objects.requireNonNull(input, "inputStream is null");

        TreeMap<String, String> configMap = new TreeMap<>();
        InputStreamReader reader = new InputStreamReader(input);
        BufferedReader br = new BufferedReader(reader);
        try {
            String line = null;
            while ((line = br.readLine()) != null) {
                int pos = line.indexOf("=");
                if (pos < 1 || line.startsWith(commentsStart)) {
                    continue;
                }
                configMap.put(line.substring(0, pos).trim(), line.substring(pos + 1).trim());
            }
            return configMap;
        } finally {
            if (null != br) {
                br.close();
            }
            if (null != reader) {
                reader.close();
            }
        }
    }

    /**
     * 将所有的key-value转为Bean
     *
     * @param allConfig   所有的配置
     * @param clazz       需要转为的对象CLass
     * @param convertNull 值为null的是否转
     * @param <T>
     * @return
     */
    public static <T extends _BaseConfig> T convertToConfigBean(Map<String, String> allConfig, Class<T> clazz, boolean convertNull) {
        Objects.requireNonNull(allConfig, "allConfig is null");
        Objects.requireNonNull(clazz, "clazz is null");

        T res = null;
        try {
            res = clazz.newInstance();
            String prefix = res.prefix();
            if (!convertNull && allConfig.size() < 1) {
                return res;
            }
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Class<?> type = field.getType();
                String key = field.getName();
                if (CommonUtil.isNotNullEmpty(prefix)) {
                    key = prefix + "." + field.getName();
                }
                String val = allConfig.get(key);
                try {
                    if (null == val) {
                        // 读取null时先设置为null
                        if (convertNull) {
                            field.set(res, null);
                        }
                    } else {
                        if (type.equals(int.class) || type.equals(Integer.class)) {
                            field.set(res, Integer.valueOf(val));
                        } else if (type.equals(double.class) || type.equals(Double.class)) {
                            field.set(res, Double.valueOf(val));
                        } else if (type.equals(float.class) || type.equals(Float.class)) {
                            field.set(res, Float.valueOf(val));
                        } else if (type.equals(byte.class) || type.equals(Byte.class)) {
                            field.set(res, Byte.valueOf(val));
                        } else if (type.equals(long.class) || type.equals(Long.class)) {
                            field.set(res, Long.valueOf(val));
                        } else if (type.equals(short.class) || type.equals(Short.class)) {
                            field.set(res, Short.valueOf(val));
                        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
                            field.set(res, Boolean.valueOf(val));
                            // 枚举处理
                        } else if (field.getType().isEnum()) {
                            Object[] t = field.getType().getEnumConstants();
                            for (Object item : t) {
                                if (item.toString().equalsIgnoreCase(val)) {
                                    field.set(res, item);
                                    break;
                                }
                            }
                        } else {
                            field.set(res, val);
                        }
                    }
                } catch (Exception e) {
                    LOG.error("field.set():", e);
                }
            }
        } catch (Exception e) {
            LOG.error("ConfigFileUtil.convertToConfigBean():", e);
        }
        return res;
    }

    /**
     * 将配置文件中符合的属性赋值到对象conf中
     *
     * @param clazz
     * @param readNull 是否需要读取空值
     * @return BaseConfig
     */
    public static <T extends _BaseConfig> T readConfigStream(InputStream input, Class<T> clazz, boolean readNull) throws IOException {
        TreeMap<String, String> allConfig = readAllConfig(input);
        return convertToConfigBean(allConfig, clazz, readNull);
    }

    /**
     * 将对象conf中的属性保存到配置文件中
     *
     * @param conf 需要保存的对象
     * @return true为保存成功
     */
    public static <T extends _BaseConfig> boolean saveConfig(T conf) {
        boolean res = false;
        if (null == conf) {
            return res;
        }
        try {
            // 先读取所有配置，在修改，以免破坏其他的配置信息
            TreeMap<String, String> allConfig = readAllConfig(conf.filePath());
            synchronized (conf.filePath()) {
                Field[] fields = conf.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object valObj = field.get(conf);
                    if (null != valObj) {
                        allConfig.put(conf.prefix() + "." + field.getName(), valObj.toString().trim());
                    }
                }
                FileWriter fw = new FileWriter(new File(conf.filePath()));
                BufferedWriter bw = new BufferedWriter(fw);
                try {
                    bw.write(commentsStart + DateUtil.getNow() + "修改");
                    bw.newLine();
                    String lastPrefix = "";
                    for (String key : allConfig.keySet()) {
                        String prefix = key.substring(0, key.lastIndexOf("."));
                        if (!lastPrefix.equals(prefix)) {
                            lastPrefix = prefix;
                            bw.newLine();
                            bw.write(commentsStart + prefix + "的属性");
                            bw.newLine();
                        }
                        bw.write(key + "=" + allConfig.get(key));
                        bw.newLine();
                    }
                    bw.flush();
                    res = true;
                } finally {
                    if (null != bw) {
                        bw.close();
                    }
                    if (null != fw) {
                        fw.close();
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("ConfigFileUtil.save():", e);
            res = false;
        }
        return res;
    }

}

