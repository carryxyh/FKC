/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan;

import dfire.ziyuan.exceptions.FKCException;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TestBorn
 *
 * @author ziyuan
 * @since 2017-01-10
 */
public class TestBorn extends BaseTest {

    @Test
    public void testborn() throws FKCException, IOException {
        Item t1 = new Item();
        List<Item> childs = new ArrayList<>();
        Item t2 = new Item();
        childs.add(t2);
        t1.setChildItems(childs);
        t1.setMemo("test memo");
        t2.setMenuId("dfasjfasdjlkasdfjlk");
        System.out.println(t1);
        System.out.println(t2);
//        Incubator<Item> incubator = IncubatorFactory.INSTANCE.getIncubator();
//        Item newOne = incubator.born(t1);
//        System.out.println(newOne);
//        System.out.println(newOne.getChildItems().get(0));
//        incubator.shutdown();
        KryoPoolConfig kryoPoolConfig = new KryoPoolConfig(KryoPoolImplType.APACHE_COMMONS);
        kryoPoolConfig.setMaxTotal(1);
        kryoPoolConfig.setMinIdle(1);
        Incubator<Item> incubator = IncubatorFactory.INSTANCE.getIncubator(kryoPoolConfig);
        Item newOne = incubator.born(t1);
        Item newOne1 = incubator.born(t1);
        Item newOne2 = incubator.born(t1);
        Item newOne3 = incubator.born(t1);
        System.out.println(newOne);
        System.out.println(newOne1);
        System.out.println(newOne2);
        System.out.println(newOne3);
        System.out.println(newOne.getChildItems().get(0));

        System.in.read();
    }
}
