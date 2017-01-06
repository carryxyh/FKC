/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan.exceptions;

/**
 * FKCException FKCException
 *
 * @author ziyuan
 * @since 2017-01-06
 */
public class FKCException extends Exception {

    private long timestamp;

    private FKCException() {

    }

    public FKCException(String message, long timestamp) {
        super(message);
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
