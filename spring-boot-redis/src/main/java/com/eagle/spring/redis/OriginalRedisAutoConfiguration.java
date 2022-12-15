package com.eagle.spring.redis;

import com.eagle.common.InitializingAutoConfig;
import com.eagle.spring.redis.lettuce.EagleLettuceConnectionConfiguration;
import com.eagle.spring.redis.original.RedisTemplateFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Description:
 * @Author: csc
 * @Create: 2022-12-16
 * @Version: 1.0
 */
@AutoConfiguration
@AutoConfigureBefore(RedisAutoConfiguration.class)
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
public class OriginalRedisAutoConfiguration implements InitializingAutoConfig {

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = RedisTemplateFactory.createRedisTemplate(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = RedisTemplateFactory.createStringRedisTemplate(redisConnectionFactory);
        return stringRedisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisConnectionFactory redisConnectionFactory(RedisProperties properties) {
        RedisConnectionFactory redisConnectionFactory;
        if (RedisProperties.ClientType.JEDIS == properties.getClientType()) {
            redisConnectionFactory = null;
        } else {
            EagleLettuceConnectionConfiguration configuration = new EagleLettuceConnectionConfiguration(properties);
            LettuceConnectionFactory lettuceConnectionFactory = configuration.redisConnectionFactory();
            lettuceConnectionFactory.afterPropertiesSet();
            redisConnectionFactory = lettuceConnectionFactory;
        }
        return redisConnectionFactory;
    }

}
