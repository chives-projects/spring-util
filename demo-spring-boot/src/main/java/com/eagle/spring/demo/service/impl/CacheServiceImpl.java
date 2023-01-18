package com.eagle.spring.demo.service.impl;

import com.eagle.spring.demo.service.CacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: csc
 * @Create: 2023-01-17
 */
@Service
public class CacheServiceImpl implements CacheService {
    @Override
//    @Cacheable(value = "caffeineCache", key = "#key")
    @Cacheable(value = "caffeineCache", cacheManager = "caffeineCacheManager")
    public String caffeine(String key) {
        System.out.println("caffeine from DB……");
        return key;
    }

    @Override
//    @Cacheable(value = "redisCache", key = "#key")
    @Cacheable(value = "redisCache", key = "#key", cacheManager = "redisCacheManager")
    public String redis(String key) {
        System.out.println("redis from DB……");
        return key;
    }
}
