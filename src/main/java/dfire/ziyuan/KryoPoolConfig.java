/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan;

import com.esotericsoftware.kryo.Kryo;

import java.util.Queue;

/**
 * KryoPoolConfig
 *
 * @author ziyuan
 * @since 2017-01-10
 */
public class KryoPoolConfig {

    /**
     * 是否使用软引用队列
     */
    private boolean useSoftRefQueue;

    /**
     * 缓存的队列
     */
    private Queue<Kryo> cacheQueue;

    public boolean isUseSoftRefQueue() {
        return useSoftRefQueue;
    }

    public void setUseSoftRefQueue(boolean useSoftRefQueue) {
        this.useSoftRefQueue = useSoftRefQueue;
    }

    public Queue<Kryo> getCacheQueue() {
        return cacheQueue;
    }

    public void setCacheQueue(Queue<Kryo> cacheQueue) {
        this.cacheQueue = cacheQueue;
    }
}
