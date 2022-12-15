package com.eagle.spring.redis;

import com.eagle.common.InitializingAutoConfig;
import com.eagle.spring.redis.factory.EagleRedisFactory;
import com.eagle.spring.redis.lettuce.EagleLettuceConnectionConfiguration;
import com.eagle.spring.redis.original.RedisTemplateFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @description: Redis多数据源配置，参考源码：LettuceConnectionConfiguration
 * RedisAutoConfiguration
 * @Author: csc
 * @Create: 2022-12-06
 */
@AutoConfiguration
@AutoConfigureBefore(OriginalRedisAutoConfiguration.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties(EagleRedisProperties.class)
@ConditionalOnProperty(prefix = EagleRedisProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class EagleRedisAutoConfiguration implements InitializingAutoConfig {

    private DefaultListableBeanFactory defaultListableBeanFactory;
    private EagleRedisProperties redisDbProperties;

    public EagleRedisAutoConfiguration(DefaultListableBeanFactory defaultListableBeanFactory, EagleRedisProperties redisDbProperties) {
        this.defaultListableBeanFactory = defaultListableBeanFactory;
        this.redisDbProperties = redisDbProperties;
    }

    @PostConstruct
    public void init() {
        Map<String, RedisProperties> propertiesMap = redisDbProperties.getConfig();
        for (Map.Entry<String, RedisProperties> entry : propertiesMap.entrySet()) {
            String redisInstanceName = entry.getKey();
            RedisProperties properties = entry.getValue();
            RedisConnectionFactory redisConnectionFactory;
            if (RedisProperties.ClientType.JEDIS == properties.getClientType()) {
                redisConnectionFactory = null;
            } else {
                EagleLettuceConnectionConfiguration configuration = new EagleLettuceConnectionConfiguration(properties);
                LettuceConnectionFactory lettuceConnectionFactory = configuration.redisConnectionFactory();
                lettuceConnectionFactory.setShareNativeConnection(redisDbProperties.isShareNativeConnection());
                lettuceConnectionFactory.setValidateConnection(redisDbProperties.isValidateConnection());
                lettuceConnectionFactory.afterPropertiesSet();
                redisConnectionFactory = lettuceConnectionFactory;
            }

            // 获取StringRedisTemplate对象
            StringRedisTemplate stringRedisTemplate = RedisTemplateFactory.createStringRedisTemplate(redisConnectionFactory);
            // 将StringRedisTemplate对象注入IOC容器bean
            defaultListableBeanFactory.registerSingleton(EagleRedisFactory.INSTANCE.getStringRedisTemplateBeanName(redisInstanceName), stringRedisTemplate);

            // 获取RedisTemplate对象
            RedisTemplate redisTemplate = RedisTemplateFactory.createRedisTemplate(redisConnectionFactory);
            // 将RedisTemplate对象注入IOC容器
            defaultListableBeanFactory.registerSingleton(EagleRedisFactory.INSTANCE.getRedisTemplateBeanName(redisInstanceName), redisTemplate);
        }
    }
}
