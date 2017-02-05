/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan;

/**
 * KryoPoolImplType
 *
 * @author ziyuan
 * @since 2017-02-05
 */
public enum KryoPoolImplType {

    /**
     * 借助apache-commons中的genericObjPool的kryo缓存实现
     */
    APACHE_COMMONS,

    /**
     * 通过queue实现的比较轻量级的kryo缓存
     */
    QUEUE

}
