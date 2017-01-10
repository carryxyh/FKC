/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan.pool;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoCallback;
import com.esotericsoftware.kryo.pool.KryoFactory;

import java.util.Queue;

/**
 * KryoPoolQueueImpl
 *
 * @author ziyuan
 * @since 2017-01-09
 */
public class KryoPoolQueueImpl implements KryoPool {

    private final Queue<Kryo> queue;

    private final KryoFactory factory;

    public KryoPoolQueueImpl(Queue<Kryo> queue, KryoFactory factory) {
        this.queue = queue;
        this.factory = factory;
    }

    @Override
    public Kryo borrowOne() {
        Kryo res;
        return (res = this.queue.poll()) != null ? res : this.factory.create();
    }

    @Override
    public void returnOne(Kryo k) {
        this.queue.offer(k);
    }

    @Override
    public <T> T run(KryoCallback<T> callback) {
        Kryo kryo = this.borrowOne();

        T t;
        try {
            t = callback.execute(kryo);
        } finally {
            this.returnOne(kryo);
        }
        return t;
    }

    @Override
    public void close() {
        this.queue.clear();
    }
}
