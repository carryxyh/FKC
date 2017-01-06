/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan.poolobjfactory;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;

import java.io.ObjectOutputStream;

/**
 * ObjOutputStreamFactory
 *
 * @author ziyuan
 * @since 2017-01-06
 */
public class ObjOutputStreamFactory<T> implements PooledObjectFactory<ObjectOutputStream> {

    @Override
    public PooledObject<ObjectOutputStream> makeObject() throws Exception {
        return null;
    }

    @Override
    public void destroyObject(PooledObject<ObjectOutputStream> p) throws Exception {

    }

    @Override
    public boolean validateObject(PooledObject<ObjectOutputStream> p) {
        return false;
    }

    @Override
    public void activateObject(PooledObject<ObjectOutputStream> p) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<ObjectOutputStream> p) throws Exception {

    }
}
