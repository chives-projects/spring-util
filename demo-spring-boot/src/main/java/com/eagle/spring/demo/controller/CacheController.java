package com.eagle.spring.demo.controller;

import com.eagle.spring.demo.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.support.NullValue;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: csc
 * @Create: 2023-01-17
 */
@RestController
@RequestMapping("cache")
public class CacheController {
    @Autowired
    private CacheService cacheService;

    @GetMapping(value = "caffeine/{key}")
    public String caffeine(@PathVariable String key) {
        return cacheService.caffeine(key);
    }

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping(value = "redis/{key}")
    public String redis(@PathVariable String key) {
        redisTemplate.opsForValue().set(key, key, 30, TimeUnit.SECONDS);
        return cacheService.redis(key);
    }

    public static void main(String[] args) {
        System.out.println(NullValue.INSTANCE == null);
    }

}
