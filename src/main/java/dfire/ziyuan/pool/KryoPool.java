/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan.pool;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoCallback;
import com.esotericsoftware.kryo.pool.KryoFactory;
import dfire.ziyuan.exceptions.FKCExceptionHandler;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * KryoPool
 *
 * @author ziyuan
 * @since 2017-01-09
 */
public interface KryoPool {

    /**
     * 从池中获取一个kryo
     *
     * @return kryo
     */
    Kryo borrowOne();

    /**
     * 归还一个kryo
     *
     * @param k kryo
     */
    void returnOne(Kryo k);

    /**
     * 从pool中获取一个kryo后马上执行callback
     *
     * @param callback callback
     * @param <T>
     * @return 返回值
     */
    <T> T run(KryoCallback<T> callback);

    /**
     * 关闭这个kryo池
     */
    void close();

    class QueueBuilder {

        /**
         * kryofactory
         */
        private final KryoFactory factory;

        /**
         * queue,默认ConcurrentLinkedQueue
         */
        private Queue<Kryo> queue = new ConcurrentLinkedQueue<Kryo>();

        /**
         * 是否使用soft ref queue
         */
        private boolean isQueueSoftRef;

        public QueueBuilder(KryoFactory factory) {
            if (factory == null) {
                throw new IllegalArgumentException("factory must not be null");
            }
            this.factory = factory;
        }

        public QueueBuilder queue(Queue<Kryo> queue) {
            if (queue == null) {
                throw new IllegalArgumentException("queue must not be null");
            }
            this.queue = queue;
            return this;
        }

        public QueueBuilder isQueueSoftRef(boolean isQueueSoftRef) {
            this.isQueueSoftRef = isQueueSoftRef;
            return this;
        }

        public KryoPool build() {
            Queue<Kryo> q = isQueueSoftRef ? new SoftReferenceQueue(queue) : queue;
            return new KryoPoolQueueImpl(q, this.factory);
        }

        @Override
        public String toString() {
            return getClass().getName() + "[queue.class=" + queue.getClass() + ", softReferences=" + isQueueSoftRef + "]";
        }
    }

    class CommonsBuilder {

        private final PooledObjectFactory<Kryo> pooledObjectFactory;

        private GenericObjectPoolConfig genericObjectPoolConfig;

        private AbandonedConfig abandonedConfig;

        private FKCExceptionHandler fkcExceptionHandler;

        public CommonsBuilder(PooledObjectFactory<Kryo> kryoPooledObjectFactory) {
            if (kryoPooledObjectFactory == null) {
                throw new IllegalArgumentException("pooledObjectFactory must not be null");
            }
            this.pooledObjectFactory = kryoPooledObjectFactory;
        }

        public CommonsBuilder config(GenericObjectPoolConfig genericObjectPoolConfig) {
            this.genericObjectPoolConfig = genericObjectPoolConfig;
            return this;
        }

        public CommonsBuilder abandonedStrategy(AbandonedConfig abandonedConfig) {
            this.abandonedConfig = abandonedConfig;
            return this;
        }

        public CommonsBuilder exceptionHandler(FKCExceptionHandler fkcExceptionHandler) {
            this.fkcExceptionHandler = fkcExceptionHandler;
            return this;
        }

        public KryoPool build() {
            return new KryoPoolCommonsImpl(this.genericObjectPoolConfig, this.pooledObjectFactory, this.abandonedConfig, this.fkcExceptionHandler);
        }
    }
}
