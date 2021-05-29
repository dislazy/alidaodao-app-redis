package com.alidaodao.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jack
 * redis client
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {
    /**
     * need open
     */
    private boolean enable;
    /**
     * redis host
     */
    private String host;
    /**
     * redis port
     */
    private int port;

    /**
     * redis password
     */
    private String password;
    /**
     * need init redis index db,example: 1,2,3,4,5
     */
    private Integer[] indexes;
    /**
     * default index
     */
    private int defaultIndex;


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

    public Integer[] getIndexes() {
        return indexes;
    }

    public void setIndexes(Integer[] indexes) {
        this.indexes = indexes;
    }

    public int getDefaultIndex() {
        return defaultIndex;
    }

    public void setDefaultIndex(int defaultIndex) {
        this.defaultIndex = defaultIndex;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
