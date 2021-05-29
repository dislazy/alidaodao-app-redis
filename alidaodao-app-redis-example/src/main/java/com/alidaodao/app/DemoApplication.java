package com.alidaodao.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jack
 * redis client
 */
@SpringBootApplication
@RestController
public class DemoApplication {

    @Autowired
    private RedisClient redisClient;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


    @GetMapping(value = "/dev")
    public String addRedis(@RequestParam(value = "test") String test) {
        redisClient.get(3).set("1", test);
        return redisClient.get(3).get("1");
    }
}
