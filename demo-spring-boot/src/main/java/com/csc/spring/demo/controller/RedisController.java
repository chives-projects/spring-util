package com.csc.spring.demo.controller;

import com.csc.spring.demo.po.Job;
import com.eagle.common.utils.character.JSONUtils;
import com.eagle.spring.redis.factory.EagleRedisFactory;
import com.google.common.collect.Maps;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description: 缓存测试
 * @Author: csc
 * @Create: 2022-12-06
 */
@RestController
@RequestMapping("redis")
public class RedisController {

    public static final String KEY_PREFIX = "redis-key:";

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @GetMapping("original")
    public String original() {
        stringRedisTemplate.opsForValue().set(KEY_PREFIX + "test0", "123", 12, TimeUnit.MINUTES);
        Job job = new Job();
        job.setId(23);
        job.setJobDesc("desc");
        job.setJobNumber(23L);
        stringRedisTemplate.opsForValue().set(KEY_PREFIX + "test1", job.toString());
        stringRedisTemplate.opsForValue().set(KEY_PREFIX + "test2", JSONUtils.toJSONString(job));

        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put(KEY_PREFIX + "te", 12);
        dataMap.put(KEY_PREFIX + "te2", 12);
        dataMap.put(KEY_PREFIX + "te3", "哈哈");
        redisTemplate.opsForValue().set(KEY_PREFIX + "test3", dataMap, 1, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(KEY_PREFIX + "test4", JSONUtils.toJSONString(dataMap));


        stringRedisTemplate.opsForHash().put(KEY_PREFIX + "test5", "h", JSONUtils.toJSONString(job));
        redisTemplate.opsForHash().put(KEY_PREFIX + "test5", "b", job);
        redisTemplate.opsForHash().put(KEY_PREFIX + "test5", "h", JSONUtils.toJSONString(job));
        redisTemplate.opsForHash().put(KEY_PREFIX + "test6", "h", dataMap);
        return "test";
    }

    @GetMapping("blockAdd/{key}")
    public void blockAdd(@PathVariable String key) {
        redisTemplate.opsForList().rightPush(key, "value");
    }

    @GetMapping("block/{key}")
    public void block(@PathVariable String key) {
        List<Object> obj = redisTemplate.executePipelined(
                //队列没有元素会阻塞操作，直到队列获取新的元素或超时
                (RedisCallback) connection -> connection.bLPop(0, key.getBytes()),
                redisTemplate.getKeySerializer());

        for (Object o : obj) {
            System.out.println("---------------" + o);
        }
    }

    @GetMapping("multi/{key}")
    public void multi(@PathVariable String key) {
        // 事例事务代码
        RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
        List<Object> executeResult = (List<Object>) redisTemplate.execute((RedisCallback<List<Object>>) connection -> {
            connection.multi();
            connection.set(redisTemplate.getKeySerializer().serialize(key), valueSerializer.serialize("v"));

            List<Object> execResult = connection.exec();
            return execResult;
        });
        System.out.println(executeResult);
    }


    @GetMapping("custom/{redisMark}")
    public String custom(@PathVariable String redisMark) {
        EagleRedisFactory.getStringRedisTemplate(redisMark).opsForValue().set(KEY_PREFIX + "test0", "123", 12, TimeUnit.MINUTES);
        Job job = new Job();
        job.setId(23);
        job.setJobDesc("desc");
        job.setJobNumber(23L);
        EagleRedisFactory.getStringRedisTemplate(redisMark).opsForValue().set(KEY_PREFIX + "test1", job.toString());
        EagleRedisFactory.getStringRedisTemplate(redisMark).opsForValue().set(KEY_PREFIX + "test2", JSONUtils.toJSONString(job));

        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put(KEY_PREFIX + "te", 12);
        dataMap.put(KEY_PREFIX + "te2", 12);
        dataMap.put(KEY_PREFIX + "te3", "哈哈");
        EagleRedisFactory.getRedisTemplate(redisMark).opsForValue().set(KEY_PREFIX + "test3", dataMap, 1, TimeUnit.MINUTES);
        EagleRedisFactory.getRedisTemplate(redisMark).opsForValue().set(KEY_PREFIX + "test4", JSONUtils.toJSONString(dataMap));


        EagleRedisFactory.getStringRedisTemplate(redisMark).opsForHash().put(KEY_PREFIX + "test5", "h", JSONUtils.toJSONString(job));
        EagleRedisFactory.getRedisTemplate(redisMark).opsForHash().put(KEY_PREFIX + "test5", "b", job);
        EagleRedisFactory.getRedisTemplate(redisMark).opsForHash().put(KEY_PREFIX + "test5", "h", JSONUtils.toJSONString(job));
        EagleRedisFactory.getRedisTemplate(redisMark).opsForHash().put(KEY_PREFIX + "test6", "h", dataMap);

        return redisMark;
    }


//    @Resource(name = "defaultRedisTemplate")
//    private RedisTemplate defaultRedisTemplate;
//    @Resource(name = "defaultStringRedisTemplate")
//    private StringRedisTemplate defaultStringRedisTemplate;
//    @GetMapping("custom1")
//    public String custom1() {
//        defaultStringRedisTemplate.opsForValue().set(KEY_PREFIX + "test0", "123", 12, TimeUnit.MINUTES);
//        Job job = new Job();
//        job.setId(23L);
//        job.setJobDesc("desc");
//        job.setJobNumber(23L);
//        defaultStringRedisTemplate.opsForValue().set(KEY_PREFIX + "test1", job.toString());
//        defaultStringRedisTemplate.opsForValue().set(KEY_PREFIX + "test2", JSONUtils.toJSONString(job));
//
//        Map<String, Object> dataMap = Maps.newHashMap();
//        dataMap.put(KEY_PREFIX + "te", 12);
//        dataMap.put(KEY_PREFIX + "te2", 12);
//        dataMap.put(KEY_PREFIX + "te3", "哈哈");
//        defaultRedisTemplate.opsForValue().set(KEY_PREFIX + "test3", dataMap, 1, TimeUnit.MINUTES);
//        defaultRedisTemplate.opsForValue().set(KEY_PREFIX + "test4", JSONUtils.toJSONString(dataMap));
//
//
//        defaultStringRedisTemplate.opsForHash().put(KEY_PREFIX + "test5", "h", JSONUtils.toJSONString(job));
//        defaultRedisTemplate.opsForHash().put(KEY_PREFIX + "test5", "b", job);
//        defaultRedisTemplate.opsForHash().put(KEY_PREFIX + "test5", "h", JSONUtils.toJSONString(job));
//        defaultRedisTemplate.opsForHash().put(KEY_PREFIX + "test6", "h", dataMap);
//        return "test";
//    }

    @GetMapping("roll")
    public void roll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                   /* try {
                        Thread.sleep(100);
                    } catch (Exception e) {

                    }*/
                    System.out.println("--------roll----------");
                    EagleRedisFactory.getStringRedisTemplate("test").opsForValue().set(KEY_PREFIX + "roll_test1", "123" + System.currentTimeMillis());
                }
            }
        }).start();
    }
}
