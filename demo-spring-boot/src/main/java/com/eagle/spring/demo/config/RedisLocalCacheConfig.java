package com.eagle.spring.demo.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @PackageName: com.eastmoney.hafu.acdataapi.config
 * @Author: csc
 * @Create: 2022-11-11 17:39
 * @Version: 1.0
 */
@Configuration
public class RedisLocalCacheConfig {
    /**
     * @param --redisConnectionFactory
     * @功能描述 redis作为缓存时配置缓存管理器CacheManager，主要配置序列化方式、自定义
     * <p>
     * 注意：配置缓存管理器CacheManager有两种方式：
     * 方式1：通过RedisCacheConfiguration.defaultCacheConfig()获取到默认的RedisCacheConfiguration对象，
     * 修改RedisCacheConfiguration对象的序列化方式等参数【这里就采用的这种方式】
     * https://blog.csdn.net/user2025/article/details/106595257/
     * 方式2：通过继承CachingConfigurerSupport类自定义缓存管理器，覆写各方法，参考：
     * https://blog.csdn.net/echizao1839/article/details/102660649
     * <p>
     * 切记：在缓存配置类中配置以后，yaml配置文件中关于缓存的redis配置就不会生效，如果需要相关配置需要通过@value去读取
     */
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration
                // 设置key采用String的序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer.UTF_8))
                //设置value序列化方式采用jackson方式序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer()))
                //当value为null时不进行缓存
                .disableCachingNullValues()
                // 配置缓存空间名称的前缀
                .prefixCacheNameWith("demo:")
                //全局配置缓存过期时间【可以不配置】
                .entryTtl(Duration.ofMinutes(30L));
        //专门指定某些缓存空间的配置，如果过期时间【主要这里的key为缓存空间名称】
        Map<String, RedisCacheConfiguration> map = new HashMap<>();
        //指定特定缓存空间对应的过期时间
        map.put("redisCache", redisCacheConfiguration.entryTtl(Duration.ofSeconds(40)));
//        map.put(entry.getKey(), redisCacheConfiguration.entryTtl(Duration.ofSeconds(30)));
        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)  //默认配置
                .withInitialCacheConfigurations(map)  //某些缓存空间的特定配置
                .build();
    }

    /**
     * 此方法不能用@Ben注解，避免替换Spring容器中的同类型对象
     */
    public GenericJackson2JsonRedisSerializer serializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}
