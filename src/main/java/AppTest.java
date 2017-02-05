/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */

import dfire.ziyuan.Incubator;
import dfire.ziyuan.IncubatorFactory;
import dfire.ziyuan.Item;
import dfire.ziyuan.exceptions.FKCException;

import java.util.ArrayList;
import java.util.List;

/**
 * AppTest
 *
 * @author ziyuan
 * @since 2017-01-10
 */
public class AppTest {

    public static void main(String[] args) throws FKCException {
        Item t1 = new Item();
        List<Item> childs = new ArrayList<>();
        Item t2 = new Item();
        childs.add(t2);
        t1.setChildItems(childs);
        t1.setMemo("test memo");
        t2.setMenuId("dfasjfasdjlkasdfjlk");
        Incubator<Item> incubator = IncubatorFactory.INSTANCE.getIncubator();
        Item newOne = incubator.born(t1);
        System.out.println(newOne);
        System.out.println(newOne.getChildItems().get(0));
    }
}
