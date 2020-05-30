package com.alidaodao.app;

import com.alidaodao.app.service.RedisService;
import com.alidaodao.app.config.RedisConfig;
import com.alidaodao.app.service.impl.RedisCache;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class RedisClient {

    //redis 多库map
    private Map<Integer, RedisService> map;

    // 默认库索引
    private int defaultIndex = -1;

    private boolean checkDbIndex(int index) {
        return index >= 0 && index <= 254;
    }

    /**
     *
     * @param redisConfig redisConfig
     * @param assignedDbIndexes dbIndex
     */
    public RedisClient(RedisConfig redisConfig, Set<Integer> assignedDbIndexes) {
        map = new ConcurrentHashMap<>(assignedDbIndexes.size());
        assignedDbIndexes.stream()
                .filter(this::checkDbIndex)
                .forEach(i -> map.put(i, new RedisCache(i, redisConfig)));
    }

    public void setDefaultIndex(int defaultIndex) {
        if (!map.containsKey(defaultIndex)) {
            throw new IllegalArgumentException("incorrect default db index");
        }
        this.defaultIndex = defaultIndex;
    }

    public Set<Integer> getAssignedIndexes() {
        return map.keySet();
    }

    public RedisService get(int index) {
        if (!checkDbIndex(index) || !map.containsKey(index)) {
            throw new IllegalArgumentException("incorrect db index");
        }
        return map.get(index);
    }

    public RedisService getByDefault() {
        if (defaultIndex == -1) {
            throw new IllegalArgumentException("default db index not configured");
        }
        return map.get(defaultIndex);
    }

}
