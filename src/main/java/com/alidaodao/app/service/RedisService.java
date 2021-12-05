package com.alidaodao.app.service;

import com.alidaodao.app.commons.EXPX;
import com.alidaodao.app.commons.Expire;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jack
 * reids service interface
 * common param:
 * key : redisKey
 * value：redisValue
 * Expire： 秒级过期时间
 * EXPX 选择毫秒或者秒
 * ex 是否选择秒
 * nx 是否可重复写入
 */
public interface RedisService {

    String set(String key, String value);

    String set(byte[] key, byte[] value);

    String set(String key, String value, EXPX expx, long exp);

    String set(byte[] key, byte[] value, EXPX expx, long exp);

    String set(String key, String value, Expire exp);

    String set(byte[] key, byte[] value, Expire exp);

    String set(String key, String value, boolean ex, int time, boolean nx);


    String setex(String key, long seconds, String value);

    boolean setnx(String key, String value);

    boolean exists(String key);

    Long expire(String key, Expire exp);

    Long expire(byte[] key, Expire exp);

    Long expire(String key, long seconds);

    Long expireAt(String key, long unixTime);

    Long expireAt(byte[] key, long unixTime);

    Long ttl(String key);

    String get(String key);

    List<String> mget(String... keys);

    long del(String key);

    String rename(String oldKey, String newKey);

    Long incr(String key);

    Long incrby(String key, long increment);

    ScanResult<String> scan(String cursor);

    ScanResult<byte[]> scan(byte[] cursor);

    Long hset(String key, String field, String value);

    Long hset(String key, String field, String value, Expire exp);

    Long hset(byte[] key, byte[] field, byte[] value);

    Long hset(byte[] key, byte[] field, byte[] value, Expire exp);

    boolean hsetnx(String key, String field, String value);

    String hget(String key, String field);

    byte[] hget(byte[] key, byte[] field);

    Map<String, String> hgetAll(String key);

    Long hdel(String key, String field);

    Boolean hexists(byte[] key, byte[] field);

    Boolean hexists(String key, String field);

    Long hdel(byte[] key, byte[] field);

    Set<String> hkeys(String key);

    Set<byte[]> hkeys(byte[] key);

    Long hlen(String key);

    Long hlen(byte[] key);

    Set<String> keys(String pattern);

    Long lpush(String key, String... values);

    Long lpush(byte[] key, byte[]... values);

    String lpop(String key);

    List<String> lrange(String key, long start, long end);

    Long llen(String key);

    byte[] lpop(byte[] key);

    List<byte[]> lrange(byte[] key, long start, long end);

    Long rpush(String key, String... values);

    String rpop(String key);

    Long lrem(String key, long count, String value);

    Long lrem(byte[] key, long count, byte[] value);

    String ltrim(String key, long start, long end);

    String lindex(String key, long index);

    byte[] lindex(byte[] key, long index);

    Long sadd(String key, String... members);

    Long sadd(byte[] key, byte[]... members);

    Long scard(String key);

    Long scard(byte[] key);

    Long srem(String key, String... members);

    Long srem(byte[] key, byte[]... members);

    Set<String> smembers(String key);

    Boolean sismember(String key, String member);

    Boolean sismember(byte[] key, byte[] member);

    String spop(String key);

    byte[] spop(byte[] key);

    Long zadd(String key, double score, String member);

    Long zadd(byte[] key, double score, byte[] member);

    Long zadd(String key, Map<String, Double> scoreMembers);

    Long zrem(String key, String member);

    Long zrem(byte[] key, byte[] member);

    Long zRemRangeByRank(String key, long start, long end);

    Long zRemRangeByRank(byte[] key, long start, long end);

    Long zremrangeByScore(String key, long min, long max);

    Long zrank(String key, String member);

    Long zrank(byte[] key, byte[] member);

    Long zrevrank(String key, String member);

    Long zrevrank(byte[] key, byte[] member);

    Set<String> zrange(String key, long min, long max);

    Set<byte[]> zrange(byte[] key, long min, long max);

    Set<String> zrevrange(String key, long min, long max);

    Set<byte[]> zrevrange(byte[] key, long min, long max);

    Set<Tuple> zrangeWithScores(String key, long min, long max);

    Set<Tuple> zrangeWithScores(byte[] key, long min, long max);

    Set<String> zrangeByScore(String key, long min, long max);

    Set<Tuple> zrangeByScoreWithScores(String key, long min, long max);

    Set<Tuple> zrangeByScoreWithScores(byte[] key, long min, long max);

    Set<Tuple> zrevrangeWithScores(String key, long min, long max);

    Set<Tuple> zrevrangeWithScores(byte[] key, long min, long max);

    Set<Tuple> zrevrangeByScoreWithScores(String key, long min, long max);

    Set<Tuple> zrevrangeByScoreWithScores(byte[] key, long min, long max);

    Double zincrby(String key, double incrScore, String member);

    Double zincrby(byte[] key, double incrScore, byte[] member);

    Double zscore(String key, String member);

    Double zscore(byte[] key, byte[] member);

    Long zcard(String key);

    Long zcard(byte[] key);

    Long zcount(String key, double min, double max);

    Set<String> zrevrangebyscore(String key, double max, double min, int offset, int count);

    Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count);

    Long hincrBy(String key, String field, long value);

    String hmset(String key, Map<String, String> hash);

    List<String> hmget(String key, String... fields);

    Long decr(String key);


}
