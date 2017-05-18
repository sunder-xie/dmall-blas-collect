package com.dmall.blas.collect.util;

import com.alibaba.fastjson.JSONObject;
import com.dmall.blas.collect.factory.ESTransportClientFactory;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lrkin on 2017/5/17.
 */
public class ESUtils {
    /**
     * 把JSON字符串中包含的数据转换成{@Map}对象后放入ES库中，支持JSON数组
     *
     * @param index         索引
     * @param type          类型
     * @param jsonData      json格式的数据
     * @param dataType      数据类型，也是json数据格式的
     * @param expiredSecond 多少秒后过期
     * @return
     */
    public static boolean saveWithJSON(String index, String type, String jsonData, String dataType, long expiredSecond) {

        Map<String, String> dataTypeMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(dataType)) {
            dataTypeMap = JSONObject.parseObject(dataType, Map.class);
        }

        if (StringUtils.startsWith(jsonData, "[")) {
            // 批量插入ES
            List<Map> dataList = JSONObject.parseArray(jsonData, Map.class);
            for (Map dataMap : dataList) {
                convertTypeIfNeed(dataMap, dataTypeMap);
                saveWithMap(index, type, dataMap, expiredSecond);
            }
            return true;
        }

        Map dataMap = JSONObject.parseObject(jsonData, Map.class);
        convertTypeIfNeed(dataMap, dataTypeMap);
        return saveWithMap(index, type, dataMap, expiredSecond);
    }

    /**
     * 把{@Map}对象存入ES库中
     *
     * @param index
     * @param type
     * @param dataMap
     */
    public static boolean saveWithMap(String index, String type, Map<String, Object> dataMap, long expiredSecond) {

        IndexRequestBuilder requestBuilder = ESTransportClientFactory.getESClient()
                .prepareIndex(index, type)
                .setSource(dataMap);

        if (expiredSecond > 0) {
            // 设置过期时间
            requestBuilder.setTTL(expiredSecond * 1000);
        }

        IndexResponse response = requestBuilder.execute().actionGet();
        return response.isCreated();
    }


    private static void convertTypeIfNeed(Map<String, Object> dataMap, Map<String, String> dataTypeMap) {

        if (dataTypeMap == null || dataTypeMap.isEmpty()) {
            return;
        }

        Set<Map.Entry<String, String>> entrySet = dataTypeMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            String key = entry.getKey();
            String type = entry.getValue();
            Object data = dataMap.get(key);
            if (type.equals("date") && data instanceof String) {
                dataMap.put(key, transferDate((String) data));
            }
        }
    }

    public static boolean deleteIndex(String index) {
        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(index);

        IndicesExistsResponse inExistsResponse = ESTransportClientFactory.getESClient().admin().indices()
                .exists(inExistsRequest).actionGet();

        if (inExistsResponse.isExists()) {
            DeleteIndexResponse dResponse = ESTransportClientFactory.getESClient()
                    .admin()
                    .indices()
                    .prepareDelete(index)
                    .execute().actionGet();
            return dResponse.isAcknowledged();
        }
        return false;
    }

    public static Date transferDate(String time) {
        if (Strings.isNullOrEmpty(time)) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        } catch (ParseException e) {
            return null;
        }
    }

    public static void createTTLIndex(String index, String type) throws IOException {
        //索引映射
        ESTransportClientFactory.getESClient().admin().indices().prepareCreate(index).execute().actionGet();

        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject(type)
                .startObject("_ttl").field("default", "2d").field("enable", true).endObject()
                .endObject()
                .endObject();
        PutMappingRequest mappingRequest = Requests.putMappingRequest(index).type(type).source(mapping);
        ESTransportClientFactory.getESClient().admin().indices().putMapping(mappingRequest).actionGet();
    }
}
