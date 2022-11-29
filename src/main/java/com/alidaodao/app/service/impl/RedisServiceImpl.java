package com.alidaodao.app.service.impl;

import com.alidaodao.app.service.RedisService;
import com.alidaodao.app.commons.EXPX;
import com.alidaodao.app.commons.Expire;
import com.alidaodao.app.config.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.params.XReadGroupParams;
import redis.clients.jedis.params.XReadParams;
import redis.clients.jedis.resps.ScanResult;
import redis.clients.jedis.resps.StreamEntry;
import redis.clients.jedis.resps.Tuple;
import java.util.*;

/**
 * @author Jack
 * redis service impl
 */
public class RedisServiceImpl implements RedisService {

    private RedisConfig redisConfig;

    private int dbIndex;

    private RedisServiceImpl() {

    }

    public RedisServiceImpl(Integer dbIndex, RedisConfig redisConfig) {
        this.dbIndex = dbIndex;
        this.redisConfig = redisConfig;
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    private Jedis getJedis() {
        if (redisConfig == null) {
            throw new RuntimeException("redisConfig is null on pop redis");
        }
        return redisConfig.getRedisResource(String.valueOf(dbIndex));
    }

    @Override
    public String get(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.get(key);
        }
    }

    @Override
    public List<String> mget(String... keys) {
        try (Jedis jedis = getJedis()) {
            return jedis.mget(keys);
        }
    }

    @Override
    public long del(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.del(key);
        }
    }

    @Override
    public String rename(String oldKey, String newKey) {
        try (Jedis jedis = getJedis()) {
            if (jedis.exists(oldKey)) {
                return jedis.rename(oldKey, newKey);
            }
        }
        return null;
    }

    @Override
    public String set(String key, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.set(key, value);
        }
    }

    @Override
    public String set(byte[] key, byte[] value) {
        try (Jedis jedis = getJedis()) {
            return jedis.set(key, value);
        }
    }

    @Override
    public String set(String key, String value, EXPX expx, long exp) {
        try (Jedis jedis = getJedis()) {
            return jedis.set(key, value, setParams(expx, exp, false));
        }
    }

    @Override
    public String set(byte[] key, byte[] value, EXPX expx, long exp) {
        try (Jedis jedis = getJedis()) {
            return jedis.set(key, value, setParams(expx, exp, false));
        }
    }

    /**
     * @param expx 单位 秒或者毫秒
     * @param exp  过期时间
     * @param nx   true nx 重复不写入; false   xx 重复可写入
     * @return
     */
    private static SetParams setParams(EXPX expx, long exp, boolean nx) {
        SetParams setParams = new SetParams();
        if (nx) {
            setParams.nx();
        } else {
            setParams.xx();
        }
        if (expx.equals(EXPX.SECONDS)) {
            setParams.ex(exp);
        } else if (expx.equals(EXPX.MILLISECONDS)) {
            setParams.px(exp);
        }
        return setParams;
    }

    @Override
    public String set(String key, String value, Expire exp) {
        if (exp == null) {
            return "not ok";
        }
        SetParams params = setParams(EXPX.SECONDS, exp.getTime(), false);
        try (Jedis jedis = getJedis()) {
            return jedis.set(key, value, params);
        }
    }

    @Override
    public String set(byte[] key, byte[] value, Expire exp) {
        if (exp == null) {
            return "not ok";
        }
        return set(key, value, EXPX.SECONDS, (long) exp.getTime());
    }

    @Override
    public String set(String key, String value, boolean ex, int time, boolean nx) {
        try (Jedis jedis = getJedis()) {
            return jedis.set(key, value, setParams(ex ? EXPX.SECONDS : EXPX.MILLISECONDS, time, nx));
        }
    }

    @Override
    public String setex(String key, long seconds, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.setex(key, seconds, value);
        }
    }

    @Override
    public boolean setnx(String key, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.setnx(key, value) > 0L;
        }
    }

    @Override
    public boolean exists(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.exists(key);
        }
    }

    @Override
    public Long expire(String key, Expire exp) {
        try (Jedis jedis = getJedis()) {
            return jedis.expire(key, exp.getTime());
        }
    }

    @Override
    public Long expire(byte[] key, Expire exp) {
        try (Jedis jedis = getJedis()) {
            return jedis.expire(key, exp.getTime());
        }
    }


    @Override
    public Long expire(String key, long seconds) {
        try (Jedis jedis = getJedis()) {
            return jedis.expire(key, seconds);
        }
    }

    @Override
    public Long expireAt(String key, long unixTime) {
        try (Jedis jedis = getJedis()) {
            return jedis.expireAt(key, unixTime);
        }
    }

    @Override
    public Long expireAt(byte[] key, long unixTime) {
        try (Jedis jedis = getJedis()) {
            return jedis.expireAt(key, unixTime);
        }
    }

    @Override
    public Long ttl(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.ttl(key);
        }
    }

    @Override
    public Long incr(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.incr(key);
        }
    }


    @Override
    public Long incrby(String key, long increment) {
        try (Jedis jedis = getJedis()) {
            return jedis.incrBy(key, increment);
        }
    }


    @Override
    public Set<String> keys(String pattern) {
        try (Jedis jedis = getJedis()) {
            return jedis.keys(pattern);
        }
    }


    @Override
    public ScanResult<String> scan(String cursor) {
        try (Jedis jedis = getJedis()) {
            return jedis.scan(cursor);
        }
    }

    @Override
    public ScanResult<byte[]> scan(byte[] cursor) {
        try (Jedis jedis = getJedis()) {
            return jedis.scan(cursor);
        }
    }


    @Override
    public Long hset(String key, String field, String value) {
        return hset(key, field, value, null);
    }

    @Override
    public Long hset(String key, String field, String value, Expire exp) {
        Long opCount = null;
        try (Jedis jedis = getJedis()) {
            opCount = jedis.hset(key, field, value);
            if (exp != null) {
                jedis.expire(key, exp.getTime());
            }
        }
        return opCount;
    }

    @Override
    public Long hset(byte[] key, byte[] field, byte[] value) {
        return hset(key, field, value, null);
    }

    @Override
    public Long hset(byte[] key, byte[] field, byte[] value, Expire exp) {
        Long opCount = null;
        try (Jedis jedis = getJedis()) {
            opCount = jedis.hset(key, field, value);
            if (exp != null) {
                jedis.expire(key, exp.getTime());
            }
        }
        return opCount;

    }

    @Override
    public boolean hsetnx(String key, String field, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.hsetnx(key, field, value) > 0L;
        }
    }

    @Override
    public String hget(String key, String field) {
        try (Jedis jedis = getJedis()) {
            return jedis.hget(key, field);
        }
    }

    @Override
    public byte[] hget(byte[] key, byte[] field) {
        try (Jedis jedis = getJedis()) {
            return jedis.hget(key, field);
        }
    }

    /**
     * 该命令不建议使用，所以标记过期
     *
     * @param key
     * @return
     */
    @Override
    @Deprecated
    public Map<String, String> hgetAll(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hgetAll(key);
        }
    }


    @Override
    public Long hdel(String key, String field) {
        try (Jedis jedis = getJedis()) {
            return jedis.hdel(key, field);
        }
    }

    @Override
    public Long hdel(byte[] key, byte[] field) {
        try (Jedis jedis = getJedis()) {
            return jedis.hdel(key, field);
        }
    }

    @Override
    public Boolean hexists(byte[] key, byte[] field) {
        try (Jedis jedis = getJedis()) {
            return jedis.hexists(key, field);
        }
    }

    @Override
    public Boolean hexists(String key, String field) {
        try (Jedis jedis = getJedis()) {
            return jedis.hexists(key, field);
        }
    }

    @Override
    public Set<String> hkeys(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hkeys(key);
        }
    }

    @Override
    public Set<byte[]> hkeys(byte[] key) {
        try (Jedis jedis = getJedis()) {
            return jedis.keys(key);
        }
    }

    @Override
    public Long hlen(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hlen(key);
        }
    }

    @Override
    public Long hlen(byte[] key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hlen(key);
        }
    }

    @Override
    public Long lpush(String key, String... values) {
        try (Jedis jedis = getJedis()) {
            return jedis.lpush(key, values);
        }
    }

    @Override
    public Long lpush(byte[] key, byte[]... values) {
        try (Jedis jedis = getJedis()) {
            return jedis.lpush(key, values);
        }
    }

    @Override
    public String lpop(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.lpop(key);
        }
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        try (Jedis jedis = getJedis()) {
            return jedis.lrange(key, start, end);
        }
    }

    @Override
    public List<byte[]> lrange(byte[] key, long start, long end) {
        try (Jedis jedis = getJedis()) {
            return jedis.lrange(key, start, end);
        }
    }

    @Override
    public Long llen(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.llen(key);
        }
    }

    @Override
    public byte[] lpop(byte[] key) {
        try (Jedis jedis = getJedis()) {
            return jedis.lpop(key);
        }
    }

    @Override
    public Long rpush(String key, String... values) {
        try (Jedis jedis = getJedis()) {
            return jedis.rpush(key, values);
        }
    }

    @Override
    public String rpop(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.rpop(key);
        }
    }

    @Override
    public Long lrem(String key, long count, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.lrem(key, count, value);
        }
    }

    @Override
    public Long lrem(byte[] key, long count, byte[] value) {
        try (Jedis jedis = getJedis()) {
            return jedis.lrem(key, count, value);
        }
    }


    @Override
    public String ltrim(String key, long start, long end) {
        try (Jedis jedis = getJedis()) {
            return jedis.ltrim(key, start, end);
        }
    }

    @Override
    public String lindex(String key, long index) {
        try (Jedis jedis = getJedis()) {
            return jedis.lindex(key, index);
        }
    }

    @Override
    public byte[] lindex(byte[] key, long index) {
        try (Jedis jedis = getJedis()) {
            return jedis.lindex(key, index);
        }
    }

    @Override
    public Long sadd(String key, String... members) {
        try (Jedis jedis = getJedis()) {
            return jedis.sadd(key, members);
        }
    }

    @Override
    public Long sadd(byte[] key, byte[]... members) {
        try (Jedis jedis = getJedis()) {
            return jedis.sadd(key, members);
        }
    }

    @Override
    public Long scard(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.scard(key);
        }
    }

    @Override
    public Long scard(byte[] key) {
        try (Jedis jedis = getJedis()) {
            return jedis.scard(key);
        }
    }

    @Override
    public Long srem(String key, String... members) {
        try (Jedis jedis = getJedis()) {
            return jedis.srem(key, members);
        }
    }

    @Override
    public Long srem(byte[] key, byte[]... members) {
        try (Jedis jedis = getJedis()) {
            return jedis.srem(key, members);
        }
    }

    @Override
    public Set<String> smembers(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.smembers(key);
        }
    }

    @Override
    public Boolean sismember(String key, String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.sismember(key, member);
        }
    }

    @Override
    public Boolean sismember(byte[] key, byte[] member) {
        try (Jedis jedis = getJedis()) {
            return jedis.sismember(key, member);
        }
    }

    @Override
    public String spop(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.spop(key);
        }
    }

    @Override
    public byte[] spop(byte[] key) {
        try (Jedis jedis = getJedis()) {
            return jedis.spop(key);
        }
    }

    @Override
    public Long zadd(String key, double score, String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zadd(key, score, member);
        }
    }

    @Override
    public Long zadd(byte[] key, double score, byte[] member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zadd(key, score, member);
        }
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        try (Jedis jedis = getJedis()) {
            return jedis.zadd(key, scoreMembers);
        }
    }

    @Override
    public Long zrem(String key, String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrem(key, member);
        }
    }

    @Override
    public Long zrem(byte[] key, byte[] member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrem(key, member);
        }
    }

    @Override
    public Long zRemRangeByRank(String key, long start, long end) {
        try (Jedis jedis = getJedis()) {
            return jedis.zremrangeByRank(key, start, end);
        }
    }

    @Override
    public Long zRemRangeByRank(byte[] key, long start, long end) {
        try (Jedis jedis = getJedis()) {
            return jedis.zremrangeByRank(key, start, end);
        }
    }

    @Override
    public Long zremrangeByScore(String key, long min, long max) {
        try (Jedis jedis = getJedis()) {
            return jedis.zremrangeByScore(key, min, max);
        }
    }

    @Override
    public Long zrank(String key, String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrank(key, member);
        }
    }

    @Override
    public Long zrank(byte[] key, byte[] member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrank(key, member);
        }
    }

    @Override
    public Long zrevrank(String key, String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrevrank(key, member);
        }
    }

    @Override
    public Long zrevrank(byte[] key, byte[] member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrevrank(key, member);
        }
    }

    @Override
    public Set<String> zrange(String key, long min, long max) {
        List<String> result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.zrange(key, min, max);
        }
        if (Objects.nonNull(result) && result.size() > 0) {
            return new HashSet<>(result);
        }
        return null;
    }


    @Override
    public Set<byte[]> zrange(byte[] key, long min, long max) {
        List<byte[]> result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.zrange(key, min, max);
        }
        if (Objects.nonNull(result) && result.size() > 0) {
            return new HashSet<>(result);
        }
        return null;
    }


    @Override
    public Set<String> zrevrange(String key, long min, long max) {
        List<String> result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.zrevrange(key, min, max);
        }
        if (Objects.nonNull(result) && result.size() > 0) {
            return new HashSet<>(result);
        }
        return null;
    }


    @Override
    public Set<byte[]> zrevrange(byte[] key, long min, long max) {
        List<byte[]> result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.zrevrange(key, min, max);
        }
        if (Objects.nonNull(result) && result.size() > 0) {
            return new HashSet<>(result);
        }
        return null;
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long min, long max) {
        List<Tuple> result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.zrangeWithScores(key, min, max);
        }
        if (Objects.nonNull(result) && result.size() > 0) {
            return new HashSet<>(result);
        }
        return null;
    }

    @Override
    public Set<Tuple> zrangeWithScores(byte[] key, long min, long max) {
        List<Tuple> result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.zrangeWithScores(key, min, max);
        }
        if (Objects.nonNull(result) && result.size() > 0) {
            return new HashSet<>(result);
        }
        return null;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, long min, long max) {
        List<Tuple> result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.zrangeByScoreWithScores(key, min, max);
        }
        if (Objects.nonNull(result) && result.size() > 0) {
            return new HashSet<>(result);
        }
        return null;
    }

    @Override
    public Set<String> zrevrangebyscore(String key, double max, double min, int offset, int count) {
        List<String> result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.zrevrangeByScore(key, max, min, offset, count);
        }
        if (Objects.nonNull(result) && result.size() > 0) {
            return new HashSet<>(result);
        }
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        List<Tuple> result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        }
        if (Objects.nonNull(result) && result.size() > 0) {
            return new HashSet<>(result);
        }
        return null;
    }

    @Override
    public List<String> zrangeByScore(String key, long min, long max) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrangeByScore(key, min, max);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, long min, long max) {
        List<Tuple> result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.zrangeByScoreWithScores(key, min, max);
        }
        if (Objects.nonNull(result) && result.size() > 0) {
            return new HashSet<>(result);
        }
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String key, long min, long max) {
        List<Tuple> result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.zrevrangeWithScores(key, min, max);
        }
        if (Objects.nonNull(result) && result.size() > 0) {
            return new HashSet<>(result);
        }
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(byte[] key, long min, long max) {
        List<Tuple> result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.zrevrangeWithScores(key, min, max);
        }
        if (Objects.nonNull(result) && result.size() > 0) {
            return new HashSet<>(result);
        }
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, long min, long max) {
        List<Tuple> result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.zrevrangeByScoreWithScores(key, min, max);
        }
        if (Objects.nonNull(result) && result.size() > 0) {
            return new HashSet<>(result);
        }
        return null;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, long min, long max) {
        List<Tuple> result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.zrevrangeByScoreWithScores(key, min, max);
        }
        if (Objects.nonNull(result) && result.size() > 0) {
            return new HashSet<>(result);
        }
        return null;
    }

    @Override
    public Double zincrby(String key, double incrScore, String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zincrby(key, incrScore, member);
        }
    }

    @Override
    public Double zincrby(byte[] key, double incrScore, byte[] member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zincrby(key, incrScore, member);
        }
    }

    @Override
    public Double zscore(String key, String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zscore(key, member);
        }
    }

    @Override
    public Double zscore(byte[] key, byte[] member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zscore(key, member);
        }
    }

    @Override
    public Long zcard(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.zcard(key);
        }
    }

    @Override
    public Long zcard(byte[] key) {
        try (Jedis jedis = getJedis()) {
            return jedis.zcard(key);
        }
    }

    @Override
    public Long zcount(String key, double min, double max) {
        try (Jedis jedis = getJedis()) {
            return jedis.zcount(key, min, max);
        }
    }

    @Override
    public Long hincrBy(String key, String field, long value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(field);
        try (Jedis jedis = getJedis()) {
            return jedis.hincrBy(key, field, value);
        }
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        try (Jedis jedis = getJedis()) {
            return jedis.hmset(key, hash);
        }
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        try (Jedis jedis = getJedis()) {
            return jedis.hmget(key, fields);
        }
    }

    /**
     * stream add
     *
     * @param key
     * @param streamEntryID
     * @param content
     * @return
     */
    @Override
    public StreamEntryID xadd(String key, StreamEntryID streamEntryID, Map<String, String> content) {
        try (Jedis jedis = getJedis()) {
            return jedis.xadd(key, streamEntryID, content);
        }
    }

    /**
     * get stream value for asc
     *
     * @param key
     * @param start
     * @param end
     * @param count
     * @return
     */
    @Override
    public List<StreamEntry> xrange(String key, StreamEntryID start, StreamEntryID end, int count) {
        try (Jedis jedis = getJedis()) {
            return jedis.xrange(key,start,end,count);
        }
    }

    /**
     * get stream value for desc
     *
     * @param key
     * @param end
     * @param start
     * @param count
     * @return
     */
    @Override
    public List<StreamEntry> xrevrange(String key, StreamEntryID start, StreamEntryID end, int count) {
        try (Jedis jedis = getJedis()) {
            return jedis.xrange(key,end,start,count);
        }
    }

    /**
     * create group stream
     *
     * @param stream
     * @param group
     * @param makeStream need create stream ?
     * @return
     */
    @Override
    public String xgroupCreate(String stream, String group, Boolean makeStream) {
        return xgroupCreate(stream, group, null, makeStream);
    }

    /**
     * create group stream
     *
     * @param stream
     * @param group
     * @param id
     * @param makeStream
     * @return
     */
    @Override
    public String xgroupCreate(String stream, String group, StreamEntryID id, Boolean makeStream) {
        try (Jedis jedis = getJedis()) {
            return jedis.xgroupCreate(stream, group, id, makeStream);
        }
    }

    /**
     * read group stream
     *
     * @param group
     * @param consumer
     * @param count
     * @param streams
     * @return
     */
    @Override
    public List<Map.Entry<String, List<StreamEntry>>> xreadGroup(String group, String consumer, int count, Map<String, StreamEntryID> streams) {
        try (Jedis jedis = getJedis()) {
            XReadGroupParams p = new XReadGroupParams();
            p.count(count);
            p.block(0);
            return jedis.xreadGroup(group, consumer, p, streams);
        }
    }

    /**
     * read group stream
     *
     * @param count
     * @param streams
     * @return
     */
    @Override
    public List<Map.Entry<String, List<StreamEntry>>> xread(int count, Map<String, StreamEntryID> streams) {
        try (Jedis jedis = getJedis()) {
            XReadParams p = new XReadParams();
            p.count(count);
            p.block(0);
            return jedis.xread(p, streams);
        }
    }

    @Override
    public Long decr(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.decr(key);
        }
    }

    @Override
    public Long decrBy(String key, long decrement) {
        try (Jedis jedis = getJedis()) {
            return jedis.decrBy(key,decrement);
        }
    }


}
