package com.eagle.spring.datasource.support.helper;

import com.eagle.spring.datasource.DataSourceProperties;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 多数据源帮助类,DynamicRoutingDataSource->AbstractRoutingDataSource 获取数据源
 * @Author: csc
 * @create: 2023/01/06
 */
public class DataSourceHelper {

    /**
     * 获取默认数据源
     *
     * @return
     */
    public static Object getDefaultTargetDataSource(DataSourceProperties properties) {
        return getTargetDataSources(properties).get(properties.getDefaultConfig());
    }

    /**
     * 获取合并后的目标数据源配置
     *
     * @return
     */
    public static Map<Object, Object> getTargetDataSources(DataSourceProperties properties) {
        Map<Object, Object> dsMap = new HashMap();
        if (!CollectionUtils.isEmpty(properties.getDruid())) {
            dsMap.putAll(properties.getDruid());
        }
        if (!CollectionUtils.isEmpty(properties.getHikari())) {
            dsMap.putAll(properties.getHikari());
        }
        if (!CollectionUtils.isEmpty(properties.getJndi())) {
            dsMap.putAll(properties.getJndi());
        }
        return Collections.unmodifiableMap(dsMap);
    }
}
