package com.dmall.blas.collect.core;

import com.dmall.tool.basic.toolkit.BasicToolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lrkin on 2017/5/18.
 */
public abstract class AppLifeCycleManager {

    private static Logger logger = LoggerFactory.getLogger(AppLifeCycleManager.class);

    /*任务个数信号量*/
    private static CountDownLatch taskSignal = new CountDownLatch(1);
    /*主线程停止心好累*/
    private static CountDownLatch stoppedSignal;
    /*存储任务线程ID*/
    private static Set<Long> taskIds = new HashSet<Long>();
    /*生命周期锁*/
    private static ReentrantLock lifeCycleLock = new ReentrantLock();

    private static final String EXIT_LOCK_FILE_PATH = "./system.exit.lock";

    /**
     * 由主线程调用，等待任务线程完成后退出
     */
    public static void waitStopped() {

        while (true) {
            BasicToolkit.sleepMs(1000);
            if (checkSystemExitFlag()) {
                exitAfterFinished();
                break;
            }
        }

        cleanSystemExitFlag();
    }

    private static boolean checkSystemExitFlag() {
        File file = new File(EXIT_LOCK_FILE_PATH);
        return file.exists();
    }

    private static void cleanSystemExitFlag() {
        if (new File(EXIT_LOCK_FILE_PATH).exists()) {
            new File(EXIT_LOCK_FILE_PATH).delete();
        }
    }

    /**
     * 等待所有任务完成后退出, 最大等待时长5分钟
     *
     * @throws InterruptedException
     */
    private static void exitAfterFinished() {
        if (taskIds.isEmpty()) {
            logger.info("无任务，准备程序退出....");
            return;
        }

        if (stoppedSignal == null) {
            // 加锁，防止还会有任务执行注册操作
            lifeCycleLock.lock();
            stoppedSignal = new CountDownLatch(taskIds.size());
            lifeCycleLock.unlock();

            try {
                logger.info("等待{}个任务完成....", taskIds.size());
                stoppedSignal.await(5, TimeUnit.MINUTES);
                // 再等待10秒
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("任务已完成，准备退出程序....");
        }
    }

    /**
     * 由任务线程调用，检查是否需要停止,如果需要停止。
     */
    public static void stopIfNeed() {

        registerTaskIfNeed();

        if (stoppedSignal != null) {
            stoppedSignal.countDown();
            try {
                // 阻塞
                taskSignal.await();
            } catch (InterruptedException e) {

            }
        }
    }

    /**
     * 如果任务未注册则注册当前任务，如果注册时候发起终止程序时，将不执行注册操作并阻塞当前线程
     */
    private static void registerTaskIfNeed() {
        if (!taskIds.contains(Thread.currentThread().getId())) {
            lifeCycleLock.lock();
            if (stoppedSignal != null) {
                try {
                    // 阻塞当前线程
                    taskSignal.await();
                } catch (InterruptedException e) {
                }
            }
            taskIds.add(Thread.currentThread().getId());
            lifeCycleLock.unlock();
        }
    }


}
