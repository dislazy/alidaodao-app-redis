package com.alidaodao.com;

import com.alidaodao.app.RedisClient;
import com.alidaodao.app.commons.Expire;
import com.alidaodao.app.config.RedisConfig;
import com.alidaodao.app.service.RedisService;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * test
 */
public class RedisClientTest {


    public static void main(String[] args) {
        RedisService cache = getRedisClient().getByDefault();
        //单元测试
        String set = cache.set("1234".getBytes(StandardCharsets.UTF_8), "abcd".getBytes(StandardCharsets.UTF_8));
        String dadhd = cache.set("5678", "dadhd");
        String djfhd = cache.set("91011", "djfhd", new Expire(10));
        String set1 = cache.set("111213".getBytes(StandardCharsets.UTF_8), "dfff".getBytes(StandardCharsets.UTF_8), new Expire(100));
        String setex = cache.setex("111213", 100, "12999");
        System.out.println(setex);
        //TODO need add stream test
    }

    private static RedisClient getRedisClient(){
        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setRedisHost("127.0.0.1");
        redisConfig.setRedisPort(6380);
        redisConfig.setRedisPwd("");
        //此处为需要创建的redis db实例
        Set<Integer> redisIndexes = new HashSet<>();
        redisIndexes.add(0);
        RedisClient redisClient = new RedisClient(redisConfig, redisIndexes);
        redisClient.setDefaultIndex(0);
        return redisClient;
    }
}
