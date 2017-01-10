/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan;

import dfire.ziyuan.exceptions.FKCExceptionHandler;

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
    T born(T template) throws Exception;

    /**
     * 关闭
     */
    void shutdown();

    /**
     * 给incubator设置异常处理器
     *
     * @param exceptionHandler
     */
    void setExceptionHandler(FKCExceptionHandler exceptionHandler);
}
