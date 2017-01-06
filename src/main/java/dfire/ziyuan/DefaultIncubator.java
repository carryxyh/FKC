/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan;

import com.esotericsoftware.kryo.Kryo;
import dfire.ziyuan.utils.PropertyUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * DefaultIncubator 默认的incubator,使用spi机制创建
 *
 * @author ziyuan
 * @since 2017-01-06
 */
public class DefaultIncubator<T> implements Incubator<T> {

    /**
     * 是否是关闭了的
     */
    private boolean isClosed = false;

    private GenericObjectPool<ObjectInputStream> objInputStreamPool;

    private GenericObjectPool<ObjectOutputStream> objOutputStreamPool;

    private GenericObjectPool<Kryo> kryoPool;

    public DefaultIncubator() {
        init();
    }

    /**
     * 进行初始化操作
     */
    private void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                doShutDown();
            }
        }));
        GenericObjectPoolConfig objInput = new GenericObjectPoolConfig();
        GenericObjectPoolConfig objOutput = new GenericObjectPoolConfig();
        GenericObjectPoolConfig kryo = new GenericObjectPoolConfig();
        PropertyUtils.dealPoolConfig(objInput, SerializerType.OBJ_INPUT);
        PropertyUtils.dealPoolConfig(objOutput, SerializerType.OBJ_OUTPUT);
        PropertyUtils.dealPoolConfig(kryo, SerializerType.KRYO);
        
    }

    @Override
    public T born(T template) {
        return null;
    }

    @Override
    public void shutdown() {
        doShutDown();
    }

    /**
     * 执行停止操作
     */
    public void doShutDown() {
        if (isClosed) {
            return;
        }
    }
}
