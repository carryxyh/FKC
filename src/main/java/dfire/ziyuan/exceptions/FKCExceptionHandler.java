/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan.exceptions;

import java.io.Serializable;

/**
 * FKCExceptionHandler
 *
 * @author ziyuan
 * @since 2017-01-10
 */
public interface FKCExceptionHandler extends Serializable {

    /**
     * 处理异常
     *
     * @param e
     */
    void dealException(Exception e);
}
