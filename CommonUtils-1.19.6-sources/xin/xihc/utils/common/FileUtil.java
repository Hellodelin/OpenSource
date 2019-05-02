package xin.xihc.utils.common;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 文件工具类
 *
 * @author Leo.Xi
 * @version 1.0
 * @date 2018/10/16 20:50
 * @since 1.0
 */
public class FileUtil {

    private FileUtil() {
    }

    /**
     * 将文件内容读取为字符串
     *
     * @param file 需要读取的文件
     * @return 以默认的编码的字符串
     * @throws IOException
     */
    public static String readFileToStr(File file) throws IOException {
        byte[] bytes = readFileToBytes(file);
        return new String(bytes);
    }

    /**
     * 将文件内容读取为字符串
     *
     * @param file    需要读取的文件
     * @param charset 指定字符编码
     * @return
     * @throws IOException
     */
    public static String readFileToStr(File file, Charset charset) throws IOException {
        byte[] bytes = readFileToBytes(file);
        return new String(bytes, charset);
    }

    /**
     * 将文件内容读取为字节数组
     *
     * @param file 需要读取的文件
     * @return
     * @throws IOException
     */
    public static byte[] readFileToBytes(File file) throws IOException {
        Objects.requireNonNull(file, "file is null");
        return readInputStream(new FileInputStream(file));
    }

    /**
     * 读出流的全部内容，并关闭流
     *
     * @param inputStream 输入流
     * @return
     * @throws IOException
     * @since 1.18
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        Objects.requireNonNull(inputStream, "inputStream is null");

        ByteArrayOutputStream baos = new ByteArrayOutputStream(inputStream.available());
        byte[] buffer = new byte[1024];// 1KB读一次
        try {
            int temp;
            while ((temp = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, temp);
            }
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
        }
        return baos.toByteArray();
    }

    /**
     * 保存字符串到文件
     *
     * @param data   需要保存的数据
     * @param toFile 目的文件
     * @param append 是否追加
     * @throws IOException
     */
    public static void saveToFile(String data, File toFile, final boolean append) throws IOException {
        saveToFile(data.getBytes(), toFile, append);
    }

    /**
     * 保存字符串到文件
     *
     * @param data    需要保存的数据
     * @param toFile  目的文件
     * @param append  是否追加
     * @param charset 保存的字符编码
     * @throws IOException
     */
    public static void saveToFile(String data, File toFile, final boolean append, Charset charset) throws IOException {
        saveToFile(data.getBytes(charset), toFile, append);
    }

    /**
     * 保存到文件
     *
     * @param data   需要保存的数据
     * @param toFile 目的文件
     * @param append 是否追加
     * @throws IOException
     */
    public static void saveToFile(byte[] data, File toFile, final boolean append) throws IOException {
        Objects.requireNonNull(data, "data is null");
        Objects.requireNonNull(toFile, "toFIle is null");

        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (!toFile.exists()) {
            toFile.createNewFile();
        }
        try (FileOutputStream fos = new FileOutputStream(toFile, append)) {
            fos.write(data);
            fos.flush();
        }
    }

    /**
     * 删除文件以及文件夹
     *
     * @param f      需要删除的文件对象
     * @param filter 自定义过滤器,返回true的则删掉
     */
    public static void deleteFile(File f, Predicate<File> filter) {
        if (null == f || !f.exists()) {
            return;
        }
        if (f.isDirectory()) {
            // 是文件夹先删除掉子文件
            File[] files = f.listFiles();
            if (null != files) {
                for (File file : files) {
                    deleteFile(file, filter);
                }
            }
        }
        if (filter.test(f)) {
            f.delete();
        }
    }


}
