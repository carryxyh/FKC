/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan.exceptions;

/**
 * DefaultFKCExceptionHandler
 *
 * @author ziyuan
 * @since 2017-01-10
 */
public class DefaultFKCExceptionHandler implements FKCExceptionHandler {

    @Override
    public void dealException(Exception e) {
        System.out.println(e.getMessage());
    }
}
