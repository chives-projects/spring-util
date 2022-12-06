package com.csc.spring.redis;

import com.csc.spring.logback.LoggerFactory;
import com.csc.spring.redis.factory.RedisDbFactory;
import com.csc.spring.redis.lettuce.CustomLettuceConnectionConfiguration;
import com.csc.spring.redis.original.RedisTemplateFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
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
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties(RedisDbProperties.class)
@ConditionalOnProperty(prefix = RedisDbProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class RedisDbAutoConfiguration implements InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(RedisDbAutoConfiguration.class);

    private DefaultListableBeanFactory defaultListableBeanFactory;
    private RedisDbProperties redisDbProperties;

    public RedisDbAutoConfiguration(DefaultListableBeanFactory defaultListableBeanFactory, RedisDbProperties redisDbProperties) {
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
                CustomLettuceConnectionConfiguration configuration = new CustomLettuceConnectionConfiguration(properties);
                LettuceConnectionFactory lettuceConnectionFactory = configuration.redisConnectionFactory();
                lettuceConnectionFactory.setShareNativeConnection(redisDbProperties.isShareNativeConnection());
                lettuceConnectionFactory.setValidateConnection(redisDbProperties.isValidateConnection());
                lettuceConnectionFactory.afterPropertiesSet();
                redisConnectionFactory = lettuceConnectionFactory;
            }

            // 获取StringRedisTemplate对象
            StringRedisTemplate stringRedisTemplate = RedisTemplateFactory.createStringRedisTemplate(redisConnectionFactory);
            // 将StringRedisTemplate对象注入IOC容器bean
            defaultListableBeanFactory.registerSingleton(RedisDbFactory.INSTANCE.getStringRedisTemplateBeanName(redisInstanceName), stringRedisTemplate);

            // 获取RedisTemplate对象
            RedisTemplate redisTemplate = RedisTemplateFactory.createRedisTemplate(redisConnectionFactory);
            // 将RedisTemplate对象注入IOC容器
            defaultListableBeanFactory.registerSingleton(RedisDbFactory.INSTANCE.getRedisTemplateBeanName(redisInstanceName), redisTemplate);
        }
    }

    @Override
    public void destroy() {
        logger.info("<== 【销毁--自动化配置】----Redis数据库多数据源组件【RedisDataSourceAutoConfiguration】");
    }

    @Override
    public void afterPropertiesSet() {
        logger.info("==> 【初始化--自动化配置】----Redis数据库多数据源组件【RedisDataSourceAutoConfiguration】");
    }
}
