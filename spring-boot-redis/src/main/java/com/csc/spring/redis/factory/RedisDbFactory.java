package com.csc.spring.redis.factory;

import com.csc.common.enums.ApplicationStatus;
import com.csc.common.exception.BusinessException;
import com.csc.spring.core.context.IOCContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: Redis数据源，不支持以beanName的形式通过注解获取(但是这样注解上会报错)
 * @Author: csc
 * @Create: 2022-12-06
 */
public class RedisDbFactory {
    /**
     * StringRedisTemplate后缀
     */
    private static final String STRING_SUFFIX = "StringRedisTemplate";
    /**
     * RestTemplate对象后缀
     */
    private static final String REST_SUFFIX = "RedisTemplate";
    /**
     * StringRedisTemplate对象缓存
     */
    private static final Map<String, StringRedisTemplate> stringCache = new ConcurrentHashMap<>();
    /**
     * RedisTemplate对象缓存
     */
    private static final Map<String, RedisTemplate> restCache = new ConcurrentHashMap<>();

    public static final RedisDbFactory INSTANCE = new RedisDbFactory();

    /**
     * 获取Redis模板
     *
     * @param redisMark 数据源标识
     * @return
     */
    public static StringRedisTemplate getStringRedisTemplate(String redisMark) {
        if (StringUtils.isEmpty(redisMark)) {
            throw new BusinessException(ApplicationStatus.EXCEPTION.getCode(), "Redis数据库标识" + redisMark + "对应的数据库不存在");
        }
        //获取缓存key
        String key = INSTANCE.getStringRedisTemplateBeanName(redisMark);
        if (stringCache.containsKey(key)) {
            return stringCache.get(key);
        }
        if (!IOCContext.containsBean(key)) {
            throw new BusinessException(ApplicationStatus.EXCEPTION.getCode(), "Redis数据库标识" + redisMark + "对应的数据库不存在");
        }
        StringRedisTemplate stringRedisTemplate = IOCContext.getBean(key, StringRedisTemplate.class);
        stringCache.put(key, stringRedisTemplate);
        return stringRedisTemplate;
    }

    /**
     * 获取Redis模板对戏
     *
     * @param redisMark 数据源标识
     * @return
     */
    public static RedisTemplate getRedisTemplate(String redisMark) {
        if (StringUtils.isEmpty(redisMark)) {
            throw new BusinessException(ApplicationStatus.EXCEPTION.getCode(), "Redis数据库标识" + redisMark + "对应的数据库不存在");
        }
        //获取缓存key
        String key = INSTANCE.getRedisTemplateBeanName(redisMark);
        if (restCache.containsKey(key)) {
            return restCache.get(key);
        }
        if (!IOCContext.containsBean(key)) {
            throw new BusinessException(ApplicationStatus.EXCEPTION.getCode(), "Redis数据库标识" + redisMark + "对应的数据库不存在");
        }
        RedisTemplate redisTemplate = IOCContext.getBean(key, RedisTemplate.class);
        restCache.put(key, redisTemplate);
        return redisTemplate;
    }

    /**
     * 获取StringRedisTemplate对象bean名称
     *
     * @param redisMark
     * @return
     */
    public String getStringRedisTemplateBeanName(String redisMark) {
        if (Objects.isNull(redisMark)) {
            throw new BusinessException(ApplicationStatus.ILLEGAL_ARGUMENT.getCode(), "Redis数据库标识不可为空");
        }
        return MessageFormat.format("{0}{1}", redisMark, STRING_SUFFIX);
    }

    /**
     * 获取RedisTemplate对象bean名称
     *
     * @param redisMark
     * @return
     */
    public String getRedisTemplateBeanName(String redisMark) {
        if (Objects.isNull(redisMark)) {
            throw new BusinessException(ApplicationStatus.ILLEGAL_ARGUMENT.getCode(), "Redis数据库标识不可为空");
        }
        return MessageFormat.format("{0}{1}", redisMark, REST_SUFFIX);
    }
}
