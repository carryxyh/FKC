/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan.poolobjfactory;

import com.esotericsoftware.kryo.Kryo;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;

/**
 * KryoFactory
 *
 * @author ziyuan
 * @since 2017-01-06
 */
public class KryoFactory implements PooledObjectFactory<Kryo> {

    @Override
    public PooledObject<Kryo> makeObject() throws Exception {
        return null;
    }

    @Override
    public void destroyObject(PooledObject<Kryo> kryo) throws Exception {

    }

    @Override
    public boolean validateObject(PooledObject<Kryo> kryo) {
        return false;
    }

    @Override
    public void activateObject(PooledObject<Kryo> kryo) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<Kryo> kryo) throws Exception {

    }
}
