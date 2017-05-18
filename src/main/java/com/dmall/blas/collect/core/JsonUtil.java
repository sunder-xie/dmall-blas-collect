package com.dmall.blas.collect.core;

/**
 * Created by lrkin on 2017/5/18.
 */

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

public class JsonUtil {

    // Java实体对象转json对象
    public static String model2Json(Blog blog) {
        String jsonData = null;
        try {
            XContentBuilder jsonBuild = XContentFactory.jsonBuilder();
            jsonBuild.startObject().field("id", blog.getId()).field("title", blog.getTitle())
                    .field("posttime", blog.getPosttime()).field("content", blog.getContent()).field("updatetime", blog.getUpdatetime()).endObject();

//            jsonBuild.startObject().field("id", blog.getId()).field("title", blog.getTitle())
//                    .field("posttime", blog.getPosttime()).field("content", blog.getContent()).endObject();
            jsonData = jsonBuild.string();
            //System.out.println(jsonData);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonData;
    }

}