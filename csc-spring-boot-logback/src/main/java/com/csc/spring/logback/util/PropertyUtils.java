package com.csc.spring.logback.util;

import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

/**
 * @description: cloud微服务判定
 * @Author :  csc
 * @create: 2022/11/18
* @since 4.0.7
 */
public abstract class PropertyUtils {
    public static final String BOOTSTRAP_ENABLED_PROPERTY = "spring.cloud.bootstrap.enabled";
    public static final String USE_LEGACY_PROCESSING_PROPERTY = "spring.config.use-legacy-processing";
    public static final String MARKER_CLASS = "org.springframework.cloud.bootstrap.marker.Marker";
    public static final boolean MARKER_CLASS_EXISTS = ClassUtils.isPresent(MARKER_CLASS, (ClassLoader) null);

    private PropertyUtils() {
        throw new UnsupportedOperationException("unable to instatiate utils class");
    }

    public static boolean bootstrapEnabled(Environment environment) {
        return environment.getProperty(BOOTSTRAP_ENABLED_PROPERTY, Boolean.class, false) || MARKER_CLASS_EXISTS;
    }

    public static boolean useLegacyProcessing(Environment environment) {
        return environment.getProperty(USE_LEGACY_PROCESSING_PROPERTY, Boolean.class, false);
    }
}
