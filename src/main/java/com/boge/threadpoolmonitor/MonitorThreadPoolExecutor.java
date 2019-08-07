package com.boge.threadpoolmonitor;

import com.boge.threadpoolmonitor.factory.MonitorThreadFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 被监控线程池
 */
public class MonitorThreadPoolExecutor extends ThreadPoolExecutor {

    public MonitorThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,new MonitorThreadFactory(),new MonitorRejectedExecutionHandler());
    }
}
