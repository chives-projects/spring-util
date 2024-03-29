package com.eagle.common.utils.calculation;

import com.eagle.common.enums.ApplicationStatus;
import com.eagle.common.exception.BusinessException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP压缩及解压缩工具类
 */
public class GZIPUtils {

    /**
     * 字符串压缩为GZIP字节数组
     *
     * @param str
     * @return
     */
    public static byte[] compress(String str) {
        return compress(str, StandardCharsets.UTF_8);
    }

    /**
     * 字符串压缩为GZIP字节数组
     *
     * @param str      字符串
     * @param charset 编码
     * @return 压缩后的字节数组
     */
    public static byte[] compress(String str, Charset charset) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(charset));
            gzip.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ApplicationStatus.ILLEGAL_ARGUMENT.getCode(), "字符串压缩异常");
        }
        return out.toByteArray();
    }

    /**
     * GZIP解压缩
     *
     * @param bytes
     * @return
     */
    public static byte[] decompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ApplicationStatus.ILLEGAL_ARGUMENT.getCode(), "字符串解压缩异常");
        }
        return out.toByteArray();
    }

    /**
     * 将字节数组解压缩为字符串
     *
     * @param bytes 字节数组
     * @return
     */
    public static String decompressToString(byte[] bytes) {
        return decompressToString(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 将字节数组解压缩为字符串
     *
     * @param bytes    字节数组
     * @param charset 编码
     * @return
     */
    public static String decompressToString(byte[] bytes, Charset charset) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(charset.name());
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ApplicationStatus.ILLEGAL_ARGUMENT.getCode(), "字符串解压缩异常");
        }
    }
}
