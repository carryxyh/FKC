/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan.poolobjfactory;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;

import java.io.ObjectInputStream;

/**
 * ObjInputStreamFactory
 *
 * @author ziyuan
 * @since 2017-01-06
 */
public class ObjInputStreamFactory implements PooledObjectFactory<ObjectInputStream> {

    @Override
    public PooledObject<ObjectInputStream> makeObject() throws Exception {
        return null;
    }

    @Override
    public void destroyObject(PooledObject<ObjectInputStream> p) throws Exception {

    }

    @Override
    public boolean validateObject(PooledObject<ObjectInputStream> p) {
        return false;
    }

    @Override
    public void activateObject(PooledObject<ObjectInputStream> p) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<ObjectInputStream> p) throws Exception {

    }
}
