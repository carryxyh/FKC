/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan.poolobjfactory;

import dfire.ziyuan.StreamHolder;
import dfire.ziyuan.exceptions.FKCExceptionHandler;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * StreamHolderFactory
 *
 * @author ziyuan
 * @since 2017-01-10
 */
public class StreamHolderFactory implements PooledObjectFactory<StreamHolder> {

    private FKCExceptionHandler exceptionHandler;

    public StreamHolderFactory(FKCExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public PooledObject<StreamHolder> makeObject() throws Exception {
        StreamHolder streamHolder = new StreamHolder();
        return new DefaultPooledObject<>(streamHolder);
    }

    @Override
    public void destroyObject(PooledObject<StreamHolder> p) throws Exception {
        StreamHolder holder = p.getObject();
        holder.destroyHolder();
    }

    @Override
    public boolean validateObject(PooledObject<StreamHolder> p) {
        return false;
    }

    @Override
    public void activateObject(PooledObject<StreamHolder> p) throws Exception {
        StreamHolder holder = p.getObject();
        try {
            holder.activeHolder();
        } catch (Exception e) {
            exceptionHandler.dealException(e);
        }
    }

    @Override
    public void passivateObject(PooledObject<StreamHolder> p) throws Exception {
        StreamHolder holder = p.getObject();
        try {
            holder.closeHolder();
        } catch (Exception e) {
            exceptionHandler.dealException(e);
        }
    }
}
