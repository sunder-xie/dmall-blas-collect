package com.dmall.blas.collect.service.impl;


import com.dmall.blas.collect.domain.EventDataQuery;
import com.dmall.blas.collect.enums.FileWriterType;
import com.dmall.blas.collect.factory.ESTransportClientFactory;
import com.dmall.blas.collect.service.CollectDataService;
import com.dmall.tool.basic.Page;
import com.dmall.tool.basic.toolkit.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CollectDataServiceImpl implements CollectDataService, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(CollectDataServiceImpl.class);

    // ES中的索引前缀
    private static final String ES_USER_PROFILE_INDEX_PREFIX = "dmall_dm_userprofile";
    // 索引最大保存天数
    private static final int INDEX_MAX_SAVE_DATA_DAYS = 3;
    // 最大提交线程数量
    private static int MAX_THREAD_COUNT = 20;
    // 用于ES异步提交的线程池
    private static ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_THREAD_COUNT);

    // 索引类型
    private String indexType;
    // ES客户端对象
    private TransportClient esClient;

    public CollectDataServiceImpl setEsClient(TransportClient esClient) {
        this.esClient = esClient;
        return this;
    }

    public void afterPropertiesSet() throws Exception {

    }

    public void destroy() throws Exception {

        long start = System.currentTimeMillis();
        logger.info("CollectDataService等待关闭....");
        try {

            BlockingQueue<Runnable> tasks = executorService.getQueue();
            if (tasks != null && !tasks.isEmpty()) {
                int spendTime = 15;
                int maxWaitMillSeconds = (tasks.size() / MAX_THREAD_COUNT) * spendTime;
                maxWaitMillSeconds = Math.min(5 * 60, maxWaitMillSeconds); // 最大等待5分钟
                maxWaitMillSeconds = Math.max(spendTime, maxWaitMillSeconds); // 最小等待15秒
                logger.info("正在提交ES的任务数量：{}, 最大用时{}秒", tasks.size(), maxWaitMillSeconds);
                boolean result = executorService.awaitTermination(maxWaitMillSeconds, TimeUnit.SECONDS);
                logger.info("if executorService terminated: {}", result);
            }
        } catch (Exception e) {
            logger.error("关闭ES提交线程executorService出错：", e);
        }
        logger.info("CollectDataService关闭执行完成, 耗时：{}毫秒", (System.currentTimeMillis() - start));
    }

    /**
     * 判断index是否存在
     *
     * @param indexName
     * @return
     */
    private boolean isIndexExists(String indexName) {
        try {
            IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);
            IndicesExistsResponse inExistsResponse = esClient.admin().indices()
                    .exists(inExistsRequest).actionGet();

            return inExistsResponse.isExists();

        } catch (Exception e) {
            logger.error("异常：", e);
        }

        return false;
    }

    public void asynSaveParsedData(Map<String, Object> eventParsedData) {

        if (eventParsedData == null) {
            return;
        }

        // 记录开始时间
        final long start = System.currentTimeMillis();

        final IndexRequestBuilder requestBuilder = esClient.prepareIndex("dmall-blas-collect", "log")
                .setSource(eventParsedData);
        // 异步插入
        executorService.execute(new Runnable() {
            public void run() {
                try {
                    IndexResponse response = requestBuilder.execute().actionGet();
                    logger.info("插入ES库耗时：{}毫秒, 结果：{}",
                            (System.currentTimeMillis() - start),
                            (response.isCreated() ? "成功" : "失败"));
                } catch (Exception e) {
                    logger.error("插入ES库失败：", e);
                }
            }
        });
    }

    public void asynBatchSaveParsedData(final List<Map<String, Object>> eventParsedData) {

        if (eventParsedData == null || eventParsedData.size() <= 0) {
            return;
        }

        // 记录开始时间
        final long start = System.currentTimeMillis();

        final BulkRequestBuilder bulkRequest = esClient.prepareBulk();
        for (Map<String, Object> data : eventParsedData) {
            bulkRequest.add(esClient.prepareIndex("dmall-blas-collect", "log")
                    .setSource(data));
        }
        // 异步插入
        executorService.execute(new Runnable() {
            public void run() {
                BulkResponse bulkResponse = bulkRequest.execute().actionGet();
                BulkItemResponse[] responses = bulkResponse.getItems();
                int failureCount = 0;
                for (BulkItemResponse response : responses) {
                    if (response.isFailed()) {
                        failureCount++;
                        if (response.getFailure() != null) {
                            logger.error("批量插入ES库出错: {}", response.getFailureMessage());
                        } else {
                            System.out.println("批量插入ES库成功");
                        }
                    }
                }
//                logger.info("批量插入{}条数据, 到ES耗时：{}毫秒, 失败：{}条",
//                        eventParsedData.size(),
//                        (System.currentTimeMillis() - start),
//                        failureCount);
            }
        });
    }
}