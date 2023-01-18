package com.eagle.spring.demo.service;

/**
 * @Description:
 * @Author: csc
 * @Create: 2023-01-17
 */
public interface CacheService {
    String caffeine(String key);

    String redis(String key);
}
