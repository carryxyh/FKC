/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import dfire.ziyuan.exceptions.DefaultFKCExceptionHandler;
import dfire.ziyuan.exceptions.FKCException;
import dfire.ziyuan.exceptions.FKCExceptionHandler;
import dfire.ziyuan.pool.DefaultIOPoolConfig;
import dfire.ziyuan.pool.KryoPool;
import dfire.ziyuan.poolobjfactory.StreamHolderFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    private FKCExceptionHandler exceptionHandler;

    private StreamHolderFactory streamHolderFactory;

    private GenericObjectPool<StreamHolder> holderPool;

    private GenericObjectPoolConfig poolConfig;

    private AbandonedConfig abandonedConfig;

    private KryoPool kryoPool;

    private KryoPoolConfig kryoPoolConfig;

    public DefaultIncubator() {
        init();
    }

    /**
     * 进行初始化操作
     */
    private void init() {
        if (poolConfig == null) {
            poolConfig = DefaultIOPoolConfig.getConfig();
        }
        if (abandonedConfig == null) {
            abandonedConfig = new AbandonedConfig();
        }
        if (exceptionHandler == null) {
            exceptionHandler = new DefaultFKCExceptionHandler();
        }
        if (kryoPoolConfig == null) {
            //初始化相关池对象
            kryoPool = new KryoPool.Builder(new KryoFactory() {
                @Override
                public Kryo create() {
                    return new Kryo();
                }
            }).isQueueSoftRef(true).build();
        } else {
            kryoPool = new KryoPool.Builder(new KryoFactory() {
                @Override
                public Kryo create() {
                    return new Kryo();
                }
            }).isQueueSoftRef(kryoPoolConfig.isUseSoftRefQueue()).queue(kryoPoolConfig.getCacheQueue()).build();
        }
        streamHolderFactory = new StreamHolderFactory(exceptionHandler);
        holderPool = new GenericObjectPool<StreamHolder>(streamHolderFactory, poolConfig, abandonedConfig);

        //初始化的时候先设置好钩子,防止使用的时候忘记关闭
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                doShutDown();
            }
        }));
    }

    @Override
    public T born(T template) throws FKCException {
        Kryo kryo = null;
        StreamHolder holder = null;
        try {
            kryo = kryoPool.borrowOne();
            holder = holderPool.borrowObject();
            ObjectOutputStream oos = holder.getOos();
            Output output = new Output(oos);
            kryo.writeObject(output, template);
            ObjectInputStream ois = holder.getOis();
            Input input = new Input(ois);
            T t = (T) kryo.readObject(input, template.getClass());
            return t;
        } catch (Exception e) {
            throw new FKCException(e.getMessage(), System.currentTimeMillis());
        } finally {
            if (kryo != null) {
                kryoPool.returnOne(kryo);
            }
            if (holder != null) {
                holderPool.returnObject(holder);
            }
        }
    }

    @Override
    public void shutdown() {
        doShutDown();
    }

    @Override
    public void setIncubatorCfg(IncubatorConfig config) {
        this.exceptionHandler = config.getExceptionHandler();
        this.poolConfig = config.getGenericObjectPoolConfig();
        this.abandonedConfig = config.getAbandonedConfig();
        this.kryoPoolConfig = config.getKryoPoolConfig();
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
}
