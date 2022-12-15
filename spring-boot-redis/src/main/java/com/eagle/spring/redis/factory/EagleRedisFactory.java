package com.eagle.spring.redis.factory;

import com.eagle.common.enums.ApplicationStatus;
import com.eagle.common.exception.BusinessException;
import com.eagle.spring.core.context.IOCContext;
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
public class EagleRedisFactory {
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

    public static final EagleRedisFactory INSTANCE = new EagleRedisFactory();

    public static final String REDIS_MARK_NOT_EXISTS_MES = "Redis数据库标识{0}对应的数据库不存在";

    /**
     * 获取Redis模板
     *
     * @param redisMark 数据源标识
     * @return
     */
    public static StringRedisTemplate getStringRedisTemplate(String redisMark) {
        if (StringUtils.isEmpty(redisMark)) {
            throw new BusinessException(ApplicationStatus.EXCEPTION.getCode(), MessageFormat.format(REDIS_MARK_NOT_EXISTS_MES, redisMark));
        }
        //获取缓存key
        String key = INSTANCE.getStringRedisTemplateBeanName(redisMark);
        if (stringCache.containsKey(key)) {
            return stringCache.get(key);
        }
        if (!IOCContext.containsBean(key)) {
            throw new BusinessException(ApplicationStatus.EXCEPTION.getCode(), MessageFormat.format(REDIS_MARK_NOT_EXISTS_MES, redisMark));
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
            throw new BusinessException(ApplicationStatus.EXCEPTION.getCode(), MessageFormat.format(REDIS_MARK_NOT_EXISTS_MES, redisMark));
        }
        //获取缓存key
        String key = INSTANCE.getRedisTemplateBeanName(redisMark);
        if (restCache.containsKey(key)) {
            return restCache.get(key);
        }
        if (!IOCContext.containsBean(key)) {
            throw new BusinessException(ApplicationStatus.EXCEPTION.getCode(), MessageFormat.format(REDIS_MARK_NOT_EXISTS_MES, redisMark));
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
