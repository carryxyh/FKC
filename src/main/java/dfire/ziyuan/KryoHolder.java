/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan;

import com.esotericsoftware.kryo.Kryo;
import dfire.ziyuan.pool.KryoPool;

/**
 * KryoHolder
 *
 * @author ziyuan
 * @since 2017-01-10
 */
public class KryoHolder {

    private static final ThreadLocal<Kryo> localKryo = new ThreadLocal<>();

    private KryoPool kryoPool;

    public KryoHolder(KryoPool kryoPool) {
        this.kryoPool = kryoPool;
    }

    public Kryo get() {
        Kryo k = localKryo.get();
        if (k == null) {
            k = kryoPool.borrowOne();
            localKryo.set(k);
        }
        return k;
    }

    public void release() {
        localKryo.remove();
    }
}
