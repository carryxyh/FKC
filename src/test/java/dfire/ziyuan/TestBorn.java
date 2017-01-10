/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package dfire.ziyuan;

import dfire.ziyuan.exceptions.FKCException;
import org.junit.Test;

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
    public void testborn() throws FKCException {
        Item t1 = new Item();
        List<Item> childs = new ArrayList<>();
        Item t2 = new Item();
        childs.add(t2);
        t1.setChildItems(childs);
        t1.setMemo("test memo");
        t2.setMenuId("dfasjfasdjlkasdfjlk");
        Incubator<Item> incubator = IncubatorFactory.INSTANCE.getDefaultIncubator();
        Item newOne = incubator.born(t1);
        System.out.println(newOne);
        System.out.println(newOne.getChildItems().get(0));
    }

}
