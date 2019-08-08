package com.boge.threadpoolmonitor.threadpool;

import com.boge.threadpoolmonitor.factory.MonitorThreadFactory;
import com.boge.threadpoolmonitor.vo.ThreadPoolParam;

import java.util.concurrent.*;

/**
 * 被监控线程池
 */
public class MonitorThreadPoolExecutor extends ThreadPoolExecutor {

    public MonitorThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,String requestId) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,new MonitorThreadFactory(requestId),new MonitorRejectedExecutionHandler());
    }

    public MonitorThreadPoolExecutor(String requestId,ThreadPoolParam threadPoolParam) {
        this(threadPoolParam.getCoreSize(), threadPoolParam.getMaxSize(), threadPoolParam.getKeepAliveTime(),TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(threadPoolParam.getQueueCapacity()),requestId);
    }
}
