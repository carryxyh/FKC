/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan.pool;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoCallback;
import dfire.ziyuan.exceptions.FKCExceptionHandler;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * KryoPoolCommonsImpl
 *
 * @author ziyuan
 * @since 2017-02-05
 */
public class KryoPoolCommonsImpl implements KryoPool {

    public KryoPoolCommonsImpl(GenericObjectPoolConfig genericObjectPoolConfig, PooledObjectFactory<Kryo> pooledObjectFactory, AbandonedConfig abandonedConfig, FKCExceptionHandler fkcExceptionHandler) {
        this.kryoPool = new GenericObjectPool<Kryo>(pooledObjectFactory, genericObjectPoolConfig, abandonedConfig);
        this.fkcExceptionHandler = fkcExceptionHandler;
    }

    private FKCExceptionHandler fkcExceptionHandler;

    private GenericObjectPool<Kryo> kryoPool;

    @Override
    public Kryo borrowOne() {
        try {
            return kryoPool.borrowObject();
        } catch (Exception e) {
            fkcExceptionHandler.dealException(e);
        }
        return null;
    }

    @Override
    public void returnOne(Kryo k) {
        kryoPool.returnObject(k);
    }

    @Override
    public <T> T run(KryoCallback<T> callback) {
        try {
            Kryo kryo = kryoPool.borrowObject();
            callback.execute(kryo);
        } catch (Exception e) {
            fkcExceptionHandler.dealException(e);
        }
        return null;
    }

    @Override
    public void close() {
        kryoPool.close();
    }
}
