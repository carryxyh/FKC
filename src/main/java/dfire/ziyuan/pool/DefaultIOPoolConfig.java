/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * DefaultIOPoolConfig
 *
 * @author ziyuan
 * @since 2017-01-09
 */
public class DefaultIOPoolConfig {

    /**
     * 默认先进后出
     */
    private static final boolean LIFO = true;

    /**
     * 默认池内最大空闲对象
     */
    private static final int MAX_IDLE = 30;

    /**
     * 默认池内最大数量
     */
    private static final int MAX_TOTAL = 60;

    /**
     * 默认最小空闲数量
     */
    private static final int MIN_IDLE = 5;

    /**
     * 每次eviction检查两个对象
     */
    private static final int NUMTESTS_PER_EVICTION = 2;

    /**
     * 用光的情况下不阻塞
     */
    private static final boolean GET_WHEN_EXHAUSTED = false;

    /**
     * 5分钟检查一次整个池
     */
    private static final long TIME_BETWEEN_EVICTION_RUN = 5L * 60L * 1000L;

    /**
     * 获取对象时不检查
     */
    private static final boolean TEST_ON_BORROW = false;

    /**
     * 创建时不检查
     */
    private static final boolean TEST_ON_CREATE = false;

    /**
     * 归还时不检查
     */
    private static final boolean TEST_ON_RETURN = false;

    public static GenericObjectPoolConfig getConfig() {
        GenericObjectPoolConfig defaultConfig = new GenericObjectPoolConfig();
        defaultConfig.setLifo(LIFO);
        defaultConfig.setMaxIdle(MAX_IDLE);
        defaultConfig.setMaxTotal(MAX_TOTAL);
        defaultConfig.setMinIdle(MIN_IDLE);
        defaultConfig.setNumTestsPerEvictionRun(NUMTESTS_PER_EVICTION);
        defaultConfig.setBlockWhenExhausted(GET_WHEN_EXHAUSTED);
        defaultConfig.setTimeBetweenEvictionRunsMillis(TIME_BETWEEN_EVICTION_RUN);
        defaultConfig.setTestOnBorrow(TEST_ON_BORROW);
        defaultConfig.setTestOnCreate(TEST_ON_CREATE);
        defaultConfig.setTestOnReturn(TEST_ON_RETURN);
        return defaultConfig;
    }
}
