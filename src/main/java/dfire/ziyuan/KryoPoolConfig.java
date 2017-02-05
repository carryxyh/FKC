/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;
import dfire.ziyuan.exceptions.DefaultFKCExceptionHandler;
import dfire.ziyuan.exceptions.FKCExceptionHandler;
import dfire.ziyuan.poolobjfactory.KryoFactoryAdapter;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * KryoPoolConfig kryoPool的config,kryoPool有两种缓存实现方式分别为:1.队列 2.apache commons中pool的二次封装
 * kryo pool config 中部分对队列生效  部分对pool生效, 选择一种生成即可
 *
 * @author ziyuan
 * @since 2017-01-10
 */
public class KryoPoolConfig {

    private KryoPoolImplType kryoPoolImplType;

    public KryoPoolConfig(KryoPoolImplType kryoPoolImplType) {
        this.kryoPoolImplType = kryoPoolImplType;
    }

    /*------------------------------------------以下是对pool生效的参数----------------------------------------------*/

    /**
     * 默认先进后出
     */
    private boolean lifo = true;

    /**
     * 默认池内最大空闲对象
     */
    private int maxIdle = 30;

    /**
     * 默认池内最大数量
     */
    private int maxTotal = 60;

    /**
     * 默认最小空闲数量
     */
    private int minIdle = 5;

    /**
     * 每次eviction检查两个对象
     */
    private int numtestsPerEviction = 2;

    /**
     * 用光的情况下不阻塞
     */
    private boolean blockedWhenExhausted = false;

    /**
     * 5分钟检查一次整个池
     */
    private long timeBetweenEvictionRun = 5L * 60L * 1000L;

    /**
     * 获取对象时不检查
     */
    private boolean testOnBorrow = false;

    /**
     * 创建时不检查
     */
    private boolean testOnCreate = false;

    /**
     * 归还时不检查
     */
    private boolean testOnReturn = false;

    /**
     * 在Maintenance的时候检查是否有泄漏
     */
    private boolean removeAbandonedOnMaintenance = false;

    /**
     * borrow 的时候检查泄漏
     */
    private boolean removeAbandonedOnBorrow = false;

    /**
     * 如果一个对象borrow之后200秒还没有返还给pool，认为是泄漏的对象
     */
    private int removeAbandonedTimeout = 200;

    /**
     * 默认使用适配器
     */
    private PooledObjectFactory<Kryo> pooledObjectFactory = new KryoFactoryAdapter();

    /*------------------------------------------以下是对队列生效的参数----------------------------------------------*/

    /**
     * 是否使用软引用队列
     */
    private boolean useSoftRefQueue = false;

    /**
     * 缓存的队列
     */
    private Queue<Kryo> cacheQueue = new ConcurrentLinkedQueue();

    /**
     * kryo factory
     */
    private KryoFactory kryoFactory = new KryoFactory() {
        @Override
        public Kryo create() {
            return new Kryo();
        }
    };

    private FKCExceptionHandler exceptionHandler = new DefaultFKCExceptionHandler();

    public GenericObjectPoolConfig getGenericPoolConfig() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setLifo(this.lifo);
        genericObjectPoolConfig.setMinIdle(this.minIdle);
        genericObjectPoolConfig.setMaxIdle(this.maxIdle);
        genericObjectPoolConfig.setMaxTotal(this.maxTotal);
        genericObjectPoolConfig.setNumTestsPerEvictionRun(this.numtestsPerEviction);
        genericObjectPoolConfig.setBlockWhenExhausted(this.blockedWhenExhausted);
        genericObjectPoolConfig.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRun);
        genericObjectPoolConfig.setTestOnBorrow(this.testOnBorrow);
        genericObjectPoolConfig.setTestOnCreate(this.testOnCreate);
        genericObjectPoolConfig.setTestOnReturn(this.testOnReturn);
        return genericObjectPoolConfig;
    }

    public AbandonedConfig getAbandonedConfig() {
        AbandonedConfig abandonedConfig = new AbandonedConfig();
        abandonedConfig.setRemoveAbandonedOnBorrow(this.removeAbandonedOnBorrow);
        abandonedConfig.setRemoveAbandonedTimeout(this.removeAbandonedTimeout);
        abandonedConfig.setRemoveAbandonedOnMaintenance(this.removeAbandonedOnMaintenance);
        return abandonedConfig;
    }

    public KryoPoolImplType getKryoPoolImplType() {
        return kryoPoolImplType;
    }

    public void setKryoPoolImplType(KryoPoolImplType kryoPoolImplType) {
        this.kryoPoolImplType = kryoPoolImplType;
    }

    public void setLifo(boolean lifo) {
        this.lifo = lifo;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public void setNumtestsPerEviction(int numtestsPerEviction) {
        this.numtestsPerEviction = numtestsPerEviction;
    }

    public void setBlockedWhenExhausted(boolean blockedWhenExhausted) {
        this.blockedWhenExhausted = blockedWhenExhausted;
    }

    public void setTimeBetweenEvictionRun(long timeBetweenEvictionRun) {
        this.timeBetweenEvictionRun = timeBetweenEvictionRun;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public void setTestOnCreate(boolean testOnCreate) {
        this.testOnCreate = testOnCreate;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public void setRemoveAbandonedOnMaintenance(boolean removeAbandonedOnMaintenance) {
        this.removeAbandonedOnMaintenance = removeAbandonedOnMaintenance;
    }

    public void setRemoveAbandonedOnBorrow(boolean removeAbandonedOnBorrow) {
        this.removeAbandonedOnBorrow = removeAbandonedOnBorrow;
    }

    public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }

    public void setPooledObjectFactory(PooledObjectFactory<Kryo> pooledObjectFactory) {
        this.pooledObjectFactory = pooledObjectFactory;
    }

    public void setUseSoftRefQueue(boolean useSoftRefQueue) {
        this.useSoftRefQueue = useSoftRefQueue;
    }

    public void setCacheQueue(Queue<Kryo> cacheQueue) {
        this.cacheQueue = cacheQueue;
    }

    public void setKryoFactory(KryoFactory kryoFactory) {
        this.kryoFactory = kryoFactory;
    }

    public void setExceptionHandler(FKCExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public FKCExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public PooledObjectFactory<Kryo> getPooledObjectFactory() {
        return pooledObjectFactory;
    }

    public KryoFactory getKryoFactory() {
        return kryoFactory;
    }

    public Queue<Kryo> getCacheQueue() {
        return cacheQueue;
    }

    public boolean isUseSoftRefQueue() {
        return useSoftRefQueue;
    }
}
