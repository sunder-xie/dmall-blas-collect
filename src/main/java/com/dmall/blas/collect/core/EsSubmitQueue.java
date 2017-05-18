package com.dmall.blas.collect.core;

import com.dmall.blas.collect.service.CollectDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by lrkin on 2017/5/18.
 */
public class EsSubmitQueue implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(EsSubmitQueue.class);

    private static Queue<Map<String, Object>> dataQueue = new LinkedBlockingQueue<Map<String, Object>>();

    private int taskCount = 3; // 开启入处理数据的任务数量
    private int maxCommitCount = 100; // 批量提交数据的最大数量

    @Resource
    private CollectDataService collectDataService;

    private EsSubmitQueue() {
    }

    public void setMaxCommitCount(int maxCommitCount) {
        this.maxCommitCount = maxCommitCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        startTask();
    }

    /**
     * 开启任务
     */
    private void startTask() {
        for (int i = 0; i < taskCount; i++) {
            new Task(collectDataService).start();
        }
    }

    /**
     * 把数据放入队列
     *
     * @param eventData
     * @return
     */
    public boolean append(Map<String, Object> eventData) {
        try {
            return dataQueue.offer(eventData);
        } catch (Exception e) {
            logger.error("插入队列出错", e);
            return false;
        }
    }

    private class Task extends Thread {

        private List<Map<String, Object>> dataCache = new ArrayList<Map<String, Object>>();
        private CollectDataService collectDataService;
        private int sleepTimes = 0; // 记录连续休息次数
        private int maxSleepTimes = 3; // 最大连续休息次数

        public Task(CollectDataService collectDataService) {
            this.collectDataService = collectDataService;
        }

        @Override
        public void run() {
            AppLifeCycleManager.stopIfNeed();
            while (true) {
                saveDataIfNeed();
                if (dataQueue.isEmpty()) {
                    // 直到队列中没有元素时检查是否需要停止程序
                    AppLifeCycleManager.stopIfNeed();
                }
            }
        }

        private void saveDataIfNeed() {

            Map<String, Object> data = dataQueue.poll();
            if (data == null) {
                try {
                    Thread.sleep(1000);
                    sleepTimes++;
                } catch (InterruptedException e) {

                }
                if (dataCache.isEmpty() || sleepTimes % maxSleepTimes != 0) { // 为空或者，休眠过3次
                    return;
                }
                commit();
                return;
            }

            dataCache.add(data);
            if (dataCache.size() >= maxCommitCount) {
                commit();
            }
        }

        private void commit() {
            collectDataService.asynBatchSaveParsedData(dataCache);
            clearSleepTime();
            dataCache = new ArrayList<Map<String, Object>>();
        }

        /**
         * 清楚休眠次数
         */
        private void clearSleepTime() {
            sleepTimes = 0;
        }

        public Task setMaxSleepTimes(int maxSleepTimes) {
            this.maxSleepTimes = maxSleepTimes;
            return this;
        }
    }
}
