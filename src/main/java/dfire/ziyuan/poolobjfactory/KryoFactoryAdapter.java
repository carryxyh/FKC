/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan.poolobjfactory;

import com.esotericsoftware.kryo.Kryo;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * KryoFactoryAdapter 如果有需要可以继承该Adapter进行相应的重写,这里只提供了一个默认的实现方式
 *
 * @author ziyuan
 * @since 2017-02-05
 */
public class KryoFactoryAdapter implements PooledObjectFactory<Kryo> {

    @Override
    public PooledObject<Kryo> makeObject() throws Exception {
        Kryo kryo = new Kryo();
        return new DefaultPooledObject<>(kryo);
    }

    @Override
    public void destroyObject(PooledObject<Kryo> p) throws Exception {
    }

    @Override
    public boolean validateObject(PooledObject<Kryo> p) {
        return false;
    }

    @Override
    public void activateObject(PooledObject<Kryo> p) throws Exception {
    }

    @Override
    public void passivateObject(PooledObject<Kryo> p) throws Exception {
    }
}
