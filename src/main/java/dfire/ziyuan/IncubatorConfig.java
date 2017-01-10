/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan;

import dfire.ziyuan.exceptions.FKCExceptionHandler;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * IncubatorConfig
 *
 * @author ziyuan
 * @since 2017-01-10
 */
public class IncubatorConfig {

    private FKCExceptionHandler exceptionHandler;

    private GenericObjectPoolConfig genericObjectPoolConfig;

    private AbandonedConfig abandonedConfig;

    private KryoPoolConfig kryoPoolConfig;

    public KryoPoolConfig getKryoPoolConfig() {
        return kryoPoolConfig;
    }

    public void setKryoPoolConfig(KryoPoolConfig kryoPoolConfig) {
        this.kryoPoolConfig = kryoPoolConfig;
    }

    public FKCExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(FKCExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public GenericObjectPoolConfig getGenericObjectPoolConfig() {
        return genericObjectPoolConfig;
    }

    public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
        this.genericObjectPoolConfig = genericObjectPoolConfig;
    }

    public AbandonedConfig getAbandonedConfig() {
        return abandonedConfig;
    }

    public void setAbandonedConfig(AbandonedConfig abandonedConfig) {
        this.abandonedConfig = abandonedConfig;
    }
}
