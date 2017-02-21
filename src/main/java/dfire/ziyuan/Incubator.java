/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan;

import dfire.ziyuan.exceptions.FKCException;

import java.io.Serializable;

/**
 * Incubator 孵化器,生产新实例的接口
 *
 * @author ziyuan
 * @since 2017-01-06
 */
public interface Incubator<T> extends Serializable {

    /**
     * 生产一个新的对象
     *
     * @param template 模板
     * @return 新的对象
     */
    T born(T template) throws FKCException;

    /**
     * 关闭
     */
    void shutdown();
}
