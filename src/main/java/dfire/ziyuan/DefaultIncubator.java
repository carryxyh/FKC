/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan;

import com.esotericsoftware.kryo.Kryo;
import dfire.ziyuan.exceptions.FKCException;
import dfire.ziyuan.pool.KryoPool;

import java.util.concurrent.locks.ReentrantLock;

/**
 * DefaultIncubator 默认的incubator,使用spi机制创建
 *
 * @author ziyuan
 * @since 2017-01-06
 */
public class DefaultIncubator<T> implements Incubator<T> {

    /**
     * 是否是关闭了的
     */
    private boolean isClosed = false;

    private final ReentrantLock lock = new ReentrantLock(false);

    private KryoPool kryoPool;

    /**
     * 默认使用比较轻量级的队列的实现方式
     */
    private KryoPoolConfig kryoPoolConfig = new KryoPoolConfig(KryoPoolImplType.QUEUE);

    public DefaultIncubator() {
        init();
    }

    /**
     * 进行初始化操作
     */
    private void init() {
        //初始化的时候先设置好钩子,防止使用的时候忘记关闭
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                doShutDown();
            }
        }));

        KryoPoolImplType kryoPoolImplType = this.kryoPoolConfig.getKryoPoolImplType();
        if (kryoPoolImplType == KryoPoolImplType.APACHE_COMMONS) {
            //if apache commons
            this.kryoPool = new KryoPool.CommonsBuilder(this.kryoPoolConfig.getPooledObjectFactory())
                    .abandonedStrategy(this.kryoPoolConfig.getAbandonedConfig())
                    .config(this.kryoPoolConfig.getGenericPoolConfig())
                    .exceptionHandler(this.kryoPoolConfig.getExceptionHandler())
                    .build();
        } else {
            //if queue
            this.kryoPool = new KryoPool.QueueBuilder(this.kryoPoolConfig.getKryoFactory())
                    .queue(this.kryoPoolConfig.getCacheQueue())
                    .isQueueSoftRef(this.kryoPoolConfig.isUseSoftRefQueue())
                    .build();
        }
    }

    @Override
    public T born(T template) throws FKCException {
        Kryo kryo = kryoPool.borrowOne();
        return kryo.copy(template);
    }

    @Override
    public void shutdown() {
        doShutDown();
    }

    /**
     * 执行停止操作
     */
    private void doShutDown() {
        lock.lock();
        try {
            if (isClosed) {
                return;
            }
            if (this.kryoPool != null) {
                kryoPool.close();
            }
            isClosed = true;
        } finally {
            lock.unlock();
        }
    }

    public void setKryoPoolConfig(KryoPoolConfig kryoPoolConfig) {
        this.kryoPoolConfig = kryoPoolConfig;
    }
}
