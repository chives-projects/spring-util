//package com.csc.spring.demo.config;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
///**
// * @Author: csc
// * @Create: 2022-12-06
// * @Version: 1.0
// */
//@Configuration
//public class RedisConfig {
//
//    /**
//     * @param redisConnectionFactory：配置不同的客户端，这里注入的redis连接工厂不同： JedisConnectionFactory、LettuceConnectionFactory
//     * @功能描述 ：配置Redis序列化，原因如下：
//     * （1） StringRedisTemplate的序列化方式为字符串序列化，
//     * RedisTemplate的序列化方式默为jdk序列化（实现Serializable接口）
//     * （2） RedisTemplate的jdk序列化方式在Redis的客户端中为乱码，不方便查看，
//     * 因此一般修改RedisTemplate的序列化为方式为JSON方式【建议使用GenericJackson2JsonRedisSerializer】
//     */
//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setKeySerializer(stringSerializer());
//        redisTemplate.setValueSerializer(jacksonSerializer());
//        redisTemplate.setHashKeySerializer(stringSerializer());
//        redisTemplate.setHashValueSerializer(jacksonSerializer());
//        // bean初始化完成后调用方法，主要检查key-value序列化对象是否初始化，并标注RedisTemplate已经被初始化，否则会报：
//        // "template not initialized; call afterPropertiesSet() before using it" 异常
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }
//
//    @Bean
//    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisConnectionFactory);
//        stringRedisTemplate.setKeySerializer(stringSerializer());
//        stringRedisTemplate.setValueSerializer(jacksonSerializer());
//        stringRedisTemplate.setHashKeySerializer(stringSerializer());
//        stringRedisTemplate.setHashValueSerializer(jacksonSerializer());
//        // bean初始化完成后调用方法，主要检查key-value序列化对象是否初始化，并标注RedisTemplate已经被初始化，否则会报：
//        // "template not initialized; call afterPropertiesSet() before using it" 异常
//        stringRedisTemplate.afterPropertiesSet();
//        return stringRedisTemplate;
//    }
//
//    protected StringRedisSerializer stringSerializer() {
//        return new StringRedisSerializer();
//    }
//
//
//    protected Jackson2JsonRedisSerializer<Object> jacksonSerializer() {
//        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        //指定要序列化的域、field、get和set，以及修饰符范围，ANY是都有包括private和public
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        // objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        // 第一个参数用于验证要反序列化的实际子类型是否对验证器使用的任何条件有效，在反序列化时必须设置，否则报异常
//        // 第二个参数设置序列化的类型必须为非final类型，只有少数的类型（String、Boolean、Integer、Double）可以从JSON中正确推断
//        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
//        // 解决jackson2无法反序列化LocalDateTime的问题
//        objectMapper.registerModule(new JavaTimeModule());
//
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//
//        return jackson2JsonRedisSerializer;
//    }
//}
