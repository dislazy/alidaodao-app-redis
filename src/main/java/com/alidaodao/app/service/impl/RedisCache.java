package com.alidaodao.app.service.impl;

import com.alidaodao.app.service.RedisService;
import com.alidaodao.app.commons.EXPX;
import com.alidaodao.app.commons.Expire;
import com.alidaodao.app.config.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.SetParams;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Jack
 * redis service impl
 */
public class RedisCache implements RedisService {

    private RedisConfig redisConfig;

    private int dbIndex;

    private RedisCache() {

    }

    public RedisCache(Integer dbIndex, RedisConfig redisConfig) {
        this.dbIndex = dbIndex;
        this.redisConfig = redisConfig;
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    private Jedis getJedis(){
        if (redisConfig == null) {
            throw new RuntimeException("redisConfig is null on pop redis");
        }
        return redisConfig.getRedisResource(String.valueOf(dbIndex));
    }

    @Override
    public String get(String key) {
        String value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.get(key);
        }
        return value;
    }

    @Override
    public List<String> mget(String... keys) {
        List<String> value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.mget(keys);
        }
        return value;
    }

    @Override
    public long del(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.del(key);
        }
    }

    @Override
    public String rename(String oldKey, String newKey) {
        String rtn = null;
        try (Jedis jedis = getJedis()) {
            if (jedis.exists(oldKey)) {
                rtn = jedis.rename(oldKey, newKey);
            }
        }
        return rtn;
    }

    @Override
    public String set(String key, String value) {
        String ok = null;
        try (Jedis jedis = getJedis()) {
            ok = jedis.set(key, value);
        }
        return ok;
    }

    @Override
    public String set(byte[] key, byte[] value) {
        String ok = null;
        try (Jedis jedis = getJedis()) {
            ok = jedis.set(key, value);
        }
        return ok;
    }

    @Override
    public String set(String key, String value, EXPX expx, long exp) {
        String ok = null;
        try (Jedis jedis = getJedis()) {
            ok = jedis.set(key, value, setParams(expx, exp));
        }
        return ok;
    }

    @Override
    public String set(byte[] key, byte[] value, EXPX expx, long exp) {
        String ok = null;
        try (Jedis jedis = getJedis()) {
            ok = jedis.set(key, value, setParams(expx, exp));
        }
        return ok;
    }

    private static SetParams setParams(EXPX expx, long exp) {
        SetParams setParams = new SetParams();
        setParams.nx();
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
        SetParams params = setParams(EXPX.SECONDS, exp.getTime());
        try (Jedis jedis = getJedis()) {
            return jedis.set(key, value, params);
        }
    }

    @Override
    public String set(byte[] key, byte[] value, Expire exp) {
        if (exp == null){
            return "not ok";
        }
        return set(key, value, EXPX.SECONDS, (long) exp.getTime());
    }

    @Override
    public String set(String key, String value, boolean ex, int time, boolean nx) {
        SetParams params = new SetParams();
        if (nx) {
            params.nx();
        } else {
            params.xx();
        }

        if (ex) {
            params.ex((long) time);
        } else {
            params.px(time);
        }
        try (Jedis jedis = getJedis()) {
            return jedis.set(key, value, params);
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
        Long ok = null;
        try (Jedis jedis = getJedis()) {
            ok = jedis.expire(key, exp.getTime());
        }
        return ok;
    }

    @Override
    public Long expire(byte[] key, Expire exp) {
        Long ok = null;
        try (Jedis jedis = getJedis()) {
            ok = jedis.expire(key, exp.getTime());
        }
        return ok;
    }


    @Override
    public Long expire(String key, long seconds) {
        try (Jedis jedis = getJedis()) {
            return jedis.expire(key, seconds);
        }
    }

    @Override
    public Long expireAt(String key, long unixTime) {
        Long ok = null;
        try (Jedis jedis = getJedis()) {
            ok = jedis.expireAt(key, unixTime);
        }
        return ok;
    }

    @Override
    public Long expireAt(byte[] key, long unixTime) {
        Long ok = null;
        try (Jedis jedis = getJedis()) {
            ok = jedis.expireAt(key, unixTime);
        }
        return ok;
    }

    @Override
    public Long ttl(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.ttl(key);
        }
    }

    @Override
    public Long incr(String key) {
        Long val = null;
        try (Jedis jedis = getJedis()) {
            val = jedis.incr(key);
        }
        return val;
    }


    @Override
    public Long incrby(String key, long increment) {
        Long val = null;
        try (Jedis jedis = getJedis()) {
            val = jedis.incrBy(key, increment);
        }
        return val;
    }


    @Override
    public Set<String> keys(String pattern) {
        Set<String> sets = null;
        try (Jedis jedis = getJedis()) {
            sets = jedis.keys(pattern);
        }
        return sets;
    }


    @Override
    public ScanResult<String> scan(String cursor) {
        ScanResult<String> result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.scan(cursor);
        }
        return result;
    }

    @Override
    public ScanResult<byte[]> scan(byte[] cursor) {
        ScanResult<byte[]> result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.scan(cursor);
        }
        return result;
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
        String value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.hget(key, field);
        }
        return value;

    }

    @Override
    public byte[] hget(byte[] key, byte[] field) {
        byte[] value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.hget(key, field);
        }
        return value;
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        Map<String, String> map;
        try (Jedis jedis = getJedis()) {
            map = jedis.hgetAll(key);
        }
        return map;
    }

    @Override
    public Long hdel(String key, String field) {
        Long value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.hdel(key, field);
        }
        return value;

    }

    @Override
    public Long hdel(byte[] key, byte[] field) {
        Long value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.hdel(key, field);
        }
        return value;

    }

    @Override
    public Boolean hexists(byte[] key, byte[] field) {
        Boolean value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.hexists(key, field);
        }
        return value;

    }

    @Override
    public Boolean hexists(String key, String field) {
        Boolean value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.hexists(key, field);
        }
        return value;

    }

    @Override
    public Set<String> hkeys(String key) {
        Set<String> value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.hkeys(key);
        }
        return value;
    }

    @Override
    public Set<byte[]> hkeys(byte[] key) {
        Set<byte[]> value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.keys(key);
        }
        return value;
    }

    @Override
    public Long hlen(String key) {
        Long value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.hlen(key);
        }
        return value;
    }

    @Override
    public Long hlen(byte[] key) {
        Long value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.hlen(key);
        }
        return value;
    }

    @Override
    public Long lpush(String key, String... values) {
        Long value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.lpush(key, values);
        }
        return value;
    }

    @Override
    public Long lpush(byte[] key, byte[]... values) {
        Long value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.lpush(key, values);
        }
        return value;
    }

    @Override
    public String lpop(String key) {
        String value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.lpop(key);
        }
        return value;
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        List<String> value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.lrange(key, start, end);
        }
        return value;
    }

    @Override
    public List<byte[]> lrange(byte[] key, long start, long end) {
        List<byte[]> value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.lrange(key, start, end);
        }
        return value;
    }

    @Override
    public Long llen(String key) {
        Long value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.llen(key);
        }
        return value;
    }

    @Override
    public byte[] lpop(byte[] key) {
        byte[] value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.lpop(key);
        }
        return value;
    }

    @Override
    public Long rpush(String key, String... values) {
        Long value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.rpush(key, values);
        }
        return value;
    }

    @Override
    public String rpop(String key) {
        String value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.rpop(key);
        }
        return value;
    }

    @Override
    public Long lrem(String key, long count, String value) {
        Long result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.lrem(key, count, value);
        }
        return result;
    }

    @Override
    public Long lrem(byte[] key, long count, byte[] value) {
        Long result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.lrem(key, count, value);
        }
        return result;
    }


    @Override
    public String ltrim(String key, long start, long end) {
        String result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.ltrim(key, start, end);
        }
        return result;
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
        Long opCount = null;
        try (Jedis jedis = getJedis()) {
            opCount = jedis.sadd(key, members);
        }
        return opCount;
    }

    @Override
    public Long sadd(byte[] key, byte[]... members) {
        Long opCount = null;
        try (Jedis jedis = getJedis()) {
            opCount = jedis.sadd(key, members);
        }
        return opCount;
    }

    @Override
    public Long scard(String key) {
        Long scard = null;
        try (Jedis jedis = getJedis()) {
            scard = jedis.scard(key);
        }
        return scard;
    }

    @Override
    public Long scard(byte[] key) {
        Long scard = null;
        try (Jedis jedis = getJedis()) {
            scard = jedis.scard(key);
        }
        return scard;
    }

    @Override
    public Long srem(String key, String... members) {
        Long opCount = null;
        try (Jedis jedis = getJedis()) {
            opCount = jedis.srem(key, members);
        }
        return opCount;
    }

    @Override
    public Long srem(byte[] key, byte[]... members) {
        Long opCount = null;
        try (Jedis jedis = getJedis()) {
            opCount = jedis.srem(key, members);
        }
        return opCount;
    }

    @Override
    public Set<String> smembers(String key) {
        Set<String> memberSet = null;
        try (Jedis jedis = getJedis()) {
            memberSet = jedis.smembers(key);
        }
        return memberSet;
    }

    @Override
    public Boolean sismember(String key, String member) {
        Boolean isMember = null;
        try (Jedis jedis = getJedis()) {
            isMember = jedis.sismember(key, member);
        }
        return isMember;
    }

    @Override
    public Boolean sismember(byte[] key, byte[] member) {
        Boolean isMember = null;
        try (Jedis jedis = getJedis()) {
            isMember = jedis.sismember(key, member);
        }
        return isMember;
    }

    @Override
    public String spop(String key) {
        String value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.spop(key);
        }
        return value;
    }

    @Override
    public byte[] spop(byte[] key) {
        byte[] value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.spop(key);
        }
        return value;
    }

    @Override
    public Long zadd(String key, double score, String member) {
        Long opCount = null;
        try (Jedis jedis = getJedis()) {
            opCount = jedis.zadd(key, score, member);
        }
        return opCount;
    }

    @Override
    public Long zadd(byte[] key, double score, byte[] member) {
        Long opCount = null;
        try (Jedis jedis = getJedis()) {
            opCount = jedis.zadd(key, score, member);
        }
        return opCount;
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        try (Jedis jedis = getJedis()) {
            return jedis.zadd(key, scoreMembers);
        }
    }

    @Override
    public Long zrem(String key, String member) {
        Long opCount = null;
        try (Jedis jedis = getJedis()) {
            opCount = jedis.zrem(key, member);
        }
        return opCount;
    }

    @Override
    public Long zrem(byte[] key, byte[] member) {
        Long opCount = null;
        try (Jedis jedis = getJedis()) {
            opCount = jedis.zrem(key, member);
        }
        return opCount;
    }

    @Override
    public Long zRemRangeByRank(String key, long start, long end) {
        Long opCount = null;
        try (Jedis jedis = getJedis()) {
            opCount = jedis.zremrangeByRank(key, start, end);
        }
        return opCount;
    }

    @Override
    public Long zRemRangeByRank(byte[] key, long start, long end) {
        Long opCount = null;
        try (Jedis jedis = getJedis()) {
            opCount = jedis.zremrangeByRank(key, start, end);
        }
        return opCount;
    }

    @Override
    public Long zremrangeByScore(String key, long min, long max) {
        Long replyValue;
        try (Jedis jedis = getJedis()) {
            replyValue = jedis.zremrangeByScore(key, min, max);
        }
        return replyValue;
    }

    @Override
    public Long zrank(String key, String member) {
        Long rank = null;
        try (Jedis jedis = getJedis()) {
            rank = jedis.zrank(key, member);
        }
        return rank;
    }

    @Override
    public Long zrank(byte[] key, byte[] member) {
        Long rank = null;
        try (Jedis jedis = getJedis()) {
            rank = jedis.zrank(key, member);
        }
        return rank;
    }

    @Override
    public Long zrevrank(String key, String member) {
        Long rank = null;
        try (Jedis jedis = getJedis()) {
            rank = jedis.zrevrank(key, member);
        }
        return rank;
    }

    @Override
    public Long zrevrank(byte[] key, byte[] member) {
        Long rank = null;
        try (Jedis jedis = getJedis()) {
            rank = jedis.zrevrank(key, member);
        }
        return rank;
    }

    @Override
    public Set<String> zrange(String key, long min, long max) {
        Set<String> memSet = null;
        try (Jedis jedis = getJedis()) {
            memSet = jedis.zrange(key, min, max);
        }
        return memSet;
    }

    @Override
    public Set<byte[]> zrange(byte[] key, long min, long max) {
        Set<byte[]> memSet = null;
        try (Jedis jedis = getJedis()) {
            memSet = jedis.zrange(key, min, max);
        }
        return memSet;
    }

    @Override
    public Set<String> zrevrange(String key, long min, long max) {
        Set<String> memSet = null;
        try (Jedis jedis = getJedis()) {
            memSet = jedis.zrevrange(key, min, max);
        }
        return memSet;
    }

    @Override
    public Set<byte[]> zrevrange(byte[] key, long min, long max) {
        Set<byte[]> memSet = null;
        try (Jedis jedis = getJedis()) {
            memSet = jedis.zrevrange(key, min, max);
        }
        return memSet;
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long min, long max) {
        Set<Tuple> memSet = null;
        try (Jedis jedis = getJedis()) {
            memSet = jedis.zrangeWithScores(key, min, max);
        }
        return memSet;
    }

    @Override
    public Set<Tuple> zrangeWithScores(byte[] key, long min, long max) {
        Set<Tuple> memSet = null;
        try (Jedis jedis = getJedis()) {
            memSet = jedis.zrangeWithScores(key, min, max);
        }
        return memSet;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, long min, long max) {
        Set<Tuple> memSet = null;
        try (Jedis jedis = getJedis()) {
            memSet = jedis.zrangeByScoreWithScores(key, min, max);
        }
        return memSet;
    }

    @Override
    public Set<String> zrevrangebyscore(String key, double max, double min, int offset, int count) {
        Set<String> memSet = null;
        try (Jedis jedis = getJedis()) {
            memSet = jedis.zrevrangeByScore(key, max, min, offset, count);
        }
        return memSet;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        Set<Tuple> memSet = null;
        try (Jedis jedis = getJedis()) {
            memSet = jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        }
        return memSet;
    }

    @Override
    public Set<String> zrangeByScore(String key, long min, long max) {
        Set<String> valueSet;
        try (Jedis jedis = getJedis()) {
            valueSet = jedis.zrangeByScore(key, min, max);
        }
        return valueSet;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, long min, long max) {
        Set<Tuple> memSet = null;
        try (Jedis jedis = getJedis()) {
            memSet = jedis.zrangeByScoreWithScores(key, min, max);
        }
        return memSet;
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String key, long min, long max) {
        Set<Tuple> memSet = null;
        try (Jedis jedis = getJedis()) {
            memSet = jedis.zrevrangeWithScores(key, min, max);
        }
        return memSet;
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(byte[] key, long min, long max) {
        Set<Tuple> memSet = null;
        try (Jedis jedis = getJedis()) {
            memSet = jedis.zrevrangeWithScores(key, min, max);
        }
        return memSet;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, long min, long max) {
        Set<Tuple> memSet = null;
        try (Jedis jedis = getJedis()) {
            memSet = jedis.zrevrangeByScoreWithScores(key, min, max);
        }
        return memSet;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, long min, long max) {
        Set<Tuple> memSet = null;
        try (Jedis jedis = getJedis()) {
            memSet = jedis.zrevrangeByScoreWithScores(key, min, max);
        }
        return memSet;
    }

    @Override
    public Double zincrby(String key, double incrScore, String member) {
        Double newScore = null;
        try (Jedis jedis = getJedis()) {
            newScore = jedis.zincrby(key, incrScore, member);
        }
        return newScore;
    }

    @Override
    public Double zincrby(byte[] key, double incrScore, byte[] member) {
        Double newScore = null;
        try (Jedis jedis = getJedis()) {
            newScore = jedis.zincrby(key, incrScore, member);
        }
        return newScore;
    }

    @Override
    public Double zscore(String key, String member) {
        Double score = null;
        try (Jedis jedis = getJedis()) {
            score = jedis.zscore(key, member);
        }
        return score;
    }

    @Override
    public Double zscore(byte[] key, byte[] member) {
        Double score = null;
        try (Jedis jedis = getJedis()) {
            score = jedis.zscore(key, member);
        }
        return score;
    }

    @Override
    public Long zcard(String key) {
        Long count = null;
        try (Jedis jedis = getJedis()) {
            count = jedis.zcard(key);
        }
        return count;
    }

    @Override
    public Long zcard(byte[] key) {
        Long count = null;
        try (Jedis jedis = getJedis()) {
            count = jedis.zcard(key);
        }
        return count;
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
        Long result;
        try (Jedis jedis = getJedis()) {
            result = jedis.hincrBy(key, field, value);
        }
        return result;
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

    @Override
    public Long decr(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.decr(key);
        }
    }

}
