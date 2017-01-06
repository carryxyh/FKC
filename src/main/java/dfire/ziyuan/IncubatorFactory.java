/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan;

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
    public Incubator getIncubator() {
        Incubator incubator = null;
        ServiceLoader<Incubator> serviceLoader = ServiceLoader.load(Incubator.class);
        Iterator<Incubator> incubators = serviceLoader.iterator();
        if (incubators.hasNext()) {
            incubator = incubators.next();
        }
        return incubator;
    }
}
