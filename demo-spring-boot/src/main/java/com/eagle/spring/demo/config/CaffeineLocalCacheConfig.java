package com.eagle.spring.demo.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: csc
 * @Create: 2023-01-17
 */
@Configuration
public class CaffeineLocalCacheConfig {

    /**
     * 创建基于Caffeine的Cache Manager
     */
    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        Caffeine caffeine = Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
//                .refreshAfterWrite(5,TimeUnit.SECONDS)
                .expireAfterWrite(20, TimeUnit.SECONDS);

        CaffeineCacheManager cacheManager = new CaffeineCacheManager("caffeineCache", "itemCache");
        cacheManager.setAllowNullValues(true);
        cacheManager.setCaffeine(caffeine);
//        cacheManager.setCacheLoader(cacheLoader);
        return cacheManager;
    }
//
//    @Value("${caffeine.spec}")
//    private String caffeineSpec;
//
//    @Bean(name = "caffeineSpec")
//    public CacheManager cacheManagerWithCaffeineFromSpec() {
//        CaffeineSpec spec = CaffeineSpec.parse(caffeineSpec);
//        Caffeine caffeine = Caffeine.from(spec);
//        //此方法等同于上面from(spec)
////        Caffeine caffeine = Caffeine.from(caffeineSpec);
//
//        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
//        cacheManager.setCaffeine(caffeine);
//        cacheManager.setCacheNames(getNames());
//        return cacheManager;
//    }

}

