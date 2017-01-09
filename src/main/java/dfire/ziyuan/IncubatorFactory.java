/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan;

import dfire.ziyuan.exceptions.FKCException;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * IncubatorFactory 生产incubator的工厂
 *
 * @author ziyuan
 * @since 2017-01-06
 */
public enum IncubatorFactory {

    INSTANCE;

    /**
     * 使用java的spi获取一个Incubator
     *
     * @return
     */
    public Incubator getDefaultIncubator() throws FKCException {
        Incubator incubator;
        ServiceLoader<Incubator> serviceLoader = ServiceLoader.load(Incubator.class);
        Iterator<Incubator> incubators = serviceLoader.iterator();
        if (incubators.hasNext()) {
            incubator = incubators.next();
            return incubator;
        }
        throw new FKCException("没有找到相关的SPI扩展实现,请使用 new DefaultIncubator创建", System.currentTimeMillis());
    }

    public Incubator getDIYIncubator() throws FKCException {
        Incubator incubator;
        ServiceLoader<Incubator> serviceLoader = ServiceLoader.load(Incubator.class);
        Iterator<Incubator> incubators = serviceLoader.iterator();
        if (incubators.hasNext()) {
            incubator = incubators.next();
            return incubator;
        }
        throw new FKCException("没有找到相关的SPI扩展实现,请使用 new DefaultIncubator创建", System.currentTimeMillis());
    }
}
