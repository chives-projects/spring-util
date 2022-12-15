package com.eagle.logback.domain.entity;

import org.springframework.util.StringUtils;

/**
 * @Description: 路径工具类
 * @create: 2022/11/18
*/
public class LogbackUrl {

    private static final String SLASH = "/";

    /**
     * @param path 路径
     * @return
     * @Description 规范化路径
     */
    public static String normalizePath(String path) {
        if (!StringUtils.hasText(path)) {
            return path;
        }
        String normalizedPath = path;
        if (!normalizedPath.startsWith(SLASH)) {
            normalizedPath = SLASH + normalizedPath;
        }
        if (normalizedPath.endsWith(SLASH)) {
            normalizedPath = normalizedPath.substring(0, normalizedPath.length() - 1);
        }
        return normalizedPath;
    }
}
