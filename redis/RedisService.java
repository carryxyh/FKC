package com.twodfire.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

public class RedisService extends RedisBaseService {

    private String ip;

    private int    port;

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void init() {

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        config.setMaxIdle(this.getMaxIdle());
        config.setMinIdle(this.getMinIdle());
        config.setMaxTotal(this.getMaxTotal());
        config.setMaxWaitMillis(this.getMaxWaitMillis());
        config.setTestOnBorrow(this.isTestOnBorrow());

        pool = new JedisPool(config, ip, port, Protocol.DEFAULT_TIMEOUT, null, database);

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                pool.destroy();
            }
        });
    }

    public Jedis getResource() {
        return pool.getResource();
    }

}
