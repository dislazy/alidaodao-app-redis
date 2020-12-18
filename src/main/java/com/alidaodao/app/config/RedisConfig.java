package com.alidaodao.app.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;


public class RedisConfig {

    private String host;

    private int port;

    private String password;

    private int maxTotal = 1024;

    private int maxIdle = 200;

    private int maxWaitMillis = 2000;

    private boolean testOnBorrow = false;

    private boolean testOnReturn = false;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    private StampedLock lock = new StampedLock();

    private Map<String, JedisPool> redisPoolMap = new ConcurrentHashMap<>();



    public Jedis getRedisResource(String dbIndexStr) {
        JedisPool jedisPool = redisPoolMap.get(dbIndexStr);
        if (jedisPool == null) {
            jedisPool = newRedisPool(Integer.parseInt(dbIndexStr));
            long stamp = lock.writeLock();
            try {
                redisPoolMap.put(dbIndexStr, jedisPool);
            } finally {
                lock.unlockWrite(stamp);
            }
        }
        return jedisPool.getResource();
    }

    private JedisPool newRedisPool(int dbIndex) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(config.getMaxTotal());
        config.setMaxIdle(config.getMaxIdle());
        config.setMaxWaitMillis(config.getMaxWaitMillis());
        config.setTestOnBorrow(config.getTestOnBorrow());
        config.setTestOnReturn(config.getTestOnReturn());
        return new JedisPool(config, getHost(), getPort(), Protocol.DEFAULT_TIMEOUT * 10, getPassword(),
                dbIndex, null);
    }
}
