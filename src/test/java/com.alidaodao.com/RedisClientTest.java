package com.alidaodao.com;

import com.alidaodao.app.RedisClient;
import com.alidaodao.app.commons.Expire;
import com.alidaodao.app.config.RedisConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * test
 */
public class RedisClientTest {


    public static void main(String[] args) {
        RedisClient redisClient = getRedisClient();
        for (int i = 0; i < 100; i++) {
            String test = redisClient.getByDefault().set("test:" + i, "test", Expire.h2);
            System.out.println(test+" " + i);
        }
    }

    private static RedisClient getRedisClient(){
        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setRedisHost("127.0.0.1");
        redisConfig.setRedisPort(6379);
        redisConfig.setRedisPwd("test");
        //此处为需要创建的redis db实例
        Set<Integer> redisIndexes = new HashSet<>();
        redisIndexes.add(0);
        RedisClient redisClient = new RedisClient(redisConfig, redisIndexes);
        redisClient.setDefaultIndex(0);
        return redisClient;
    }
}
