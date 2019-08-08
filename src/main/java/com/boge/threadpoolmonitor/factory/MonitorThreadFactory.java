package com.boge.threadpoolmonitor.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class MonitorThreadFactory implements ThreadFactory {

    private String requestId;

    private final static String THREAD_PREFIX = "thread-";

    private final static AtomicLong ID = new AtomicLong(1);

    public MonitorThreadFactory(String requestId){
        this.requestId = requestId+"-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(THREAD_PREFIX+requestId+ID.getAndAdd(1));
        return thread;
    }
}
