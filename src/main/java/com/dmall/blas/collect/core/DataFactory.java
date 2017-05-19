package com.dmall.blas.collect.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrkin on 2017/5/18.
 */
public class DataFactory {
    public static DataFactory dataFactory = new DataFactory();

    private DataFactory() {
    }

    public DataFactory getInstance() {
        return dataFactory;
    }

    public static List<String> getInitJsonData() {
        List<String> list = new ArrayList<String>();
        String data1 = JsonUtil.model2Json(new Blog(1 + 5, "git简介", "2016-06-19", "SVN与Git最主要的区别...", "2016-07-21"));
        String data2 = JsonUtil.model2Json(new Blog(2 + 5, "Java中泛型的介绍与简单使用", "2016-06-19", "学习目标 掌握泛型的产生意义...", "2016-07-21"));
        String data3 = JsonUtil.model2Json(new Blog(3 + 5, "SQL基本操作", "2016-06-19", "基本操作：CRUD ...", "2016-07-21"));
        String data4 = JsonUtil.model2Json(new Blog(4 + 5, "Hibernate框架基础", "2016-06-19", "Hibernate框架基础...", "2016-07-21"));
        String data5 = JsonUtil.model2Json(new Blog(5 + 5, "Shell基本知识", "2016-06-19", "Shell是什么...", "2016-07-21"));

//        String data1 = JsonUtil.model2Json(new Blog(1, "git简介", "2016-06-19", "SVN与Git最主要的区别..."));
//        String data2 = JsonUtil.model2Json(new Blog(2, "Java中泛型的介绍与简单使用", "2016-06-19", "学习目标 掌握泛型的产生意义..."));
//        String data3 = JsonUtil.model2Json(new Blog(3, "SQL基本操作", "2016-06-19", "基本操作：CRUD ..."));
//        String data4 = JsonUtil.model2Json(new Blog(4, "Hibernate框架基础", "2016-06-19", "Hibernate框架基础..."));
//        String data5 = JsonUtil.model2Json(new Blog(5, "Shell基本知识", "2016-06-19", "Shell是什么..."));
        list.add(data1);
        list.add(data2);
        list.add(data3);
        list.add(data4);
        list.add(data5);
        return list;
    }
}