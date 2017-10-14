package com.twodfire.redis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Protocol;

public class RedisSentinelService extends RedisBaseService {

    private String sentinels;

    private String masterName;

    public void init() {

        Set<String> sentinelset = new HashSet<>(Arrays.asList(sentinels.split(",")));

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        config.setMaxIdle( this.getMaxIdle() );
        config.setMinIdle( this.getMinIdle() );
        config.setMaxTotal( this.getMaxTotal() );

        config.setMaxWaitMillis( this.getMaxWaitMillis() );  //表示当borrow一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException

        config.setTestOnBorrow( this.isTestOnBorrow() );  //获取连接时关闭触发ping
        config.setTestOnReturn( this.isTestOnReturn() );  //释放连接时关闭触发ping

        config.setTestWhileIdle( this.isTestWhileIdle() );  //表示有一个idle object evitor线程对idle object进行扫描，如果validate失败，此object会被从pool中drop掉
        config.setTimeBetweenEvictionRunsMillis( this.getTimeBetweenEvictionRunsMillis() );   //每隔30秒定期检查空闲连接
        config.setNumTestsPerEvictionRun( this.getNumTestsPerEvictionRun() );  // 空闲连接扫描时，每次最多扫描的连接数, -1 全部扫描
        config.setMinEvictableIdleTimeMillis( this.getMinEvictableIdleTimeMillis() );    //连接在池中保持空闲而不被空闲连接回收器线程回收的最小时间值

        pool = new JedisSentinelPool(masterName, sentinelset, config, Protocol.DEFAULT_TIMEOUT, null, database);

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                pool.destroy();
            }
        });
    }

    /**
     * @param sentinels the sentinels to set
     */
    public void setSentinels(String sentinels) {
        this.sentinels = sentinels;
    }

    /**
     * @param masterName the masterName to set
     */
    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public Jedis getResource() {
        return pool.getResource();
    }

}
